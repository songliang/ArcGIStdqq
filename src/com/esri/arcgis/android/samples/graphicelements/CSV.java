package com.esri.arcgis.android.samples.graphicelements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CSV {
	public ArrayList<String[]>  readeCsv(String filepath) throws IOException{  
			CsvReader reader ;
	        ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据  
	        String csvFilePath = filepath;  
	        reader = new CsvReader(csvFilePath,',',Charset.forName("GBK"));    //一般用这编码读就可以了      
	        reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
	         while(reader.readRecord()){ //逐行读入除表头的数据      
	             csvList.add(reader.getValues());  
	         }
	         reader.close();  
	           
	        /* for(int row=0;row<csvList.size();row++){  
	               
	             String  cell = csvList.get(row)[0]; //取得第row行第0列的数据  
	             System.out.println(cell);  	               
	         }*/
	         return csvList;
	}
	public int GetCount(String path) throws IOException {
		 CsvReader reader;
		 int i=0;
			reader = new CsvReader(path,',',Charset.forName("GBK"));			
				while(reader.skipRecord()){
				     i++;  
				 }
	         reader.close();
         return i;
           
	} 
	public String[] getPointValue(String path,int id) throws IOException {
		 CsvReader reader;
		 id++;
			reader = new CsvReader(path,',',Charset.forName("GBK"));			
				while(id>0){
				     reader.skipRecord();
				     id--;
				 }
        String[] valueStrings=reader.getValues();
        reader.close();
        return valueStrings;
          
	} 
	public boolean AddCsv(String path,String[] value) throws IOException{
			FileOutputStream stream;// provides file access 
			OutputStreamWriter writer;// writes to the file
			stream = new FileOutputStream(path,true);
			writer = new OutputStreamWriter(stream,"GBK");
			String string = "";
			for (int i = 0; i < value.length; i++) {
				if (i==0) {
					string=value[i];
				}
				else {
					string=string+","+value[i];
				}				
			}
			writer.append(string); 
			writer.append("\n"); 
			writer.close(); 
			return true;
	}
	
	/** 
	 * 写入CSV文件 
	 * @throws IOException 
	 */  
	public void writeCsv(String path,String[] value) throws IOException{  
	         CsvWriter wr =new CsvWriter(path,',',Charset.forName("GBK"));                      
	         wr.writeRecord(value);	         
	         wr.close();  
	}
}
