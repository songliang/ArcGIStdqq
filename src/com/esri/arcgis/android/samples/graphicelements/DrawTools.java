package com.esri.arcgis.android.samples.graphicelements;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.CalloutPopupWindow;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.arcgis.android.samples.DataControl.JieZhiLine;
import com.esri.arcgis.android.samples.DataControl.JieZhiPoint;
import com.esri.arcgis.android.samples.tableItem.JieZhiLineItem;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;

public class DrawTools {
	public enum DrawMode{
		PICTURE,POINT,POLYLINE,POLYGON
		}
	public enum Category{//地块属性点、界址点，界址点连线
		landpoint,boundarypoint,boundaryline
	}
	
	private ArrayList<Integer> jiezhis=new ArrayList<Integer>();
	private ArrayList<Integer> jiezhiLines=new ArrayList<Integer>();
	private ArrayList<Integer> pointfull=new ArrayList<Integer>();
	private ArrayList<Integer> SelectIDS=new ArrayList<Integer>();
	private HashMap<Integer, Integer> selectViewHashMap=new HashMap<Integer, Integer>();
	public double dlng;
	public double dlat;
	private MapView mapview;
	private GraphicsLayer paintlayer;
	public GraphicsLayer storageLayer;
	private DrawMode mode;
	private Category category=Category.landpoint;
	private Geometry geometry;
	private ArrayList<Point> pt_list;
	public boolean isCleared=true;
	private ArrayList<Double> distance_list;
	private Context context;
	//符号样式
	private SimpleMarkerSymbol sMarkerSymbol1;
	private SimpleMarkerSymbol sMarkerSymbol2;
	private TextSymbol textSymbol;
	private PictureMarkerSymbol pMarkerSymbol;
	private SimpleLineSymbol sLineSymbol;
	private SimpleFillSymbol sFillSymbol;
	//Graphics在paintlayer中的索引
	private int idofpoint=-1;
	private int idofpolyline=-1;
	private int idofpolygon=-1;
	private int idofarea=-1;
	//标志
	private Boolean fCross=false;
	private Boolean fMDistance=false;
	private boolean fMArea=false;
    //singletap监听器
	DrawSingleTapListener drawsingleTapListener;
	selectSingleTapListener selectSingleTapListener;
	lineSingleTapListener lineSingleTapListener;
	//连线相关变量
	private Boolean flags1=false;
	private Boolean flags2=false;
	private int [] lineids;//线两段点的id
	private Geometry [] linegs;//line geometry
	private int [] selectedlayerids;//线两段点在storagerlayer中的id
	public CalloutPopupWindow cpw=null;
	private View lineView;
	//
	public int seltolerance=2;
	
	public DrawTools(Context context,MapView mapview,GraphicsLayer paintlayer,GraphicsLayer storagelayer,DrawMode mode) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.mapview=mapview;
		this.paintlayer=paintlayer;
		this.storageLayer=storagelayer;
	    pt_list=new ArrayList<Point>();
	    distance_list=new ArrayList<Double>();
	    setDrawMode(mode);
	    
	    sMarkerSymbol1=new SimpleMarkerSymbol(Color.BLUE,10,STYLE.CIRCLE);
	    sMarkerSymbol2=new SimpleMarkerSymbol(Color.RED,12,STYLE.DIAMOND);
	    textSymbol=new TextSymbol((int) 20.0f,"",Color.WHITE);
	    textSymbol.setHorizontalAlignment(HorizontalAlignment.CENTER);
	    sLineSymbol=new SimpleLineSymbol(Color.BLUE,4);
	    
	    sFillSymbol=new SimpleFillSymbol(Color.MAGENTA);
	    sFillSymbol.setAlpha(100);
	    sFillSymbol.setOutline(new SimpleLineSymbol(Color.BLUE,2));
	    
	    drawsingleTapListener=new DrawSingleTapListener();
	    selectSingleTapListener=new selectSingleTapListener();
	    lineSingleTapListener=new lineSingleTapListener();
	    
	    lineids=new int []{-1,-1};
	    selectedlayerids=new int []{-1,-1};
	    linegs=new Geometry []{null,null};
	    LayoutInflater inflater=LayoutInflater.from(context);
		lineView=inflater.inflate(R.layout.cpw_line, null);
	    cpw=new CalloutPopupWindow(lineView);
	}
	 /** 
     * 一定一个接口 
     */  
    public interface ICoallBack{  
        public void onSelecteFeature(int layerid,int featureid,Category category,Point mappt);
        public void onCreateFeature(Category category);
    }  
      
    /** 
     * 初始化接口变量 
     */  
    ICoallBack icallBack = null;  
      
    /** 
     * 自定义控件的自定义事件 
     * @param iBack 接口类型 
     */  
    public void setItemSelected(ICoallBack iBack)  
    {  
        icallBack = iBack;  
    }  
	public void setDrawMode(DrawMode mode) {
		this.mode=mode;
		clear();
	}
	public DrawMode getDrawMode() {
		return this.mode;
	}
	public void setPaintLayer(GraphicsLayer paintlayer) {
		this.paintlayer=paintlayer;
	}
	public void setfCross(Boolean fcross){
		this.fCross=fcross;
	}
	public void setfMDistance(Boolean fMDistance){
		this.fMDistance=fMDistance;
	}
	public void setfMArea(Boolean fMArea) {
		this.fMArea=fMArea;
	}
	public void setCategory(Category category){
		this.category=category ;
	}
	private Geometry creatGeometry(){
		if(mode==DrawMode.POINT){
			return new Point();
		}else if(mode==DrawMode.POLYLINE){
			return new Polyline();
		}else {
			return new Polygon();
		}
	}

	public void clear(){
		try {
		isCleared=true;
		paintlayer.removeAll();
		pt_list.clear();
		distance_list.clear();
		geometry=null;
		geometry=creatGeometry();
		idofpoint=-1;
		idofpolyline=-1;
		idofpolygon=-1;
		idofarea=-1;
		//连线相关
		for(int i=0;i<2;i++){
			linegs[i]=null;
			lineids[i]=-1;
			selectedlayerids[i]=-1;
		}
		flags1=false;
		flags2=false;
		if(cpw!=null&&cpw.isShowing()){
			cpw.hide();
		}
		storageLayer.clearSelection();
		((Button)lineView.findViewById(R.id.button1))
			.setText("选择");
		((Button)lineView.findViewById(R.id.button2))
		.setText("选择");
		
	} catch (Exception e) {
			 System.err.println(e);
				// TODO: handle exception
			}
	}
	/**
	 * x:0 for length,1 for area
	 */
	public String createString(double value,int x) {		
		if(value<0) value=-value;
		if(x==0){
			if(value<100){
				return String.format("%sm",new DecimalFormat("0.000").format(value));
			}else {
				return String.format("%skm",new DecimalFormat("0.000").format(value/1000.0));
			}
		}else {
			if(value>=100000){
				return String.format("%skm2",new DecimalFormat("0.000").format(value/1000000.0));
			}else {
				return String.format("%sm2",new DecimalFormat("0.000").format(value));
			}
		}		
	}
	public void saveCurrentFeature(int id) {
		if(idofpoint!=-1){
			drawToStorage(id,geometry,category);
		}
		clear();	
	}
	public void DrawFormWG84(int id,double dLong,double dLat,Category category)
	{
		Point ptMap = (Point)GeometryEngine.project(dLong, dLat, mapview.getSpatialReference());
		drawToStorage(id,ptMap,category);
	}
	public void DrawLineFromWG84(int id,Point qidian ,Point zhongdian){
		Point qidianMap = (Point)GeometryEngine.project(qidian.getX(),qidian.getY(), mapview.getSpatialReference());
		Point zhongdianMap = (Point)GeometryEngine.project(zhongdian.getX(),zhongdian.getY(), mapview.getSpatialReference());
        Polyline pl=new Polyline();
        pl.startPath(qidianMap);
        pl.lineTo(zhongdianMap);
        drawToStorage(id, pl, Category.boundaryline);
	}
	public void DrawLineFromWG84(int id,double l,Double t,double l1,double t1){
		Point qidianMap = (Point)GeometryEngine.project(l, t, mapview.getSpatialReference());
		Point zhongdianMap = (Point)GeometryEngine.project(l1,t1, mapview.getSpatialReference());
		Polyline pl=new Polyline();
        pl.startPath(qidianMap);
        pl.lineTo(zhongdianMap);
        drawToStorage(id, pl, Category.boundaryline);
	}
	public void DrawFormWG84(int id,double dLong,double dLat)
	{
		Point ptMap = (Point)GeometryEngine.project(dLong, dLat, mapview.getSpatialReference());
//		drawToStorage(id,ptMap,category);
	}
	public void drawToStorage(int id,Geometry p,Category category){
		Map<String, Object> attr=new HashMap<String, Object>();
		attr.put("pointID", id);
		attr.put("category", category);
		Graphic gh;
		switch (category) {
		case landpoint:
			gh=new Graphic(p,sMarkerSymbol1,attr);			
			break;
		case boundarypoint:
			gh=new Graphic(p,sMarkerSymbol2,attr);
			break;
		default:
			gh=new Graphic(p,sLineSymbol,attr);
			break;
		}
		int id1=storageLayer.addGraphic(gh);
		switch (category) {
		case landpoint:
			pointfull.add(id1);
			break;
		case boundarypoint:			
			jiezhis.add(id1);
			selectViewHashMap.put(id, id1);
			break;
		default:
			jiezhiLines.add(id1);
			break;
		}
	}
	
	public void start(int arg) {
		mapview.setOnSingleTapListener(null);
		switch (arg) {
		case 0:
			mapview.setOnSingleTapListener(drawsingleTapListener);
			break;
		case 1:
			mapview.setOnSingleTapListener(selectSingleTapListener);
			break;
		default:
			mapview.setOnSingleTapListener(lineSingleTapListener);
			break;
		}
	}
	public void stop(){
		mapview.setOnSingleTapListener(null);
	}
	private void setCPWlineView(Boolean istwo) {
		
			LinearLayout lay11=(LinearLayout)lineView.findViewById(R.id.llayout11);
			final LinearLayout lay22=(LinearLayout)lineView.findViewById(R.id.llayout22);
			lay22.setVisibility(View.GONE);
			final LinearLayout lay33=(LinearLayout)lineView.findViewById(R.id.llayout33);
			lay33.setVisibility(View.GONE);
			
			final TextView qidiantxt=(TextView)lineView.findViewById(R.id.txt_linename1);
			qidiantxt.setText("起点界址点："+lineids[0]);
			TextView zhongdiantxt=(TextView)lineView.findViewById(R.id.txt_linename2);
			zhongdiantxt.setText("终点界址点："+lineids[1]);
			
			Button qidianButton=(Button)lineView.findViewById(R.id.button1);
			qidianButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Button btn=(Button)arg0;
					if(!flags1){
						btn.setText("清除");
						cpw.hide();
						flags1=true;
					}else {
						if(flags2){
							((Button)lineView.findViewById(R.id.button2))
									.setText("选择");
							flags2=false;
							lineids[0]=lineids[1];
							linegs[0]=linegs[1];
							selectedlayerids[0]=selectedlayerids[1];
							selectedlayerids[1]=-1;
							qidiantxt.setText("起点界址点："+lineids[0]);
							lay22.setVisibility(View.GONE);
							lay33.setVisibility(View.GONE);
						}else {
							if(((LinearLayout)lineView.findViewById(R.id.llayout22)).getVisibility()==View.VISIBLE){
								flags2=false;
								lineids[0]=lineids[1];
								linegs[0]=linegs[1];
								selectedlayerids[0]=selectedlayerids[1];
								selectedlayerids[1]=-1;
								qidiantxt.setText("起点界址点："+lineids[0]);
								lay22.setVisibility(View.GONE);
							}
							btn.setText("选择");
							flags1=false;							
						}	
						setLineSelected();
					}
					cpw.refresh();
				}
			});
			Button zhongdianButton=(Button)lineView.findViewById(R.id.button2);
			zhongdianButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Button btn=(Button)arg0;
					if(!flags2){
						btn.setText("清除");
						flags2=true;
						lay33.setVisibility(View.VISIBLE);
					}else {
						btn.setText("选择");
						flags2=false;
						lay33.setVisibility(View.GONE);
					}
					cpw.refresh();
				}
			});
			
			((ImageView)lineView.findViewById(R.id.img_linequit))
				.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(cpw!=null&&cpw.isShowing()){
							cpw.hide();
						}
						storageLayer.clearSelection();
						flags1=false;
						flags2=false;
						storageLayer.clearSelection();
						selectedlayerids[0]=-1;
						selectedlayerids[1]=-1;
						((Button)lineView.findViewById(R.id.button2))
							.setText("选择");
						((Button)lineView.findViewById(R.id.button1))
							.setText("选择");
					}
				});
			
			TextView distTextView=(TextView)lineView.findViewById(R.id.txt_linedist);
			distTextView.setVisibility(View.GONE);
			
			Button measureButton=(Button)lineView.findViewById(R.id.btn_linemeasure);
			measureButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Polyline pl=new Polyline();
					pl.startPath((Point)linegs[0]);
					pl.lineTo((Point)linegs[1]);
					((TextView)lineView.findViewById(R.id.txt_linedist))
						.setVisibility(View.VISIBLE);
					((TextView)lineView.findViewById(R.id.txt_linedist))
						 .setText(createString(pl.calculateLength2D(), 0));
					cpw.refresh();
				}
			});
			Button lineButton=(Button)lineView.findViewById(R.id.btn_line);
			lineButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Polyline pl=new Polyline();
					pl.startPath((Point)linegs[0]);
					pl.lineTo((Point)linegs[1]);
					JieZhiLine lineitem=new JieZhiLine(context);
					JieZhiPoint jiezhis=new JieZhiPoint(context);
					JieZhiLineItem valueItem=null;
					int result=0;
					try {
						valueItem=new JieZhiLineItem(jiezhis.queryById("ID",Integer.toString(lineids[0])),jiezhis.queryById("ID",Integer.toString(lineids[1])));
						result=lineitem.save(valueItem);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.err.println(e);
					}
					Map<String, Object> attr=new HashMap<String, Object>();
//					attr.put("pointID",valueItem.getId());
					if (valueItem!=null) {
						attr.put("pointID",valueItem.getId());
						attr.put("category", Category.boundaryline);
						Graphic tmpGraphic=new Graphic(pl, sLineSymbol,attr);						
						int id=storageLayer.addGraphic(tmpGraphic);
						jiezhiLines.add(id);
					}	
					//cpw.hide();
					((ImageView)lineView.findViewById(R.id.img_linequit)).performClick();
				}
			});
			if(istwo){
				lay22.setVisibility(View.VISIBLE);
				lay33.setVisibility(View.GONE);
			}else {
				lay22.setVisibility(View.GONE);
				lay33.setVisibility(View.GONE);
			}		
	}
	public void DeleHash(Integer id,Category type)
	{
		switch (type) {
		case boundaryline:
			jiezhiLines.remove(id);			
			break;			
		case boundarypoint:
			pointfull.remove(id);
		break;
		case landpoint:
			jiezhis.remove(id);
			break;
		}
	}	
	public void RemoveStoryAndHash(Category type)
	{
		//Integer[] tmpHashMap = null;
		switch (type) {
		case boundaryline:
			for (Integer itmeInteger : jiezhiLines) {
				storageLayer.removeGraphic(itmeInteger.intValue());
			}
			jiezhiLines.clear();
			break;			
		case boundarypoint:
			for (Integer itmeInteger : jiezhiLines) {
				storageLayer.removeGraphic(itmeInteger.intValue());
			}
			jiezhiLines.clear();
			for (Integer itmeInteger : jiezhis) {
				storageLayer.removeGraphic(itmeInteger.intValue());
			}
			jiezhis.clear();	
			
		break;
		case landpoint:
			for (Integer itmeInteger : pointfull) {
				storageLayer.removeGraphic(itmeInteger.intValue());
			}
			pointfull.clear();
			break;
		}
	}
	private Category getEnumfromString(String tString) {
		Category category;
		if(tString.contains("landpoint"))category=Category.landpoint;
		else if(tString.contains("boundarypoint"))category=Category.boundarypoint;
		else category=Category.boundaryline;
	    return category;
	}
	private void setLineSelected() {
		storageLayer.clearSelection();
		int tempcount=0;
		int [] tempids=new int [selectedlayerids.length];
		for (int i = 0; i < selectedlayerids.length; i++) {
			if(selectedlayerids[i]!=-1){
				tempcount++;
				tempids[i]=selectedlayerids[i];
			}
		}
		int [] selids=new int [tempcount];
		for (int i = 0; i < selids.length; i++) {
			selids[i]=selectedlayerids[i];
		}
		storageLayer.setSelectedGraphics(selids, true);
	}
	private Point calPolygonCenter(ArrayList<Point> list) throws Exception {
		 if (1 == list.size())  
		    {  
		        return list.get(0);  
		    }   
		    else if (2 == list.size())  
		    {  
		        return new Point((list.get(0).getX() + list.get(1).getX()) / 2, (list.get(0).getY() + list.get(1).getY()) / 2);  
		    }  
		    else if (list.size() >= 3)  
		    {  
		        ArrayList<Point> newList = new ArrayList<Point>();  
		        for (int i = 1; i < list.size() - 1;++i )  
		        {  
		            newList.add(calTriangleCenter(list.get(0), list.get(i), list.get(i+1)));  
		        }  
		        return calPolygonCenter(newList);  
		    }   
		    else  
		    {  
		        throw new Exception("点的集合为空！");  
		    } 
	}
	private Point calTriangleCenter(Point p0,Point p1,Point p2) {
		double x=p0.getX()+p1.getX()+p2.getX();
		double y=p0.getY()+p1.getY()+p2.getY();
		return new Point(x/3,y/3);
	}
	
	private class DrawSingleTapListener implements OnSingleTapListener{
		private static final long serialVersionUID=1L;
		
		@Override
		public void onSingleTap(float x, float y){
			try {
			//生成点
			Point screenp;
			Point temPoint;
			if(fCross){
				screenp=new Point(mapview.getWidth()/2.0f, mapview.getHeight()/2.0f);
			}else{
				screenp=new Point(x, y);
			}
			temPoint=mapview.toMapPoint(screenp);

			isCleared=false;
			//绘制点
			if(mode==DrawMode.POINT){				
				((Point)geometry).setX(temPoint.getX());
				((Point)geometry).setY(temPoint.getY());
				SimpleMarkerSymbol tempmark;
				switch (category) {
				case landpoint:
					tempmark=sMarkerSymbol1;
					break;
				default:
					tempmark=sMarkerSymbol2;
					break;
				}
				if(idofpoint==-1){
					Graphic gh=new Graphic(geometry,tempmark);
					idofpoint= paintlayer.addGraphic(gh);
					pt_list.add(temPoint);
				}else {
					pt_list.set(0, temPoint);
					paintlayer.updateGraphic(idofpoint, geometry);
				}
				SpatialReference tmpReference = SpatialReference.create(SpatialReference.WKID_WGS84);		
				Point ptMasp = (Point)GeometryEngine.project(geometry, mapview.getSpatialReference(), tmpReference);
				dlat=ptMasp.getY();
				dlng=ptMasp.getX();
				icallBack.onCreateFeature(category);
				
			}
			//绘制线段
			if(mode==DrawMode.POLYLINE){
				paintlayer.addGraphic(new Graphic(temPoint, sMarkerSymbol1));
				if(idofpolyline==-1){
					((Polyline)geometry).startPath(temPoint);
					Graphic gh=new Graphic(geometry, sLineSymbol);
					idofpolyline=paintlayer.addGraphic(gh);
					distance_list.add(0.0);

				}else{
					((Polyline)geometry).lineTo(temPoint);
					double dist=((Polyline)geometry).calculateLength2D();
					distance_list.add(dist);
					if(fMDistance){
						textSymbol.setText(createString(dist,0));
						paintlayer.addGraphic(new Graphic(temPoint, textSymbol));
					}
				}
				pt_list.add(temPoint);
				paintlayer.updateGraphic(idofpolyline, geometry);
			}
			//绘制多边形
			if(mode==DrawMode.POLYGON){
				paintlayer.addGraphic(new Graphic(temPoint, sMarkerSymbol1));
				pt_list.add(temPoint);
				if(idofpolygon==-1){
					((Polygon)geometry).startPath(temPoint);
					Graphic gh=new Graphic(geometry, sFillSymbol);
					idofpolygon=paintlayer.addGraphic(gh);
					textSymbol.setText("");
					idofarea=paintlayer.addGraphic(new Graphic(temPoint, textSymbol));
				}else{
					((Polygon)geometry).lineTo(temPoint);
					if(fMArea){
						paintlayer.removeGraphic(idofarea);
						double area=((Polygon)geometry).calculateArea2D();
						textSymbol.setText(createString(area,1));
						idofarea=paintlayer.addGraphic(new Graphic(calPolygonCenter(pt_list), textSymbol));
					}
				}
				
				paintlayer.updateGraphic(idofpolygon, geometry);
			}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(e);
			}
			
		}
	}
	private class selectSingleTapListener implements OnSingleTapListener{
		private static final long serialVersionUID=1L;
		
		@Override
		public void onSingleTap(float x, float y){
			//生成点
			Point screenp;
			Point temPoint;
			if(fCross){
				screenp=new Point(mapview.getWidth()/2.0f, mapview.getHeight()/2.0f);
			}else{
				screenp=new Point(x, y);
			}
			temPoint=mapview.toMapPoint(screenp);
			//选取判断
			int[] IDs=new int[]{};
			SelectIDS.clear();
			IDs=storageLayer.getGraphicIDs((float)screenp.getX(), (float)screenp.getY(),seltolerance);
//				Toast.makeText(mapview.getContext(),">0",Toast.LENGTH_LONG).show();
			    int selectlandpoint=-1;
			    int selectboundarypoint=-1;
			    int selected=-1;
				for(int i=0;i<IDs.length;i++){
					Map<String, Object> attr=storageLayer.getGraphic(IDs[i]).getAttributes();
					if(attr!=null){
						String tString=(String)attr.get("category");
						Category category=getEnumfromString(tString);				
							switch (category) {
							case landpoint:
								selectlandpoint=i;
								break;
							case boundarypoint:
								selectboundarypoint=i;
								break;
							case boundaryline:
								SelectIDS.add(IDs[i]);
							default:
								break;
							} 
					}
				}
				
				if(selectlandpoint!=-1){
					selected=selectlandpoint;
					temPoint=(Point)storageLayer.getGraphic(IDs[selected]).getGeometry();
				}else if(selectboundarypoint!=-1){
					selected=selectboundarypoint;
					temPoint=(Point)storageLayer.getGraphic(IDs[selected]).getGeometry();
				}else selected=IDs.length-1;
	            
				if(selected>=0){
					storageLayer.clearSelection();
					Map<String, Object> attrr=storageLayer.getGraphic(IDs[selected]).getAttributes();
					storageLayer.setSelectedGraphics(new int []{IDs[selected]}, true);
					Integer integerid=(Integer)attrr.get("pointID");
					String ctstring=(String)attrr.get("category");
					Category ct=getEnumfromString(ctstring);
					icallBack.onSelecteFeature(IDs[selected], integerid, ct,temPoint);					
				}
				return ;
		}
	}
	private class lineSingleTapListener implements OnSingleTapListener{
		private static final long serialVersionUID=1L;
		@Override
		public void onSingleTap(float x, float y){
			Point screenp;
			Point temPoint;
			if(fCross){
				screenp=new Point(mapview.getWidth()/2.0f, mapview.getHeight()/2.0f);
			}else{
				screenp=new Point(x, y);
			}
//			temPoint=mapview.toMapPoint(screenp);
			int[] IDs=new int[]{};
			IDs=storageLayer.getGraphicIDs((float)screenp.getX(), (float)screenp.getY(),seltolerance);			
		    int selected=-1;
			for(int i=0;i<IDs.length;i++){
				Map<String, Object> attr=storageLayer.getGraphic(IDs[i]).getAttributes();
				if(attr!=null){
					String tString=(String)attr.get("category");
					Category category=getEnumfromString(tString);
					if(category==Category.boundarypoint){
						selected=i;
					}

				}
			}
			if(selected<0) return;
		
			Graphic g=storageLayer.getGraphic(IDs[selected]);
			int featureid=(Integer)g.getAttributes().get("pointID");
			if(flags2) return;
			if(!flags1){
				lineids[0]=featureid;
				linegs[0]=g.getGeometry();
				selectedlayerids[0]=IDs[selected];
				setCPWlineView( false);
				cpw.showCallout(mapview, (Point)g.getGeometry(), 0, 0);
			}else{
				lineids[1]=featureid;
				linegs[1]=g.getGeometry();
				selectedlayerids[1]=IDs[selected];
				setCPWlineView(true);
				cpw.showCallout(mapview, (Point)g.getGeometry(), 0, 0);
			}
			setLineSelected();
			
			
		}
	}
	public void clearSelectLine() {
		// TODO Auto-generated method stub
		
		
			for (Integer itemInteger : SelectIDS) {
				try {
					jiezhiLines.remove(itemInteger);
					storageLayer.removeGraphic(itemInteger);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
		
		
	}
	public void DeleLine(int id) {
		// TODO Auto-generated method stub
		Integer valueInteger=selectViewHashMap.get(id);
		selectViewHashMap.remove(id);
		jiezhiLines.remove(valueInteger);
		storageLayer.removeGraphic(valueInteger.intValue());
	}

}
