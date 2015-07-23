package com.esri.arcgis.android.samples.graphicelements;
import java.util.List;

import com.esri.arcgis.android.samples.graphicelements.R.string;
import com.esri.arcgis.android.samples.graphicelements.SpinnerActivity.SpinnerSelectedListener;

import android.content.Context;
import android.app.Dialog;  
import android.content.DialogInterface;  
import android.text.Editable;
import android.view.LayoutInflater;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;    
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
public class InfoDialog extends Dialog  {
	 public InfoDialog(Context context) {  
	        super(context);  
	    }  
	  
	    public InfoDialog(Context context, int theme) {  
	        super(context, theme);  
	    }  
	  
	    public static class Builder {  
	        private Context context;  	  
	        private String title;  	        
	        private String positiveButtonText;  
	        private DialogInterface.OnClickListener positiveButtonClickListener;  
	        private DialogInterface.OnClickListener negativeButtonClickListener;  
	        private List<String[]> UserList;
	        private int UserID;
	        Spinner choseButton;
	        EditText name;
	        EditText groundnum;
	        EditText idnum;
	        EditText gsizeText;
	        EditText otherinfo;	       
	        ImageView pic;
	        //private static final String[] m={"A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他"};
	        private ArrayAdapter<String> adapter;
	        public Builder(Context context) {  
	            this.context = context;  
	        }  	  	  
	  
	        public Builder setContentView(View v) {  
	            return this;  
	        } 
	        public Builder setPositiveButton(DialogInterface.OnClickListener listener) { 
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setNegativeButton(DialogInterface.OnClickListener listener) {  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	        private void InitSpinner()
	    	{	
	        	if (UserList==null) {
					return;
				}
	    		String[] tempStrings=new String[UserList.size()+1];
	    		tempStrings[0]="";		
	    		for (int i = 0; i < UserList.size(); i++) {
	    			tempStrings[i+1]=UserList.get(i)[1];
	    		}
	            adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,tempStrings);
	            
	            //设置下拉列表的风格
	            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            //将adapter 添加到spinner中
	            choseButton.setAdapter(adapter);
	            //添加事件Spinner事件监听  
	            choseButton.setOnItemSelectedListener(new SpinnerSelectedListener());
	            //设置默认值
	            choseButton.setVisibility(View.VISIBLE);
	    	}
	        public InfoDialog create() { 
 
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	            // instantiate the dialog with the custom Theme  
	            final InfoDialog dialog = new InfoDialog(context);  
	            View layout = inflater.inflate(R.layout.infodialog, null);
	             //choseButton=((Spinner) layout.findViewById(R.id.ChoseList));
	             InitSpinner(); 
	             name= ((EditText) layout.findViewById(R.id.Name)) ;	             
		         groundnum= ((EditText) layout.findViewById(R.id.GroundNum));		         
		         idnum =((EditText) layout.findViewById(R.id.IDNum)) ;
		         gsizeText =((EditText) layout.findViewById(R.id.GSize)); 
		         otherinfo =((EditText) layout.findViewById(R.id.OtherInfo)) ;
		         pic =((ImageView) layout.findViewById(R.id.Pic)) ;		         
		         dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));  
	            // set the dialog title  
	             if (positiveButtonClickListener != null) {  
	                    ((Button) layout.findViewById(R.id.positiveButton))  
	                            .setOnClickListener(new View.OnClickListener() {  
	                                public void onClick(View v) {  
	                                    positiveButtonClickListener.onClick(dialog,  
	                                            DialogInterface.BUTTON_POSITIVE);  
	                                    
	                                }  
	                            });  
	                }
	             if (positiveButtonClickListener != null) {  
	                    ((Button) layout.findViewById(R.id.negativeButton))  
	                            .setOnClickListener(new View.OnClickListener() {  
	                                public void onClick(View v) {  
	                                	negativeButtonClickListener.onClick(dialog,  
	                                            DialogInterface.BUTTON_POSITIVE);  
	                                }  
	                            });  
	                }  
	            // set the cancel button   
	             if (positiveButtonText != null) {  
	                 ((Button) layout.findViewById(R.id.positiveButton))  
	                         .setText(positiveButtonText);  
	                 }
	            dialog.setContentView(layout);  
	            if (title!="") {
	            	dialog.setTitle(title);
				}
	            else {
	            		dialog.setTitle(R.string.notice);
	            	}	            
	            return dialog;  
	        }
	        //使用数组形式操作
	        class SpinnerSelectedListener implements OnItemSelectedListener{
	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	                    long arg3) {
	            	String[] valueStrings=UserList.get(arg2);
	            	name.setText(valueStrings[1]);
	            	idnum.setText(valueStrings[2]); 
	            }	     
	            public void onNothingSelected(AdapterView<?> arg0) {
	            }
	        }
	        public Builder setUserList(List<String[]> value) {  
	            this.UserList = value;  
	            return this;  
	        }
	        public Builder setPositiveButton(String positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = positiveButtonText;  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	        
	        /** 
	         * Set the Dialog title from resource 
	         *  
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(int title) {  
	            this.title = (String) context.getText(title);  
	            return this;  
	        }  
	  
	        /** 
	         * Set the Dialog title from String 
	         *  
	         * @param title 
	         * @return 
	         */  
	  
	        public Builder setTitle(String title) {  
	            this.title = title;  
	            return this;  
	        }  
	  
	    }  
}
