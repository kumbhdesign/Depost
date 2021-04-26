package com.droidninja.imageeditengine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Template {

   @SerializedName("error")
   @Expose
   private Integer error;
   @SerializedName("list_template")
   @Expose
   private ListTemplate listTemplate;

   public Integer getError() {
       return error;
   }

   public void setError(Integer error) {
       this.error = error;
   }

   public ListTemplate getListTemplate() {
       return listTemplate;
   }

   public void setListTemplate(ListTemplate listTemplate) {
       this.listTemplate = listTemplate;
   }

}
