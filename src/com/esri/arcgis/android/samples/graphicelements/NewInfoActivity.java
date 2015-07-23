package com.esri.arcgis.android.samples.graphicelements;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import java.util.List;

import textwatcher.EditTextListView;
import textwatcher.EnumButtonActivity;
import textwatcher.ValueNameDomain;

import com.esri.arcgis.android.samples.DataControl.LanFull;
import com.esri.arcgis.android.samples.DataControl.PointFull;
import com.esri.arcgis.android.samples.DataControl.UserInfo;
import com.esri.arcgis.android.samples.graphicelements.R.id;
import com.esri.arcgis.android.samples.graphicelements.R.string;
import com.esri.arcgis.android.samples.graphicelements.SpinnerActivity.SpinnerSelectedListener;
import com.esri.arcgis.android.samples.tableItem.LandItem;
import com.esri.arcgis.android.samples.tableItem.PointFullItem;
import com.esri.arcgis.android.samples.tableItem.UserInfoItem;
import com.esri.core.internal.catalog.Item;
import com.esri.core.internal.catalog.User;

import android.content.Context;
import android.content.Intent;
import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;  
import android.content.DialogInterface;  
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.format.Time;
import android.view.GestureDetector;
import android.view.LayoutInflater;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;  
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;    
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
public class NewInfoActivity extends Activity  {
	private List<LandItem> lst;
    private String positiveButtonText;   
    private double dlat;
    private double dlong;
    //private List<String[]> UserList;
    private String[] SelectList;
    Button choseButton;
    Button AddUserInfo;
    EditText name;
    EditText groundnum;
    EditText idnum;
    EditText gsizeText;
    EditText otherinfo;	
    EditText hnameEditText;
    ImageView Imageview;
    DataControl dataControl;
    public GestureDetector gDetector;  
    String imagePath;
	Profile profile;
	int IDUum=0;
	 protected void onCreate(Bundle savedInstanceState) {  
		 super.onCreate(savedInstanceState);
			//去除标题
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.infodialog);
	        dataControl=new DataControl(this);
	        Bundle extras = getIntent().getExtras();      
	        profile=new Profile(NewInfoActivity.this);
	        choseButton=((Button) findViewById(R.id.choseButton));
	        choseButton.setOnClickListener(new View.OnClickListener() {  
              public void onClick(View v) {
                  InitSelects(); 
              }  
	        });
	        AddUserInfo=((Button) findViewById(R.id.AddUserInfo));
	        AddUserInfo.setOnClickListener(new View.OnClickListener() {  
	              public void onClick(View v) {
	            	  if (CheckForm(name)) 
	            		  if(CheckForm(idnum))
	            		  { 
	            			  try {
	            			  UserInfo tInfo=new UserInfo(NewInfoActivity.this);	            			 
								tInfo.save(new UserInfoItem(name.getText().toString(),idnum.getText().toString()));
								if (GloableFunction.UserList==null) 
									GloableFunction.UserList=new ArrayList<ValueNameDomain>();
								GloableFunction.UserList.add(new ValueNameDomain(name.getText().toString(),idnum.getText().toString()));
								ShowMessage("添加成功");
							}
	            			  catch (SQLException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
									System.err.println(e);
									System.out.print("Something is wrong");
							}
	            			  
	            		  }	            
	              }  
		        });
	        
	         name= ((EditText) findViewById(R.id.Name)) ;	             
	         groundnum= ((EditText) findViewById(R.id.GroundNum));		         
	         idnum =((EditText) findViewById(R.id.IDNum)) ;
	         gsizeText =((EditText) findViewById(R.id.GSize)); 
	         otherinfo =((EditText) findViewById(R.id.OtherInfo)) ;
	         hnameEditText=((EditText)findViewById(R.id.HName));
	         Imageview =((ImageView) findViewById(R.id.Pic)) ;
	         Imageview.setOnTouchListener(new View.OnTouchListener() {
	 			
	 			@Override
	 			public boolean onTouch(View v, MotionEvent event) {
	 				// TODO Auto-generated method stub
	 				gDetector.onTouchEvent(event);
	 				return true;
	 			}
	 		});
	         gDetector=new GestureDetector(this,new SimpleOnGestureListener(){
	 			@Override
	 			public boolean onSingleTapConfirmed(MotionEvent e) {	
	 					if (imagePath==null||imagePath.isEmpty()) {
	 						onDoubleTap(e);
	 					}
	 					else {
	 						final AlertDialog dialog = new AlertDialog.Builder(NewInfoActivity.this).create();
		 					ImageView imgView = new ImageView(NewInfoActivity.this);
		 					imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		 					imgView.setImageDrawable(Imageview.getDrawable());
		 					dialog.setView(imgView);
		 					dialog.show();
		 					imgView.setOnClickListener(new OnClickListener() {
		 						@Override
		 						public void onClick(View v) {
		 							// TODO Auto-generated method stub
		 							dialog.dismiss();
		 						}
		 					});	 				
						}
	 				return true;  
	 			}
	 			@Override
	 			public boolean onDoubleTap(MotionEvent e) {
	 				SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-ddhhmmss");       
	 	 			imagePath=sDateFormat.format(new java.util.Date())+".jpg"; 
	 	 			File picture = new File(profile.getString(R.string.DefaultImageFloder)+"/"+ imagePath);
	 				Uri uri=Uri.fromFile(picture);
	 				Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
	 				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	 				startActivityForResult(intent, 4);
	 			        return true;
	 			}
	 		});	         
         // set the dialog title   
            ((Button) findViewById(R.id.positiveButton))  
                         .setOnClickListener(new View.OnClickListener() {  
                             public void onClick(View v) {
            					 PointFullItem item=new PointFullItem();            					 
            					 item.setName(name.getText().toString().replace(",", "，"));
            					 item.setPersonID(idnum.getText().toString().replace(",", "，"));
            					 item.setGroundID(groundnum.getText().toString().replace(",", "，"));
            					 item.setHousID(hnameEditText.getText().toString().replace(",", "，"));
            					 item.setGroundSize(gsizeText.getText().toString().replace(",", "，"));
            					 item.setOther(otherinfo.getText().toString().replace(",", "，"));
            					 item.setLat(dlat);
            					 item.setLng(dlong);
            					 item.setImgPath(imagePath);//Imageview.getDrawingCache();
            					 PointFull pointFull=new PointFull(NewInfoActivity.this);
            					 try {
            					 if (IDUum>0) {
            						 item.setId(IDUum);
									pointFull.update(item);
            					 	}
            					 else {
            						 item=pointFull.saveAndGetValue(item);
            					 	}									
            					 } catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								 }
            					 Intent i = new Intent(NewInfoActivity.this, DrawGraphicElements.class); 
                                 i.putExtra("id",item.getId());
                                 NewInfoActivity.this.setResult(RESULT_OK, i);
                                 finish();
                            	 /*if(CheckForm(name))
                            		 if (CheckForm(idnum)) 
                            			 if (CheckForm(groundnum)) 
                            				 if(CheckForm(hnameEditText))
                            				 if (CheckForm(gsizeText)) {                            					 
                            					 String[] info=new String[11];
                            					 info[1]=name.getText().toString().replace(",", "，");
                            					 info[2]=idnum.getText().toString().replace(",", "，");
                            					 info[3]=groundnum.getText().toString().replace(",", "，");
                            					 info[4]=hnameEditText.getText().toString().replace(",", "，");
                            					 info[5]=gsizeText.getText().toString().replace(",", "，");
                            					 info[6]=otherinfo.getText().toString().replace(",", "，");
                            					 info[7]=Double.toString(dlat).replace(",", "，");
                            					 info[8]=Double.toString(dlong).replace(",", "，");
                            					 info[9]=imagePath;//Imageview.getDrawingCache();
                            					 info[10]="0";                            					 
                                            	 dataControl.AddPointValue(info);
                                                 Intent i = new Intent(NewInfoActivity.this, DrawGraphicElements.class); 
                                                 i.putExtra("",info[0]);
                                                 NewInfoActivity.this.setResult(RESULT_OK, i);  
                                                 //NewInfoActivity.this.finish();
                                            	 finish();
										}*/
                            	 
                            	 
                             }
                         });  
            ((Button) findViewById(R.id.negativeButton))  
                         .setOnClickListener(new View.OnClickListener() {  
                             public void onClick(View v) {  
                             	finish();
                             }  
                         });  
         // set the cancel button   
          if (positiveButtonText != null) {  
              ((Button) findViewById(R.id.positiveButton))  
                      .setText(positiveButtonText);  
              }
            IDUum=extras.getInt("id");
	        if(IDUum<1){//如果id=0，说明是添加；如果有id!=0，说明是修改；	        	
	        	dlong=extras.getDouble("long");
	        	dlat=extras.getDouble("lat");
	        }
	        else {
	        	PointFull value=new PointFull(this);	        	
	        	try {
					PointFullItem item=value.queryById("ID", Integer.toString(IDUum));
					//imagePath="temp.jpg";
		        	name.setText(item.getName());
		        	idnum.setText(item.getPersonID());
		        	groundnum.setText(item.getGroundID());
		        	hnameEditText.setText(item.getHousID());
		        	gsizeText.setText(item.getGroundSize());
		        	otherinfo.setText(item.getOther());
		        	dlat=item.getLat();
		        	dlong=item.getLng();
		        	imagePath=item.getImgPath();
		        	File picture = new File(profile.getString(R.string.DefaultImageFloder)+"/"+ imagePath);
					Uri uri=Uri.fromFile(picture);
					try {
						Imageview.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
     } 	
	public void ShowMessage(String message)
	    {
	    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	    }
	 private boolean CheckForm(EditText elseView) 
	 {
		 if(elseView.getText().toString()==null||elseView.getText().toString().isEmpty())
		 {
			 SetFocus(elseView);
			 ShowMessage("请输入该处关键字");
			 return false;
		 }
		 return true;
	}
	 private void SetFocus(View ele) {
		  ele.setFocusable(true);
		  ele.setFocusableInTouchMode(true);
		  ele.requestFocus();
		  ele.requestFocusFromTouch();		
	}
	 private void InitSelects()
 	{	
     	if (GloableFunction.UserList==null||GloableFunction.UserList.size()<1) 
     	{
    		try {
    			UserInfo t=new UserInfo(this);
    			List<UserInfoItem> userlistsInfoItems=t.queryAll();
    			if (userlistsInfoItems.size()>0) 
    			{
    				GloableFunction.UserList=new ArrayList<ValueNameDomain>();
    				for (UserInfoItem userInfoItem : userlistsInfoItems) {
    					ValueNameDomain itemStrings=new ValueNameDomain(userInfoItem.getName(),userInfoItem.getIDNumber());
    					GloableFunction.UserList.add(itemStrings);
    				}
    			}
    			else {
         			Toast.makeText(this, "表为空，点击菜单键，导入人员信息表", Toast.LENGTH_SHORT).show();
         			return;
				}
    			
    		}
    		catch(Exception e)
    		{
    			return;
    		}
    	}
 		Intent intent = new Intent(NewInfoActivity.this,EditTextListView.class);
	    startActivityForResult(intent, 8);
 	}
     public void setPositiveButton(String positiveButtonText,  
             DialogInterface.OnClickListener listener) {  
         this.positiveButtonText = positiveButtonText;  
     }  
     @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		// TODO Auto-generated method stub
 		super.onActivityResult(requestCode, resultCode, data);
 		if(resultCode==RESULT_OK)
 		{
 			if (requestCode==4){
 				File picture = new File(profile.getString(R.string.DefaultImageFloder)+"/"+ imagePath);
 				Uri uri=Uri.fromFile(picture);
// 				view.setImageURI(Uri.fromFile(picture));
 				try {
					Imageview.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
					ExifInterface exifInterface;
					exifInterface = new ExifInterface(profile.getString(R.string.DefaultImageFloder)+"/"+imagePath);
					System.out.println(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
					exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,GloableFunction.convertToSexagesimal(Double.toString(dlong)));
					exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,GloableFunction.convertToSexagesimal(Double.toString(dlat)));
					exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,dlat > 0 ?"N" : "S");
					exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,dlong > 0 ?"E" : "W");
					exifInterface.saveAttributes();
				} 
 				catch(Exception e){
 					
 				}
 				}
 			
	 			
 			
 				else if(requestCode==8){
 					//选择完毕 
 					Bundle bundle = data.getExtras();  
 	                String iDnuString = bundle.getString("id");
 	                String nameString=bundle.getString("name");
 	                name.setText(nameString);
 	                idnum.setText(iDnuString); 
 	                
 	    			try {
 	 	               LanFull t=new LanFull(this);
 	 	               lst=t.queryByIds("IDNumber", iDnuString);
						if (null != lst && !lst.isEmpty()) {
							if (lst.size()>1) {
								String[] items=new String[lst.size()]; 
								for (int i = 0; i < lst.size(); i++) {
									items[i]=lst.get(i).getGroundID();
								}
								new AlertDialog.Builder(NewInfoActivity.this).setTitle("地块号").setIcon(
									     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
									     items, 0,
									     new DialogInterface.OnClickListener() {
									      public void onClick(DialogInterface dialog, int which) {
									    	  LandItem item=lst.get(which);
									    	  groundnum.setText(item.getGroundID());
									        	hnameEditText.setText(item.getHousID());
									        	gsizeText.setText(item.getGroundSize());
									        	otherinfo.setText(item.getOther());
									        	dialog.dismiss();
									      }
									     }).setNegativeButton("取消", null).show();
							}
							else {
								 LandItem item=lst.get(0);
						    	  groundnum.setText(item.getGroundID());
						        	hnameEditText.setText(item.getHousID());
						        	gsizeText.setText(item.getGroundSize());
						        	otherinfo.setText(item.getOther());
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 	                SetFocus(groundnum);
 				}
 				//view.refreshDrawableState();
 		}
 		
 		else if(resultCode==RESULT_CANCELED)
 		{
 			imagePath="";
 			return;
 		}
 	}
     /*
    @Override protected void onResume() 
 	{
 		super.onResume(); 
 		File picture = new File(profile.getString(R.string.DefaultImageFloder) + "/temp.jpg");
 		Uri dt=Uri.fromFile(picture);
 		view.setImageURI(Uri.fromFile(picture));
 	}*/
}
