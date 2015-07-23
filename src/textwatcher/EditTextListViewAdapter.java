package textwatcher;

import java.util.List;

import com.esri.arcgis.android.samples.graphicelements.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditTextListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ValueNameDomain> data;

	public EditTextListViewAdapter(Context context, List<ValueNameDomain> data) {
		this.inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item, null);
		}

		TextView textView = (TextView) convertView.findViewById(R.id.tvData);
		textView.setText((String) data.get(position).getName());
		textView.setTag(data.get(position));
		return convertView;
	}


}
