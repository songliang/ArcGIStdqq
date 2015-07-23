package com.esri.arcgis.android.samples.graphicelements;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

public class BtnGPSListener implements OnClickListener {
	
	public LocationDisplayManager locationService = null;
	private MapView map;
	private Context context;
	public double dLong;
	public double dLat;
	private  Boolean firstlocationchanged=true;
	public  BtnGPSListener(Context xcontext,MapView view)
	{
		context=xcontext;
		map=view;
		locationService = map.getLocationDisplayManager();
		locationService.setLocationListener(new MyLocationListener());
		locationService.setAccuracyCircleOn(true);
		locationService.setAllowNetworkLocation(true);
		locationService.setShowLocation(true);
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub		
		Button gpsButton=(Button) v;
		int flag=(Integer) gpsButton.getTag();
		switch (flag) {
		case 0:		
			gpsButton.setTag(1);
//			gpsButton.setText(R.string.GPSoff);
			gpsButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.locationon_72));
			Toast.makeText(map.getContext(), "开始定位……", Toast.LENGTH_SHORT).show();
			locationService.start();
			firstlocationchanged=true;
			break;
		default:
			gpsButton.setTag(0);
//			gpsButton.setText(R.string.GPSon);
			gpsButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.locationoff_72));
			Toast.makeText(map.getContext(), "停止定位……", Toast.LENGTH_SHORT).show();
			dLong=0.0;
			dLat=0.0;
			locationService.stop();
			break;
		}		
	} 
	public void SetCenter()
	{
		if(dLong==0.0&&dLat==0.0)return;
		Point ptMap = (Point)GeometryEngine.project(dLong, dLat, map.getSpatialReference());
		DecimalFormat decimalFormat=new DecimalFormat("#.000000");
		String tString=" "+decimalFormat.format(dLong)+"  "+decimalFormat.format(dLat)+" ";
		((DrawGraphicElements)context).crosspositionTextView.setText(tString);
		map.centerAt(ptMap,false);
		//map.zoomTo(ptMap, 8);
	}
	class MyLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			dLong = arg0.getLongitude();
			dLat = arg0.getLatitude();
			if(firstlocationchanged){
				SetCenter();
				firstlocationchanged=false;
			}
		  //map.centerAt(ptMap,false);
//			map.zoomTo(ptMap, 8);
		}		
		
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(map.getContext(), "定位功能已禁用！", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(map.getContext(), "定位功能已启用！", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO Auto-generated method stub		
		}
	}

}
