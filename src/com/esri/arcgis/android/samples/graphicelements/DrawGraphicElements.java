/*
 * Copyright �2010 ESRI
 * All rights reserved under the copyright laws of the United States and applicable international laws, treaties, and conventions.
 * You may freely redistribute and use this sample code, with or without modification, provided you include the original copyright notice and use restrictions.
 * Disclaimer: THE SAMPLE CODE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ESRI OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) SUSTAINED BY YOU OR A THIRD PARTY, HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT ARISING IN ANY WAY OUT OF THE USE OF THIS SAMPLE CODE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts and Legal Services Department
 * 380 New York Street Redlands, California, 92373
 * USA
 * email: contracts@esri.com
 */
package com.esri.arcgis.android.samples.graphicelements;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import textwatcher.ValueNameDomain;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.CalloutPopupWindow;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.DataControl.JieZhiLine;
import com.esri.arcgis.android.samples.DataControl.JieZhiPoint;
import com.esri.arcgis.android.samples.DataControl.LanFull;
import com.esri.arcgis.android.samples.DataControl.LoginUser;
import com.esri.arcgis.android.samples.DataControl.PointFull;
import com.esri.arcgis.android.samples.DataControl.UserInfo;
import com.esri.arcgis.android.samples.graphicelements.DrawTools.Category;
import com.esri.arcgis.android.samples.graphicelements.DrawTools.DrawMode;
import com.esri.arcgis.android.samples.tableItem.IExportFunction;
import com.esri.arcgis.android.samples.tableItem.JieZhiLineItem;
import com.esri.arcgis.android.samples.tableItem.JieZhiPointItem;
import com.esri.arcgis.android.samples.tableItem.LandItem;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;
import com.esri.arcgis.android.samples.tableItem.PointFullItem;
import com.esri.arcgis.android.samples.tableItem.UserInfoItem;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;

import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.runtime.LicenseLevel;
import com.esri.core.runtime.LicenseResult;

/**
 * @author ZHAO
 *
 */
/**
 * @author ZHAO
 *
 */
public class DrawGraphicElements extends Activity {
	boolean[] ClearDataSelect = new boolean[]{false,false,false,false};  
	int ImportChose=0;
	CSV _cCsv;
	Profile profile;
	DataControl _dataControl;
	MapView mapView = null;
	final String[] geometryTypes = new String[] { "地块点绘制", "路径计算",
	"面积测算" };
	/*
	 * Android UI elements
	 */
	Button clearButton = null;
	Button crossButton=null;
	Button zoominButton=null;
	Button zoomoutButton=null;
	Button gPSButton=null;
	Button gCenter=null;
	RadioGroup radioGroup=null;
	DrawTools draw=null;
	View cross1=null;
	View cross2=null;
	BtnGPSListener gPSListerner;
	TextView crosspositionTextView=null;
	CalloutPopupWindow cpw=null;
	View selfeatureView=null;
	/*
	 * 要素选择相关变量
	 */
	private int layerid;
	private int featureid;//元素ID
	private Category category;
	
	@SuppressWarnings("serial")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArcGISRuntime.setClientId("kitY7Fh0BBLxoIe5");
		setContentView(R.layout.main);
		System.out.println("12");//用于测试，防止程序未刷入；
		InitView();//初始化按钮；
		profile=new Profile(this);
		_cCsv=new CSV();
		/*读取地图*/		
		String mappath=profile.getString(R.string.mappath);
		LoadMap(mappath, mapView);
		draw=InitMap(mapView);//初始化地图；获得操作句柄；
		//读取用户列表；
		//String userCvs=profile.getString(R.string.userCvs);
		//System.out.println(userCvs);
		_dataControl=new DataControl(this);	
	    LayoutInflater inflater=LayoutInflater.from(DrawGraphicElements.this);
		selfeatureView=inflater.inflate(R.layout.cpw, null);
		cpw=new CalloutPopupWindow(selfeatureView);
	}	
	private void setCenterLongandLat() {
		SpatialReference gpsReference=SpatialReference.create(SpatialReference.WKID_WGS84);
		Point mapPoint=mapView.toMapPoint(new Point(mapView.getWidth()/2.0f, mapView.getHeight()/2.0f));
		Point longlat=(Point)GeometryEngine.project(mapPoint,mapView.getSpatialReference(), gpsReference);
		DecimalFormat decimalFormat=new DecimalFormat("#.000000");
		String tString=" "+decimalFormat.format(longlat.getX())+"  "+decimalFormat.format(longlat.getY())+" ";
		crosspositionTextView.setText(tString);
	}
	private void setCPWView(){
		TextView lengthTextView=(TextView)selfeatureView.findViewById(R.id.txt_length);
		Button deleteButton=(Button)selfeatureView.findViewById(R.id.btn_delete);
		Button modifyButton=(Button)selfeatureView.findViewById(R.id.btn_modify);
		ImageView quitButton=(ImageView)selfeatureView.findViewById(R.id.img_quit);
		
		lengthTextView.setVisibility(View.GONE);
		modifyButton.setVisibility(View.VISIBLE);
		
		quitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(cpw!=null&&cpw.isShowing()){
					cpw.hide();
			
				}
				draw.storageLayer.clearSelection();
			}
		});
		
		
		deleteButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					try {
						switch (category) {
						case landpoint:							
							DeleElementByID(PointFull.class);							
							break;
						case boundarypoint:
							JieZhiLine iteJieZhiPoint=new JieZhiLine(DrawGraphicElements.this);
							List<JieZhiLineItem> list1=iteJieZhiPoint.queryByIds("startid_id",Integer.toString(featureid));
							if (list1!=null) 
							for (JieZhiLineItem jieZhiLineItem : list1) {
								draw.DeleLine(jieZhiLineItem.getId());
							}
							iteJieZhiPoint.delete(list1);
							List<JieZhiLineItem> list2=iteJieZhiPoint.queryByIds("endid_id",Integer.toString(featureid));
							if (list2!=null) 
							for (JieZhiLineItem jieZhiLineItem : list2) {
								draw.DeleLine(jieZhiLineItem.getId());
							}
							iteJieZhiPoint.delete(list2);
							//draw.clearSelectLine();
							DeleElementByID(JieZhiPoint.class);							
							/*if(iteJieZhiPoint.queryById("startid_id",Integer.toString(featureid))==null&&iteJieZhiPoint.queryById("endid_id",Integer.toString(featureid))==null)
							{
								DeleElementByID(JieZhiPoint.class);
							}
							else {
								ShowMessage("请先删除界址点连线");
								return;
							}*/
							break;
						case boundaryline:
							DeleElementByID(JieZhiLine.class);
							break;
						default:
							DeleElementByID(JieZhiLine.class);
							break;
						}
						deleSelect();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		
		modifyButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), ""+featureid, Toast.LENGTH_LONG).show();
					try {
						switch (category) {
						case landpoint:
							 Intent i = new Intent(DrawGraphicElements.this, NewInfoActivity.class);
							  i.putExtra("id", featureid);
				              startActivityForResult(i, 3); 
							//DeleElementByID(PointFull.class);							
							break;
						case boundarypoint:
							ShowJieZhiDialog("修改", featureid);
							//DeleElementByID(JieZhiPoint.class);
							break;
						case boundaryline:
							DeleElementByID(JieZhiLine.class);
							break;
						default:
							break;
						}
						if(cpw!=null && cpw.isShowing()){
							cpw.hide();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		
		
		String nameString="";
		Drawable drawable=null;
		switch (category) {
			case landpoint:
				nameString="地块点";
				drawable=getResources().getDrawable(R.drawable.landpoint);
				break;
			case boundarypoint:
				nameString="界址点";
				drawable=getResources().getDrawable(R.drawable.boundarypoint);
				break;
			default:
				lengthTextView.setVisibility(View.VISIBLE);
				modifyButton.setVisibility(View.GONE);
				double tlength=((Polyline)draw.storageLayer.getGraphic(layerid).getGeometry()).calculateLength2D();
				lengthTextView.setText(draw.createString(tlength, 0));
				nameString="界址点连线";
				drawable=getResources().getDrawable(R.drawable.boundaryline);
				cpw.refresh();
				break;
		}
		TextView nametTextView=(TextView)selfeatureView.findViewById(R.id.txt_name);
		nametTextView.setText(nameString);
		ImageView flagImageView=(ImageView)selfeatureView.findViewById(R.id.img_flag);
		flagImageView.setImageDrawable(drawable);
		((TextView)selfeatureView.findViewById(R.id.txt_id))
			.setText("ID:"+featureid);
			
	}
	private void deleSelect()
	{
		draw.storageLayer.clearSelection();
		draw.storageLayer.removeGraphic(layerid);
		if(cpw!=null&&cpw.isShowing()){
			cpw.hide();
		}
		draw.DeleHash(featureid, category);
	}
	private void SetFocus(View ele) {
		  ele.setFocusable(true);
		  ele.setFocusableInTouchMode(true);
		  ele.requestFocus();
		  ele.requestFocusFromTouch();		
	}
	private ArrayList<String[]> LoadUser(String path) {
		if(FileUtils.fileIsExists(path))
		{
			try {
				return _cCsv.readeCsv(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ShowMessage("文件读取错误，请重启应用");
				return null;
			}
		}
		else {
			ShowMessage("用户列表不存在，请重新选择");
			return null;
		}
	}
	private int DeleElementByID(Class T) throws Exception
	{	
			Object result=CreatDaoByName(T);
			int value= ((BaseDao)result).deleteById("ID", Integer.toString(featureid));
			ShowMessage("删除成功");
			return value;
	}
	private Object CreatDaoByName(Class T) throws Exception
	{
		Class[] pType = new Class[]{ Context.class};
		Constructor ctor = T.getConstructor(pType);
		Object[] obj = new Object[]{DrawGraphicElements.this };
		return ctor.newInstance(obj);
	}
	private DrawTools InitMap(MapView mapiew) {
		// TODO Auto-generated method stub		
		/**
		 * Add TiledMapServiceLayer and GraphicsLayer to map
		 */	
		GraphicsLayer paintlayer = new GraphicsLayer();
		GraphicsLayer storageLayer=new GraphicsLayer();
		mapiew.addLayer(paintlayer);
		mapiew.addLayer(storageLayer);
		DrawTools drawTools=new DrawTools(DrawGraphicElements.this,mapiew, paintlayer,storageLayer, DrawMode.POINT);       
		drawTools.setfMDistance(true);
		drawTools.setfMArea(true);
		drawTools.setfCross(true);
		drawTools.start(0);
//		mapView.setOnSingleTapListener(new OnSingleTapListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void onSingleTap(float x, float y) {
//				// TODO Auto-generated method stub
////				Toast.makeText(getApplicationContext(), "onsingletap", Toast.LENGTH_LONG).show();
//				if(cpw==null)
//					cpw=new CalloutPopupWindow(getViewfromxml(DrawGraphicElements.this,R.layout.cpw_line));
//				else
//					cpw.showCallout(mapView, mapView.toMapPoint(mapView.getWidth()/2.0f, mapView.getHeight()/2.0f), 0, 0);
//			}
//		});
		drawTools.setItemSelected(new DrawTools.ICoallBack() {

			@Override
			public void onSelecteFeature(int layerid,int featureid, Category category,Point mappt) {
				// TODO Auto-generated method stub
				DrawGraphicElements.this.layerid=layerid;
				DrawGraphicElements.this.featureid=featureid;
				DrawGraphicElements.this.category=category;
				setCPWView();
				cpw.showCallout(mapView, mappt, 0, 0);
			}

			@Override
			public void onCreateFeature(Category category) {
				// TODO Auto-generated method stub
				switch (category) {
				case landpoint:
					  Intent i = new Intent(DrawGraphicElements.this, NewInfoActivity.class);
					  i.putExtra("long", draw.dlng);
					  i.putExtra("lat", draw.dlat);
		              startActivityForResult(i, 3); 
					break;
				case boundarypoint:
					 ShowJieZhiDialog("添加",0);
						/*Intent j = new Intent(DrawGraphicElements.this, NewInfoActivity.class);
						j.putExtra("long", draw.dlng);
						j.putExtra("lat", draw.dlat);
						startActivityForResult(j, 3);*/ 
					break;
				default:
					break;
				}
			}
		});
		return drawTools;
	}
	private void ShowJieZhiDialog(String title,int id) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		 final View layoutDialog = inflater.inflate(R.layout.jiezhipoint,null);
		// TODO Auto-generated method stubs					
		AlertDialog.Builder builder = new Builder(DrawGraphicElements.this);
		EditText textView=(EditText) layoutDialog.findViewById(R.id.JiezhiPoint);
		EditText text=(EditText)layoutDialog.findViewById(R.id.JiezhiOther);
		if (id!=0) {
			try {
				BaseDao itemObject=(BaseDao)CreatDaoByName(JieZhiPoint.class);
				JieZhiPointItem jieZhiPoint=(JieZhiPointItem)itemObject.queryById("ID",Integer.toString(id));
				textView.setText(Integer.toString(jieZhiPoint.getRtkID()));
				textView.setTag(jieZhiPoint);
				text.setText(jieZhiPoint.getOther());
				SetFocus(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			int t=profile.getInt(R.string.rtk);
			textView.setText(Integer.toString(t+1));
			SetFocus(text);
		}
		builder.setView(layoutDialog);
		builder.setTitle(title);
		builder.setNegativeButton("保存",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				EditText textView=(EditText)layoutDialog.findViewById(R.id.JiezhiPoint);
				EditText text=(EditText)layoutDialog.findViewById(R.id.JiezhiOther);
				JieZhiPointItem jie=(JieZhiPointItem) textView.getTag();
				JieZhiPoint jieZhi=new JieZhiPoint(DrawGraphicElements.this);
				if (jie!=null) {
					jie.setOther(textView.getText().toString());
					try {
						jieZhi.update(jie);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						JieZhiPointItem item=jieZhi.saveAndGetValue(new JieZhiPointItem(text.getText().toString(),draw.dlat,draw.dlng,Integer.parseInt(textView.getText().toString())));
						profile.Save(R.string.rtk, Integer.parseInt(textView.getText().toString()));
						draw.saveCurrentFeature(item.getId());
						dialog.dismiss();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						ShowMessage("保存错误");
						dialog.dismiss();					
					}
				}				
			}
		});
		builder.setPositiveButton("取消",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				draw.clear();
			}
		});
		builder.create().show();
	}
	private void LoadMap(String path, MapView mapView2) {
		// TODO Auto-generated method stub
		if (path!=""&&FileUtils.fileIsExists(path)) {		
		ArcGISLocalTiledLayer localTiledLayer=new ArcGISLocalTiledLayer(path);
		//ArcGISDynamicMapServiceLayer WordlocalTiledLayer=new ArcGISDynamicMapServiceLayer("http://services.arcgisonline.com/Arcgis/rest/services/World_Street_Map/MapServer");		
		//mapView2.addLayer(WordlocalTiledLayer);
		//localTiledLayer.setVisible(false);
		mapView2.addLayer(localTiledLayer);
		mapView2.setOnStatusChangedListener(new OnStatusChangedListener(){
			 private static final long serialVersionUID = 1L;
			 
		     public void onStatusChanged(Object source, STATUS status) {
		         if (OnStatusChangedListener.STATUS.INITIALIZED == status && source == mapView) {
		        		double x=mapView.getMinScale();
		    	 		double y=mapView.getMaxScale();
		    	 		x=(x+y)/8;
		    	 		Point centerPoint=mapView.getCenter();
		    	 		mapView.zoomToScale(centerPoint, x);
		    	 		setCenterLongandLat();
		         }
		       }
		
		});
		mapView2.setOnPanListener(new OnPanListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void prePointerUp(float fromx, float fromy, float tox, float toy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void prePointerMove(float fromx, float fromy, float tox, float toy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void postPointerUp(float fromx, float fromy, float tox, float toy) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "postpointmove",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void postPointerMove(float fromx, float fromy, float tox, float toy) {
				// TODO Auto-generated method stub
				setCenterLongandLat();
			}
		});
		
		}		
		else {
			ShowMessage("地图不存在，请重新选择");
		}
	}
	//使用了全局变量；
	protected void InitView()
	{
		mapView = (MapView)findViewById(R.id.map);  
		cross1=(View)findViewById(R.id.cross1);
		cross2=(View)findViewById(R.id.cross2);
		/*
		 * Initialize Android Geometry Button
		 */
		clearButton = (Button) findViewById(R.id.clearbutton);
		clearButton.setEnabled(true);
		clearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				draw.clear();
//				clearButton.setEnabled(false);
			}
		});
		crossButton=(Button)findViewById(R.id.cross);
		crossButton.setTag("y");
		crossButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button button=(Button)v;
				String display=(String)button.getTag();
				if(display.equalsIgnoreCase("y")){
					crossButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_73));
					cross1.setVisibility(View.GONE);
					cross2.setVisibility(View.GONE);
					button.setTag("n");
					draw.setfCross(false);
					draw.seltolerance=8;
					crosspositionTextView.setVisibility(View.GONE);
				}else {
					crossButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_72));
					cross1.setVisibility(View.VISIBLE);
					cross2.setVisibility(View.VISIBLE);
					button.setTag("y");
					draw.setfCross(true);
					draw.seltolerance=8;
					crosspositionTextView.setVisibility(View.VISIBLE);
				}
			}
		});

		zoominButton=(Button)findViewById(R.id.zoomin);
		zoominButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapView.zoomin();
			}
		});
		
		zoomoutButton=(Button)findViewById(R.id.zoomout);
		zoomoutButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapView.zoomout();
			}
		});
		gPSButton=(Button)findViewById(R.id.GPS);
		gPSButton.setTag(0);
		gPSListerner=new BtnGPSListener(DrawGraphicElements.this, this.mapView);
		gPSButton.setOnClickListener(gPSListerner);
		gCenter=(Button)findViewById(R.id.gCenter);
		gCenter.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mapView.zoomout();
				gPSListerner.SetCenter();
			}
		});
		
		crosspositionTextView=(TextView)findViewById(R.id.crosspositon);

		radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if(cpw!=null&& cpw.isShowing()){
					cpw.hide();
				}
				draw.storageLayer.clearSelection();
				draw.clear();
				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.landpointradio:
					draw.setCategory(DrawTools.Category.landpoint);
					draw.setDrawMode(DrawMode.POINT);
					draw.start(0);
					break;
				case R.id.boundarypointradio:
					draw.setCategory(DrawTools.Category.boundarypoint);
					draw.setDrawMode(DrawMode.POINT);
					draw.start(0);
					break;
				case R.id.measurelengthradio:
					draw.setDrawMode(DrawMode.POLYLINE);
					draw.start(0);
					break;
				case R.id.measurearearadio:
					draw.setDrawMode(DrawMode.POLYGON);
					draw.start(0);
					break;
				case R.id.selectradio:
					draw.start(1);
					break;
				default:
					draw.setCategory(DrawTools.Category.boundaryline);
					draw.start(2);
					break;
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mapView.pause();
 }
	@Override 	
	protected void onResume() {
		super.onResume(); 
		mapView.unpause();
	}
	@Override 	
	protected void onDestroy() {
		super.onDestroy(); 
		//mapView.unpause();
		if (gPSListerner!=null&&gPSListerner.locationService!=null) {
			gPSListerner.locationService.stop();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		 // 加载菜单  
        	getMenuInflater().inflate(R.menu.menu, menu);
        	return true;  
	    }
	 //根据菜单执行相应内容  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {    
        case R.id.history:
        	LoadHistory();       	
        	return true;
        case R.id.importhistory:
        	ShowImportChose();        	
        	return true;
        case R.id.dataClear:
        	showClearChose();
        	return true;
        case R.id.exportData:
        	Exportdata();
        	return true;
        }          
        return false;  
    }
    private void ShowImportChose()
	 {
    	String[] itemStrings=new String[]{"界址点连线数据","界址点数据","用户登录用表","地块点数据","用户信息表","承包信息表"};
        new AlertDialog.Builder(DrawGraphicElements.this)
                .setTitle("请点击选择")
                .setItems(itemStrings, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ImportChose=which;
						//showFileChooser(code);
						showFileChooser(GloableFunction.ImPort_Point_CODE);
					}})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 
							dialog.dismiss();
						}})
					.show();
		 
	 }
    private void Exportdata() {
    	String[] itemStrings=new String[]{"界址点连线数据","界址点数据","用户登录用表","地块点数据"};
    	new AlertDialog.Builder(this).setTitle("复选框").setMultiChoiceItems(
    			itemStrings, null, 
    			new DialogInterface.OnMultiChoiceClickListener() {
                    @Override  
                    public void onClick(DialogInterface dialogInterface,   
                            int which, boolean isChecked) {  
                    	ClearDataSelect[which]=isChecked;
                    	//selected[which] = isChecked;  
                    }  
                })
    			.setPositiveButton("确定", 
    				new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int which) {
                    	String filepathString=profile.getString(R.string.DefaultFloderPath)+"/";
                		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd-hh-mm"); 
                		filepathString=filepathString+sDateFormat.format(new java.util.Date());
            			FileUtils.CreatFloder(filepathString);
            			filepathString=filepathString+"/";            			
                    	if (ClearDataSelect[0]) {
                    		ClearDataSelect[0]=false;
                    		ExportTable(JieZhiLine.class,filepathString);
						}
                    	if (ClearDataSelect[1]) {
                    		ClearDataSelect[1]=false;
                    		ExportTable(JieZhiPoint.class,filepathString);
						}
						if (ClearDataSelect[2]) {
							ClearDataSelect[2]=false;							
							ExportTable(LoginUser.class,filepathString);
						}
						if (ClearDataSelect[3]) {
							ClearDataSelect[3]=false;
							ExportTable(PointFull.class,filepathString);							
						}
//						if (ClearDataSelect[4]) {
//							ClearDataSelect[4]=false;
//							ExportTable(UserInfo.class,filepathString);
//						}
//						if (ClearDataSelect[5]) {
//							ClearDataSelect[5]=false;
//							ExportTable(LanFull.class,filepathString);
//						}
						ShowMessage("导出完成！");
                    }  
                })
    			.setNegativeButton("取消", 
    				new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int which) 
                    {
                    	
                    }
                }
    		).show();
	}
	private void showClearChose() {
		// TODO Auto-generated method stub
    	String[] itemStrings=new String[]{"界址点连线数据","界址点数据","用户登录用表","地块点数据"};
    	new AlertDialog.Builder(this).setTitle("复选框").setMultiChoiceItems(
    			itemStrings, null, 
    			new DialogInterface.OnMultiChoiceClickListener() {
                    @Override  
                    public void onClick(DialogInterface dialogInterface,   
                            int which, boolean isChecked) {  
                    	ClearDataSelect[which]=isChecked;
                    	//selected[which] = isChecked;  
                    }  
                })
    			.setPositiveButton("确定", 
    				new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int which) {
                    	if (ClearDataSelect[0]) {
                    		ClearDataSelect[0]=false;
                    		ClearTable(JieZhiLine.class);
                    		draw.RemoveStoryAndHash(Category.boundaryline);
						}
                    	if (ClearDataSelect[1]) {
                    		ClearDataSelect[1]=false;
                    		ClearDataSelect[0]=false;
                    		ClearTable(JieZhiLine.class);
                    		draw.RemoveStoryAndHash(Category.boundaryline);
                    		ClearTable(JieZhiPoint.class);
                    		draw.RemoveStoryAndHash(Category.boundarypoint);
						}
						if (ClearDataSelect[2]) {
							ClearDataSelect[2]=false;
							ClearTable(LoginUser.class);
						}
						if (ClearDataSelect[3]) {
							ClearDataSelect[3]=false;
							ClearTable(PointFull.class);//	
							draw.RemoveStoryAndHash(category.landpoint);
						}
//						if (ClearDataSelect[4]) {
//							ClearDataSelect[4]=false;
//							ClearTable(UserInfo.class);
//							GloableFunction.UserList.clear();
//						}
//						if (ClearDataSelect[5]) {
//							ClearDataSelect[5]=false;
//							ClearTable(LanFull.class);
//							//GloableFunction.UserList.clear();
//						}
						ShowMessage("删除成功！");
                    }  
                })
    			.setNegativeButton("取消", 
    				new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }  
                }
    		).show();
	}
	private void ExportTable(Class T,String filepathString)
	{
		try {
			Object result = CreatDaoByName(T);
			_dataControl.Export(((BaseDao)result).queryAll(),result,filepathString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void ClearTable(Class T)
	{
		try {
			Object result = CreatDaoByName(T);
			((BaseDao)result).ClearTable();
		}
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	private void LoadHistory() {
			try {
				PointFull full=new PointFull(this);
				List<PointFullItem> list=full.queryAll();
				for (PointFullItem pointFullItem : list) {
					try {
					draw.DrawFormWG84(pointFullItem.getId(),pointFullItem.getLng(),pointFullItem.getLat(),Category.landpoint);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				JieZhiPoint jieZhiPoint=new JieZhiPoint(this);
				List<JieZhiPointItem> jiesFullItems=jieZhiPoint.queryAll();
				
				for (JieZhiPointItem item : jiesFullItems) {
					try {
					draw.DrawFormWG84(item.getId(),item.getLng(),item.getLat(),Category.boundarypoint);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				JieZhiLine jieZhiLine=new JieZhiLine(this);
				
				List<JieZhiLineItem> jiesLineItems=jieZhiLine.queryAll();
				for (JieZhiLineItem item : jiesLineItems) {
					try {
					JieZhiPointItem startJieZhiPoint=item.getStartid();
					JieZhiPointItem end=item.getEndid();
					draw.DrawLineFromWG84(item.getId(), startJieZhiPoint.getLng(),startJieZhiPoint.getLat(),end.getLng(),end.getLat() );
					} catch (Exception e) {
						// TODO: handle exception
					}
				//draw.DrawFormWG84(item.getId(),item.getLng(),item.getLat(),Category.boundarypoint);	
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ShowMessage("加载成功.如需重载，请退出重新登录");
	}
	public void ShowMessage(String message)
    {
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /*文件选择器*/
    private void showFileChooser(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("*/*");        
        intent.addCategory(Intent.CATEGORY_OPENABLE);     
        try {
            startActivityForResult( Intent.createChooser(intent, "选择文件"), code);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装文件管理工具.",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
    	if (requestCode==3) {
    		if (resultCode != RESULT_OK) {
    			draw.clear();
    			return;
			}
        	//填写个人信息完成；
                //取出字符串  
                Bundle bundle = data.getExtras();  
                int str = bundle.getInt("id");  
                ShowMessage(Integer.toString(str));
    			draw.saveCurrentFeature(str);
                return;			
    		}
    	if (resultCode != RESULT_OK)
    		{
    			ShowMessage("选择错误，请重新选择");
    			return;
    		}    	
    	 // Get the Uri of the selected file 
        Uri uri = data.getData();
        String path = FileUtils.getPath(this, uri);
        switch (requestCode) {            
            case 2:
            	profile.Save(R.string.PointCvs,path);
            	ImportData();            	
            	break;
        }
    //super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**选择导入方式；
     * 
     */
    private void ImportData() {
		// TODO Auto-generated method stubs	    	
		AlertDialog.Builder builder = new Builder(DrawGraphicElements.this);
		builder.setMessage("清空后导入还是增加？"); 
		builder.setTitle("提示");
		builder.setNegativeButton("覆盖导入",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				InsertOrAdd(true);
				dialog.dismiss();				
			}
		});
		builder.setNeutralButton("增加导入",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {			
				InsertOrAdd(false);
				dialog.dismiss();				
			}
		});
		builder.setPositiveButton("取消",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		});
		builder.create().show();
	}
	
	/**
	 * @param is 为真，是用自己的ID；为假，数据库自动增加ID；
	 * @param choseimte 
	 * @param userList2
	 */
	private void  InsertOrAdd(boolean is) {
		ArrayList<String[]> pointList=_dataControl.ImportHistory(profile.getString(R.string.PointCvs));
		BaseDao baseDao=null;
		if (ImportChose>5) return;
		switch (ImportChose) {
		//String[] itemStrings=new String[]{"界址点连线","界址点","用户登录用表","勘测元素点","用户信息表"};
		case 0:
			baseDao=new JieZhiLine(DrawGraphicElements.this);
			break;
		case 1:
			baseDao=new JieZhiPoint(DrawGraphicElements.this);
			break;
		case 2:
			baseDao=new LoginUser(DrawGraphicElements.this);
			break;
		case 3:
			baseDao=new PointFull(DrawGraphicElements.this);
			break;
		case 4:
			baseDao=new UserInfo(DrawGraphicElements.this);
			break;
		case 5:
			baseDao=new LanFull(DrawGraphicElements.this);
			break;
		}
		//PointFull pointFull=new PointFull(this);
    	for (int i = 0; i < pointList.size(); i++) {        		
    		String[] point=pointList.get(i);    
    		//String[] itemStrings=new String[]{"界址点连线","界址点","用户登录用表","勘测元素点","用户信息表"};
    		Object item2=null; 
    		
    		switch (ImportChose) {
    		case 0:
    			baseDao=new JieZhiLine(DrawGraphicElements.this);
    			item2=new JieZhiLineItem(Integer.parseInt(point[0]),new JieZhiPointItem(Integer.parseInt(point[1])),new JieZhiPointItem(Integer.parseInt(point[2])));
    			break;
    		case 1:
    			item2=new JieZhiPointItem(Integer.parseInt(point[0]), point[1], Double.parseDouble(point[2]), Double.parseDouble(point[3]),Integer.parseInt(point[4]));
    			break;
    		case 2:
    			item2=new LogUserItem(Integer.parseInt(point[0]), point[1], point[2]);
    			break;
    		case 3:
    			item2=new PointFullItem(Integer.parseInt(point[0]), point[1], point[2], point[3],
        				point[4], point[5], point[6],Double.parseDouble(point[7]), Double.parseDouble(point[8]),
        				point[9]);
    			break;
    		case 4:
    			//int iD, String name, String iDNumber
    			item2=new UserInfoItem(Integer.parseInt(point[0]), point[1], point[2]);
    			if (GloableFunction.UserList==null) 
					GloableFunction.UserList=new ArrayList<ValueNameDomain>();
					GloableFunction.UserList.add(new ValueNameDomain(point[1],point[2]));
    			break;
    		case 5:
    			item2=new LandItem(Integer.parseInt(point[0]), point[1], point[2], point[3],point[4], point[5],point[6]);
    			break;
			}    		
    		try {
    		if (!is) {
    			((IExportFunction)item2).setId(0);
    			baseDao.SaveOrUpdate(item2);
			}  
    		else baseDao.save(item2); 		
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				ShowMessage("导入失败");
				return;
			}            		
    		ShowMessage("导入完成");
    		//draw.DrawFormWG84(item2.getId(),item2.getLng(),item2.getLat(),Category.landpoint);
    		//draw.DrawFormWG84(Integer.parseInt(point[0]),Double.parseDouble(point[7]),Double.parseDouble(point[6]));
		}   	
	}
   
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog();
                    return false;
            }
            return false;
    }
    protected void dialog() {

            AlertDialog.Builder builder = new AlertDialog.Builder(DrawGraphicElements.this);
            builder.setMessage("确定退出吗？");
            builder.setTitle("退出提示");
            builder.setPositiveButton("确定",
                            new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                    }
                            });
            builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                    }
                            });
            builder.create().show();
    }
}