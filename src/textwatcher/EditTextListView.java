package textwatcher;
import java.util.ArrayList;
import java.util.List;

import com.esri.arcgis.android.samples.graphicelements.DrawGraphicElements;
import com.esri.arcgis.android.samples.graphicelements.GloableFunction;
import com.esri.arcgis.android.samples.graphicelements.NewInfoActivity;
import com.esri.arcgis.android.samples.graphicelements.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditTextListView extends Activity {
	//按钮静态缓存，该用法可以避免使用startActivityForResult来获取按钮返回的时间
	private EditText edit_search;
	private ListView lv;
	private EditTextListViewAdapter adapter;
	List<ValueNameDomain> newlist = new ArrayList<ValueNameDomain>();//查询后的数据list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edittextlistview);
		init();

	}
	//初始化控件
	private void init() {
		edit_search = (EditText) findViewById(R.id.edit_search);
		//为输入添加TextWatcher监听文字的变化
		edit_search.addTextChangedListener(new TextWatcher_Enum());
		adapter = new EditTextListViewAdapter(this, GloableFunction.UserList);
		lv = (ListView) findViewById(R.id.edittextListview);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new onclick());
	}
	//当editetext变化时调用的方法，来判断所输入是否包含在所属数据中
	private List<ValueNameDomain> getNewData(String input_info) {
		//遍历list
		for (int i = 0; i < GloableFunction.UserList.size(); i++) {
			ValueNameDomain domain = GloableFunction.UserList.get(i);
			//如果遍历到的名字包含所输入字符串
			if (domain.getName().contains(input_info)) {
				//将遍历到的元素重新组成一个list
				ValueNameDomain domain2 = new ValueNameDomain();
				domain2.setName(domain.getName());
				domain2.setValue(i + "");
				newlist.add(domain2);
			}
		}
		return newlist;
	}

	//button的点击事件
	class onclick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			TextView text = (TextView) view.findViewById(R.id.tvData);
			//String str = (String) text.getText();
			//btn.setText(str);
			ValueNameDomain value=(ValueNameDomain)text.getTag();
			Intent i = new Intent(EditTextListView.this, DrawGraphicElements.class); 
			i.putExtra("id",value.getValue());
			i.putExtra("name", value.getName());
			EditTextListView.this.setResult(RESULT_OK, i);
            finish();
		}

	}

	//TextWatcher接口
	class TextWatcher_Enum implements TextWatcher {

		//文字变化前
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		//文字变化时
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			newlist.clear();
			if (edit_search.getText() != null) {
				String input_info = edit_search.getText().toString();
				newlist = getNewData(input_info);
				adapter = new EditTextListViewAdapter(EditTextListView.this,
						newlist);
				lv.setAdapter(adapter);
			}
		}

		//文字变化后
		@Override
		public void afterTextChanged(Editable s) {

		}

	}

}
