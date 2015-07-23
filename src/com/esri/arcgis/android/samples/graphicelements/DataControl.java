package com.esri.arcgis.android.samples.graphicelements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;
import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.IExportFunction;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;

import android.R.integer;
import android.content.Context;
import android.os.Environment;

/**
 * @author ZHAO
 * 高级操作了，进行point列表的生成，读写操作；当文件夹不存在的时候，自动创建文件夹；当文件不存在的时候，创建文件；
 */
public class DataControl {
	Context _context;
	Profile profile;
	CSV _cCsv;
	String pointCvsString;	
	public DataControl(Context context)
	{
		_context=context;
		profile=new Profile(context);
		_cCsv=new CSV();
		CreatDefaultFloder();
		//CreatPointTitile(CreatDefaultFiles(R.string.PointCvs),R.array.PointCvsTitle);
		//CreatDefaultFiles(R.string.JieZhiPoint);
		//CreatDefaultFiles(R.string.JieZhi);
	}
	/**检查默认的Point表是否存在；如果不存在，创建一个；并返回路径；
	 * @return
	 */
	private String CreatDefaultFiles(int path) {
		// TODO Auto-generated method stub
		String pathString=profile.getString(R.string.DefaultFloderPath)+"/"+_context.getResources().getString(path)+".csv";		
		if (!FileUtils.fileIsExists(pathString)) {
			FileUtils.CreatFile(pathString);
		}
		profile.Save(path, pathString);			
		return pathString;
	}
	/**检查表的表头是否正确；因为如果是导出，用户习惯剪切，然后删除，或者新建一个空白的；因此需要检查表头；
	 * 如果是空，则添加一个表头；如果不是空，则不用处理了；
	 * @param pathString
	 */
	private void CreatPointTitile(String pathString,int title)
	{
		ArrayList<String[]> valueArrayList;
		try {
			valueArrayList = _cCsv.readeCsv(pathString);
			if(valueArrayList==null||(valueArrayList.size()<1))_cCsv.writeCsv(pathString,_context.getResources().getStringArray(title));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	/**创建默认文件夹；
	 * 
	 */
	private void CreatDefaultFloder() {
		if(profile.getString(R.string.DefaultFloderPath)!=null&&profile.getString(R.string.DefaultImageFloder)!=null)//检查配置文件中，文件夹字段是否存在；
		{
			if(FileUtils.isFolderExist(profile.getString(R.string.DefaultFloderPath))&&FileUtils.isFolderExist(profile.getString(R.string.DefaultImageFloder)))//如果配置中路径关键字在，那看看文件加在不在；如过存在，就可以返回了；
			{
				return;
			}
		}
		String path;
		String pathimg;
		if(FileUtils.isSDcardExsit())//检测是否有sd卡，如果有，则保存到sd卡上；
		{
			path=Environment.getExternalStorageDirectory()+"/"+_context.getResources().getString(R.string.DefaultFloderPath);
			pathimg=path+"/"+_context.getResources().getString(R.string.DefaultImageFloder);
			FileUtils.CreatFloder(path);
			FileUtils.CreatFloder(pathimg);
		}
		else {//如果没有sd卡，则保存到文件夹的私有空间上；
			path=_context.getResources().getString(R.string.DefaultFloderPath);		
			pathimg=path+"/"+_context.getResources().getString(R.string.DefaultFloderPath);	
			FileUtils.CreatFloder(path);
			FileUtils.CreatFloder(pathimg);
		}
		profile.Save(R.string.DefaultFloderPath, path);
		profile.Save(R.string.DefaultImageFloder, pathimg);
	}
	/*public boolean AddPointValue(String[] value)
	{
		String path=profile.getString(R.string.PointCvs);
		try {
			value[0]=Integer.toString(_cCsv.GetCount(path));
			_cCsv.AddCsv(path, value);		
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}*/
	/*public String[] getPointValue(int id)
	{
		try {
			return _cCsv.getPointValue(profile.getString(R.string.PointCvs), id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println(e);
			return null;
		}		
		
	}*/
	public ArrayList<String[]> ImportHistory(String path)
	{
		try {
			return _cCsv.readeCsv(path);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public void Export(Object parse,Object T,String filePath) {
		List<IExportFunction> resuItems=(List<IExportFunction>)parse;		
//		pathString=profile.getString(R.string.DefaultFloderPath)+"/"+strings[6];
//		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-ddhh"); 
//		sDateFormat.format(new java.util.Date())
		String pathString=filePath+((BaseDao)T).FileName()+".csv"; 
		if (resuItems!=null&&resuItems.size()>0) {
			CsvWriter wr =new CsvWriter(pathString,',',Charset.forName("GBK"));
			try {                          
		         wr.writeRecord(resuItems.get(0).getCvsTitle().split(","));
		         for (int i = 0; i < resuItems.size(); i++) {
					IExportFunction item=resuItems.get(i);
					wr.writeRecord(item.getCvsData().split(","));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
		         wr.close();  
				e.printStackTrace();
			}
	         wr.close();  
			
			
		}
		// TODO Auto-generated method stub
		
	}
}
