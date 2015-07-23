package com.esri.arcgis.android.samples.DataControl;

import java.sql.SQLException;

import android.content.Context;

import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
public class LoginUser extends BaseDao<LogUserItem, Integer> {
 public LoginUser(Context context) {  
	  super(context);  
	 }
 	@Override  
	 public Dao<LogUserItem, Integer> getDao() throws SQLException {  
	  return getHelper().getDao(LogUserItem.class);  
	}
	@Override
	public void ClearTable() throws SQLException {
		TableUtils.clearTable(getHelper().getConnectionSource(), LogUserItem.class);
		// TODO Auto-generated method stub
		
	}
	@Override
	public String FileName() {
		// TODO Auto-generated method stub
		return "用户登录信息表";
	}
}

