package com.esri.arcgis.android.samples.graphicelements;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import textwatcher.ValueNameDomain;

import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.DataControl.JieZhiPoint;
import com.esri.arcgis.android.samples.DataControl.LanFull;
import com.esri.arcgis.android.samples.DataControl.LoginUser;
import com.esri.arcgis.android.samples.DataControl.UserInfo;
import com.esri.arcgis.android.samples.tableItem.JieZhiPointItem;
import com.esri.arcgis.android.samples.tableItem.LandItem;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;
import com.esri.arcgis.android.samples.tableItem.UserInfoItem;

import Code.serial;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author SONG
 *
 */
/**
 * @author SONG
 *
 */
/**
 * @author SONG
 *
 */
public class LoginActivity extends Activity {
	/********************登陆用************************/
	ArrayList<String[]> userList;
	String[] SelectList;
	CSV _cCsv;
	Profile profile;	
	/*******************************************/	
	private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
	private SharedPreferences sp;
	private  Button choseSpinner;
	private ArrayAdapter adapter;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
        InitLayout();
		/*初始化Cvs文件*/
        InitCvs();
        InitLoginUser();
        CreatDefaultFloder();
        if (!CheckValid()) {
        	ShowCheck();
		}
		//
	}
	private boolean CheckValid()
	{
		String uuid=profile.getString(R.string.Serial);
		serial n;
		try {
			n = new serial(getMyUUID());
			//System.out.println("Su codigo es: " + n.generador18digitosEstatico());
			if (uuid.equals(n.generador18digitosEstatico())) {
			//if (uuid.equals("123")) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
			//return false;
		}
		return false;
	}
	private void ShowCheck() {		
		LayoutInflater inflater = getLayoutInflater();
		 final View layoutDialog = inflater.inflate(R.layout.serial,null);
		// TODO Auto-generated method stubs					
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		
		EditText textView=(EditText) layoutDialog.findViewById(R.id.deviceNo);
		EditText text=(EditText)layoutDialog.findViewById(R.id.serial);
		textView.setText(getMyUUID());
		builder.setView(layoutDialog);
		builder.setTitle("软件注册");
		builder.setNeutralButton("确定",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {			
				EditText textView=(EditText)layoutDialog.findViewById(R.id.deviceNo);
				EditText text=(EditText)layoutDialog.findViewById(R.id.serial);	
				profile.Save(R.string.Serial,text.getText().toString());
				if(CheckValid())
				{
					String mapString=profile.getString(R.string.DefaultFloderPath)+"/layer.tpk";
					String userString=profile.getString(R.string.DefaultFloderPath)+"/user.csv";
					String loginString=profile.getString(R.string.DefaultFloderPath)+"/login.csv";
					String landString=profile.getString(R.string.DefaultFloderPath)+"/land.csv";
					if (FileUtils.fileIsExists(mapString)) profile.Save(R.string.mappath, mapString);
					if (FileUtils.fileIsExists(loginString)) 
					{
						profile.Save(R.string.UserLoginCvs, loginString);            	
						userList=LoadUser(loginString);
		            	InitSelects(userList);
						InsertOrAdd(false, userList);
					}	
					if (FileUtils.fileIsExists(landString)) 
					{
						LanFull Land=new LanFull(LoginActivity.this);
						try {
							ArrayList<String[]> landList;
							landList = _cCsv.readeCsv(landString);
							for (String[] point : landList) {
								LandItem item2=new LandItem(Integer.parseInt(point[0]), point[1], point[2],point[3],point[4],point[5], point[6]);
				    			Land.save(item2);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//(profile.getString(R.string.PointCvs));
					     	
		            }
					
					if (FileUtils.fileIsExists(userString)) 
					{
						UserInfo userInfo=new UserInfo(LoginActivity.this);
						try {
							ArrayList<String[]> pointList;
							pointList = _cCsv.readeCsv(userString);
							for (String[] point : pointList) {
								UserInfoItem item2=new UserInfoItem(Integer.parseInt(point[0]), point[1], point[2]);
				    			if (GloableFunction.UserList==null) 
									GloableFunction.UserList=new ArrayList<ValueNameDomain>();
									GloableFunction.UserList.add(new ValueNameDomain(point[1],point[2]));    
								userInfo.save(item2);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//(profile.getString(R.string.PointCvs));
					     	
		            }
					ShowMessage("注册成功，请登录");					
					dialog.dismiss();
				}
				else {
					ShowMessage("注册码错误，请联系管理员");
				}
			}
		});
		builder.setNegativeButton("取消",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		});
		builder.create().show();
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
			path=Environment.getExternalStorageDirectory()+"/"+getResources().getString(R.string.DefaultFloderPath);
			pathimg=path+"/"+getResources().getString(R.string.DefaultImageFloder);
			FileUtils.CreatFloder(path);
			FileUtils.CreatFloder(pathimg);
		}
		else {//如果没有sd卡，则保存到文件夹的私有空间上；
			path=getResources().getString(R.string.DefaultFloderPath);		
			pathimg=path+"/"+getResources().getString(R.string.DefaultFloderPath);	
			FileUtils.CreatFloder(path);
			FileUtils.CreatFloder(pathimg);
		}
		profile.Save(R.string.DefaultFloderPath, path);
		profile.Save(R.string.DefaultImageFloder, pathimg);
	}
	private String getMyUUID(){
		  final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		  final String tmDevice, tmSerial, tmPhone, androidId;
		  tmDevice = "" + tm.getDeviceId();
		  tmSerial = "" + tm.getSimSerialNumber();
		  androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
		  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		  String uniqueId = deviceUuid.toString();
		  Log.d("debug","uuid="+uniqueId);		  
		  uniqueId=uniqueId.replace("0", "").replace("-", "").replace("fff", "");
		  return uniqueId;		  
	 }
	public boolean CheckMap() {
		if(profile.getString(R.string.mappath).equals(""))
		{
			new AlertDialog.Builder(this) 
			                .setTitle("提示：")  
			                .setMessage("点击菜单键，配置地图文件")
			                .show();  
			return false;
		}
		else if (!FileUtils.fileIsExists(profile.getString(R.string.mappath))) {
			new AlertDialog.Builder(this) 
            .setTitle("提示：")  
            .setMessage("地图文件不存在，请重新配置")
            .show();  
			return false;
		} else return true;
	}
	public void InitLoginUser()
	{
		LoginUser loginUser=new LoginUser(this);
		 //ArrayList<CashFlow> cashFlows = (ArrayList<CashFlow>)cashFlowDao.query(map, lowMap,highMap);
		List<LogUserItem> loginusers;
		try {
			loginusers=loginUser.queryAll();
			if (loginusers.size()<1) 
			{
				ShowMessage("请导入用户登录用表");
				return;
			}
			userList=new ArrayList<String[]>();
			for (int i = 0; i < loginusers.size(); i++) {
				String[] itemStrings=new String[3];
				itemStrings[0]=Integer.toString(loginusers.get(i).getID());
				itemStrings[1]=itemStrings[0]=loginusers.get(i).getName();
				itemStrings[2]=itemStrings[0]=loginusers.get(i).getPassword();
				userList.add(itemStrings);				
			}
			InitSelects(userList);
			return;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();			
			System.err.println(e1);
		}
		new AlertDialog.Builder(this) 
        .setTitle("提示：")  
        .setMessage("点击菜单键，配置用户文件和地图文件")
        .show();  
	}
	private void InsertDB() {
		// TODO Auto-generated method stubs					
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		builder.setMessage("清空后导入还是增加？"); 
		builder.setTitle("导入登陆用户表");
		builder.setNegativeButton("覆盖导入",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
            	userList=LoadUser(profile.getString(R.string.UserLoginCvs));
            	InitSelects(userList);
				InsertOrAdd(true, userList);
				dialog.dismiss();				
			}
		});
		builder.setNeutralButton("增加导入",new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				userList=LoadUser(profile.getString(R.string.UserLoginCvs));
            	InitSelects(userList);
				InsertOrAdd(false, userList);	
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
	 * @param userList2
	 */
	private void  InsertOrAdd(boolean is,ArrayList<String[]> userList2) {
		LoginUser loginUser=new LoginUser(this);
		try {
			if (loginUser.isTableExsits()) {
				for (int i = 0; i < userList2.size(); i++) {
					String[] item=userList2.get(i);
					if (is) {
						loginUser.SaveOrUpdate(new LogUserItem(Integer.parseInt(item[0]),item[1],item[2]));
					}
					else loginUser.save(new LogUserItem(item[1],item[2]));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//创建默认文件；点元素和啥啥表；	
	private void InitLayout() {
		 //获得实例对象	
		choseSpinner=((Button) findViewById(R.id.spinner1));
		choseSpinner.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				if(SelectList==null)
				{
					ShowMessage("请点击菜单键配置登陆信息");
					return;
				}
				simpleDialog(SelectList);	
			}
		});
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        btnQuit = (ImageButton)findViewById(R.id.img_btn);
		//判断记住密码多选框的状态
      if(sp.getBoolean("ISCHECK", false))
        {
    	  //设置默认是记录密码状态
          rem_pw.setChecked(true);
       	  userName.setText(sp.getString("USER_NAME", ""));
       	  password.setText(sp.getString("PASSWORD", ""));
       	  //判断自动登陆多选框状态
       	  if(sp.getBoolean("AUTO_ISCHECK", false))
       	  {
       		     //设置默认是自动登录状态
       		     auto_login.setChecked(true);
       		    //跳转界面
				Intent intent = new Intent(LoginActivity.this,DrawGraphicElements.class);
				LoginActivity.this.startActivity(intent);
				//finish();
				
       	  }
        }		
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!CheckValid()) {
		        	ShowCheck();
		        	return;
				}
				if(!CheckMap())return;
				if(userList==null||userList.size()<1)
				{
					ShowMessage("点击菜单配置用户登陆用表");
					return;
				}
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();
			    boolean isRight = false;
			    for (int i = 0; i < userList.size(); i++) {
					if(userList.get(i)[1].equals(userNameValue)&&userList.get(i)[2].equals(passwordValue))
						{
							isRight=true;
							break;
						}
				}
				if(isRight)
				{
					Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
					//登录成功和记住密码框为选中状态才保存用户信息
					if(rem_pw.isChecked())
					{
					 //记住用户名、密码、
					  Editor editor = sp.edit();
					  editor.putString("USER_NAME", userNameValue);
					  editor.putString("PASSWORD",passwordValue);
					  editor.commit();
					}
					//跳转界面
					Intent intent = new Intent(LoginActivity.this,DrawGraphicElements.class);
					LoginActivity.this.startActivity(intent);
					//finish();
					
				}else{
					Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
				}
				
			}
		});

	    //监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (rem_pw.isChecked()) {
                    
					System.out.println("记住密码已选中");
					sp.edit().putBoolean("ISCHECK", true).commit();
					
				}else {
					
					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();
					
				}

			}
		});
		
		//监听自动登录多选框事件
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (auto_login.isChecked()) {
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				} else {
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		
		btnQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});	
		
	}
	private void InitCvs() {
		// TODO Auto-generated method stub
		profile=new Profile(this);
		_cCsv=new CSV();
	}
	 private void InitSelects(ArrayList<String[]> userList)
	 	{	
	     	if (SelectList==null) {
	     		if (userList==null) {
	     			ShowMessage("点击菜单键配置选择项");
	    			return;
	    			}
	     		SelectList=new String[userList.size()];	
	     		for (int i = 0; i < userList.size(); i++) {
	     			SelectList[i]=userList.get(i)[1];
	     		}
			}
	 	}
	 /**  
      * 单选Dialog  
      */  
     private void simpleDialog(String[] items){ 
      AlertDialog.Builder builder = new AlertDialog.Builder(this);  
      builder.setTitle("选择登录用户");  
      builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {  
          @Override  
          public void onClick(DialogInterface dialog, int which) {  
              // TODO Auto-generated method stub  
              //Toast.makeText(mContext, "你选择的ID为："+which, Toast.LENGTH_SHORT).show();  
        	  String[] valueStrings=userList.get(which);
        	  userName.setText(valueStrings[1]);
        	  password.setText(valueStrings[2]);  
        	  dialog.dismiss();
          }  
      }); 
      builder.create().show();  
     }  	
	private ArrayList<String[]> LoadUser(String path) {
		if(FileUtils.fileIsExists(path))
		{
			try {				
				return _cCsv.readeCsv(path);
			} catch (IOException e) {
				return null;				
				// TODO Auto-generated catch block				
			}
		}
		else {
			ShowMessage("用户列表不存在，请重新选择");
			return null;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		 // 加载菜单  
        getMenuInflater().inflate(R.menu.loginmenu, menu);  
        return true;  
	    }
	 //根据菜单执行相应内容  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {  
        case R.id.openMap:  
        	showFileChooser(GloableFunction.Map_SELECT_CODE);
            //Toast.makeText(getApplicationContext(), "加载地图", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.readdata:
        	showFileChooser(GloableFunction.User_SELECT_CODE);
        	//Toast.makeText(getApplicationContext(), "正在加载保存的数据", Toast.LENGTH_SHORT).show();
        	return true;
        }  
        return false;  
    }
    /*文件选择器*/
    private void showFileChooser(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("*/*"); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "选择文件"), code);
        } catch (android.content.ActivityNotFoundException ex) {
            ShowMessage("Please install a File Manager.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
    	if (resultCode != RESULT_OK)
    		{
    			ShowMessage("选择错误，请重新选择");
    			return;
    		}
    	 // Get the Uri of the selected file 
        Uri uri = data.getData();
        String path = FileUtils.getPath(this, uri);
        switch (requestCode) {
            case 0:       
                // Get the Uri of the selected file
                profile.Save(R.string.mappath, path);
            break;
            case 1:
            	profile.Save(R.string.UserLoginCvs, path);            	
            	InsertDB();
            	//checkCvs();
            	break;
        }
    //super.onActivityResult(requestCode, resultCode, data);
    }    
    
	public void ShowMessage(String message)
    {
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
	
}