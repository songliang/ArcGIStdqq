package com.esri.arcgis.android.samples.tableItem;

import java.lang.reflect.Field;

public interface IExportFunction {
	  /*public String getCvsTitle(){
		  Field fs[] = this.getClass().getDeclaredFields();
		  String result="";
		  for (int i = 0; i < fs.length; i++) {
			  Field f = fs[i];
			  result+=f.getName();
			  if (i<fs.length)
				  result+=",";
		        //System.out.println(f.getType().getName() + " " + f.getName());
		  		}
		  return result;
	  }
	  public String getCvsValue(){
		  
		return null;
	  }*/
	public String getCvsTitle();
	public String getCvsData();
	public void setId(int id) ;
}