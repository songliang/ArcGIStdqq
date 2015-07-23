package com.esri.arcgis.android.samples.DataControl;
import java.sql.SQLException;
import android.content.Context;
import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.PointFullItem;
import com.esri.arcgis.android.samples.tableItem.UserInfoItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

public class UserInfo extends BaseDao<UserInfoItem, Integer> {
	public UserInfo(Context context) {  
		  super(context);  
		 }
	@Override  
	 public Dao<UserInfoItem, Integer> getDao() throws SQLException {  
	  return getHelper().getDao(UserInfoItem.class);  
	}
	@Override
	public void ClearTable() throws SQLException {
		// TODO Auto-generated method stub
		TableUtils.clearTable(getHelper().getConnectionSource(), UserInfoItem.class);
	}
	@Override
	public String FileName() {
		// TODO Auto-generated method stub
		return "用户信息表";
	}
}