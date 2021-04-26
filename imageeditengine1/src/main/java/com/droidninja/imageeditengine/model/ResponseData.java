package com.droidninja.imageeditengine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData {

   @SerializedName("template")
   @Expose
   private Template template;

   public Template getTemplate() {
       return template;
   }

   public void setTemplate(Template template) {
       this.template = template;
   }

}
