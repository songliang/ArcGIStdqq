package com.esri.arcgis.android.samples.DataControl;

import java.sql.SQLException;

import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.JieZhiLineItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import android.content.Context;

public class JieZhiLine extends BaseDao<JieZhiLineItem, Integer> {
	public JieZhiLine(Context context) {  
		  super(context);  
		 }
	 	@Override  
		 public Dao<JieZhiLineItem, Integer> getDao() throws SQLException {  
		  return getHelper().getDao(JieZhiLineItem.class);  
		}
		@Override
		public void ClearTable() throws SQLException {
			TableUtils.clearTable(getHelper().getConnectionSource(), JieZhiLineItem.class);
			// TODO Auto-generated method stub
		}
		@Override
		public String FileName() {
			// TODO Auto-generated method stub
			return "界址点连线表";
		}
}
