package cn.wow.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.enable(MapperFeature.USE_ANNOTATIONS);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static String toJson(Object obj) {

		return toJson(mapper.writerWithDefaultPrettyPrinter(), obj);

	}

	public static String toJsonWithoutPretty(Object obj) {
		return toJson(mapper.writer(), obj);
	}

	static String toJson(ObjectWriter writer, Object obj) {
		String json = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			writer.writeValue(baos, obj);
		//	json = baos.toString();
			byte[] lens = baos.toByteArray();
			json = new String(lens, "utf-8");
		} catch (Exception ex) {
			throw new RuntimeException("JsonUtil", ex);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				throw new RuntimeException("JsonUtil", e);
			}
		}
		return json;
	}

	public static <T> T fromJson(String json) {
		T obj = null;
		TypeReference<T> ref = new TypeReference<T>() {
		};
		try {
			InputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
			obj = mapper.<T> readValue(is, ref);
		} catch (Exception e) {
			throw new RuntimeException("JsonUtil", e);
		}
		return obj;
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		T obj = null;
		try {
			obj = getJsonObj(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("JsonUtil", e);
		}
		return obj;
	}

	public static <T> T getJsonObj(String json, Class<T> clazz)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		T obj;
		InputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
		JavaType type = TypeFactory.defaultInstance().constructType(clazz);
		obj = mapper.<T> readValue(is, type);
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Collection<T> fromJson(String json, Class<? extends Collection> collectionType,
			Class<T> elementType) {
		Collection<T> objects = null;
		try {
			InputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
			JavaType type = TypeFactory.defaultInstance().constructCollectionType(collectionType, elementType);
			objects = mapper.readValue(is, type);
		} catch (Exception e) {
			throw new RuntimeException("JsonUtil", e);
		}
		return objects;
	}

	@SuppressWarnings("rawtypes")
	public static <K, V> Map<K, V> fromJson(String json, Class<? extends Map> mapType, Class<K> keyType,
			Class<V> valueType) {
		Map<K, V> map = null;
		try {
			InputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
			JavaType type = TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType);
			map = mapper.readValue(is, type);
		} catch (Exception e) {
			throw new RuntimeException("JsonUtil", e);
		}
		return map;
	}

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

}
