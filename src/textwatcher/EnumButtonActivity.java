package textwatcher;

import com.esri.arcgis.android.samples.graphicelements.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class EnumButtonActivity extends Activity {
	private Button btnEnum;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editselect);
		btnEnum = (Button) findViewById(R.id.btnEnum);
		btnEnum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnumButtonActivity.this,EditTextListView.class);
				//EditTextListView.btn = btnEnum;
				startActivity(intent);

			}
		});
	}
}