package com.esri.arcgis.android.samples.graphicelements;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
 
public class SpinnerActivity extends Activity {
     
    private static final String[] m={"A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他","A型","B型","O型","AB型","其他"};
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner);
         
        view = (TextView) findViewById(R.id.spinnerText);
        spinner = (Spinner) findViewById(R.id.Name);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
         
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
         
        //添加事件Spinner事件监听  
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
         
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
         
    }
     
    //使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            view.setText("你的血型是："+m[arg2]);
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}