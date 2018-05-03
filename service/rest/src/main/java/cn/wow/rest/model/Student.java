package cn.wow.rest.model;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class Student
{
   @Size(min = 3, max = 10)
   @NotEmpty
   private String name;
   
   private Integer age;
   
   @NotEmpty
   private String sex;
   
   public String getName()
   {
      return name;
   }
   public void setName(String name)
   {
      this.name = name;
   }
   public Integer getAge()
   {
      return age;
   }
   public void setAge(Integer age)
   {
      this.age = age;
   }
   public String getSex()
   {
      return sex;
   }
   public void setSex(String sex)
   {
      this.sex = sex;
   }
}
