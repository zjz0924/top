package cn.wow.operationlog.autolog;

import java.io.Serializable;

public interface AutoLogAdapter extends Serializable
{
   public String getUserName(Object[] args);

}
