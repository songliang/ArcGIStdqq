package com.esri.arcgis.android.samples.BasControl;

import com.esri.arcgis.android.samples.DataControl.JieZhiLine;
import com.esri.arcgis.android.samples.tableItem.JieZhiLineItem;
import com.esri.arcgis.android.samples.tableItem.JieZhiPointItem;
import com.esri.arcgis.android.samples.tableItem.LandItem;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;
import com.esri.arcgis.android.samples.tableItem.PointFullItem;
import com.esri.arcgis.android.samples.tableItem.UserInfoItem;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;  
import java.util.ArrayList;
import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;  
import android.util.Log;   
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper; 
import com.j256.ormlite.table.TableUtils; 
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {  
	public ArrayList<Class> tableList=new ArrayList<Class>();
    private static final String DATABASE_NAME = "ArcGisT.db";  
    private static final int DATABASE_VERSION = 1;      
    public DatabaseHelper(Context context){  
        //tableList=list;
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	InitTables();
    }      
    private void InitTables() {
		// TODO Auto-generated method stub
    	tableList.add(PointFullItem.class);
    	tableList.add(UserInfoItem.class);
    	tableList.add(JieZhiPointItem.class);
    	tableList.add(JieZhiLineItem.class);
    	tableList.add(LogUserItem.class);
    	tableList.add(LandItem.class);
	}
	/**  
     * 创建SQLite数据库  
     */  
    @Override  
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {  
        try { 
        	for (int i = 0; i < tableList.size(); i++) {
				TableUtils.createTable(connectionSource, tableList.get(i));
			}
            //TableUtils.createTable(connectionSource, PointFull.class);  
        } catch (SQLException e) {  
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);  
        }  
    }
    /**  
     * 更新SQLite数据库  
     */  
    @Override  
    public void onUpgrade(  
            SQLiteDatabase sqliteDatabase,   
            ConnectionSource connectionSource,   
            int oldVer,  
            int newVer) {  
        try {
        	for (int i = 0; i < tableList.size(); i++) {
				TableUtils.dropTable(connectionSource, tableList.get(i),true);
			}
           // TableUtils.dropTable(connectionSource, PointFullItem.class, true);
            
            onCreate(sqliteDatabase, connectionSource);  
        } catch (SQLException e) {  
            Log.e(DatabaseHelper.class.getName(),   
                    "Unable to upgrade database from version " + oldVer + " to new "  
                    + newVer, e);  
        }  
    }
    /** 
     * Close the database connections and clear any cached DAOs. 
     */  
    @Override  
    public void close() {  
        super.close();  
    }  
}  
