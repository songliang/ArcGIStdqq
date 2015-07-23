package com.esri.arcgis.android.samples.graphicelements;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

public class FileUtils {
    public static String getPath(Context context, Uri uri) {
 
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
 
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } 
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
 
        return null;
    }
  //判断文件是否存在；
  	public static boolean fileIsExists(String path){
                  File f=new File(path);
                 return f.exists();
      }
  	public static boolean isSDcardExsit() {
  		String status = Environment.getExternalStorageState();
  		  if (status.equals(Environment.MEDIA_MOUNTED)) {
  		   return true;
  		  } else {
  		   return false;
  		  }
  	}
  	//"/sdcard/tempdir"
  	/**
  	 * @param strFolder
  	 * 文件夹是否存在，如过存在，返回true，不存在则返回false； 
  	 * @return
  	 */
  	public static boolean isFolderExist(String strFolder)    
      { 
             File file = new File(strFolder); 
             return file.exists();
      }
  	/**
  	 * @param strFolder
  	 * @return 创建文件夹；
  	 */
  	public static boolean CreatFloder(String strFolder)
  	{
  		File file = new File(strFolder); 
  	    return file.mkdir(); 
  	}
    /**如果存在，则不做处理；如果不存在，则创建；
     * @param path,"hhaudio/jifwoejf"
     * @param filename,"jfiwe.txt"
     */
    public static void CreateCvs(String path,String filename)
    {  
        File file = new File(path);  
        if (!file.exists()) {  
            try {  
                //按照指定的路径创建文件夹  
                file.mkdirs();  
            } catch (Exception e) {  
                // TODO: handle exception  
            }  
        }  
        File dir = new File(path+"/"+filename);  
        if (!dir.exists()) {  
              try {  
                  //在指定的文件夹中创建文件  
                  dir.createNewFile();  
            } catch (Exception e) {  
            }  
        }    
    }  
    public static void CreatFile(String path) {
    	 File dir = new File(path);  
         if (!dir.exists()) {  
               try {  
                   //在指定的文件夹中创建文件  
                   dir.createNewFile();  
             } catch (Exception e) {  
             }  
         }   
	}
  	/***保存到sd卡下的.ainibaichi文件夹下的准确可行的代码****/
//      public void saveToSDCard(String filename,String content) throws Exception
//      {
//          String dir=Environment.getExternalStorageDirectory()+"/.ainibaichi";
//          java.io.File a=new java.io.File(dir);
//         /***判断文件夹是否存在，不存在则创建***/
//          if (!a.exists()){
//              a.mkdir();
//          }
//          File file=new File(a,filename);
//          FileOutputStream outStream=new FileOutputStream(file);
//          outStream.write(content.getBytes());
//          outStream.close();
//      }
}