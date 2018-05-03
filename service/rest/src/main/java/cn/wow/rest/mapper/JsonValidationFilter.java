package cn.wow.rest.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ContainerException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import cn.wow.rest.exception.Exceptions.DuplicatedFieldsException;

@Provider
public class JsonValidationFilter implements ContainerRequestFilter
{

   @Override
   public void filter(ContainerRequestContext requestContext) throws IOException
   {
      MediaType mediaType = requestContext.getMediaType();
      if (mediaType != null && "application".equalsIgnoreCase(mediaType.getType()) && "json".equalsIgnoreCase(mediaType.getSubtype()))
      {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         InputStream in = requestContext.getEntityStream();
         try
         {
            if (in.available() > 0)
            {
               ReaderWriter.writeTo(in, out);
               byte[] requestEntity = out.toByteArray();
               String json = new String(requestEntity, "UTF-8");
               final JsonFactory factory = new JsonFactory();
               final JsonParser parser = factory.createParser(json);
               jsonDuplicateDetector(parser.nextToken(), parser);
               requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
            }
         }
         catch (JsonParseException ex)
         {
            throw ex;
         }
         catch (DuplicatedFieldsException ex)
         {
            throw ex;
         }
         catch (IOException ex)
         {
            throw new ContainerException(ex);
         }
      }
   }

   private static void jsonDuplicateDetector(final JsonToken startToken, final JsonParser parser) throws IOException
   {

      JsonToken endToken;

      if(startToken == JsonToken.VALUE_NULL)
      {
         return;
      }

      if (startToken == JsonToken.START_ARRAY)
      {
         endToken = JsonToken.END_ARRAY;
      }
      else if (startToken == JsonToken.START_OBJECT)
      {
         endToken = JsonToken.END_OBJECT;
      }
      else
      {
         throw new JsonParseException(parser, "Unexpected token value " + startToken, parser.getCurrentLocation());
      }
      doCheck(parser, endToken);
   }

   private static void doCheck(JsonParser parser, JsonToken endToken) throws IOException
   {
      JsonToken token;
      JsonParser parser_local = parser;
      List<String> fieldNames = new ArrayList<String>();
      while ((token = parser_local.nextToken()) != endToken)
      {
         if (token == JsonToken.FIELD_NAME)
         {
            if (fieldNames.contains(parser_local.getCurrentName()))
            {
               String msg = "Duplicate entry '" + parser_local.getCurrentName() + "', ";
               msg += "line " + parser_local.getCurrentLocation().getLineNr() + ", ";
               msg += "column " + parser_local.getCurrentLocation().getColumnNr() + ".";
               throw new DuplicatedFieldsException(msg);
            }
            fieldNames.add(parser_local.getCurrentName());
         }
         else if (token == JsonToken.START_ARRAY)
         {
            jsonDuplicateDetector(token, parser_local);
            parser_local = parser_local.skipChildren();
         }
         else if (token == JsonToken.START_OBJECT)
         {
            jsonDuplicateDetector(token, parser_local);
            parser_local = parser_local.skipChildren();
         }
      }
   }
}
