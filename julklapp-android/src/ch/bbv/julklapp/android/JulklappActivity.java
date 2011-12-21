package ch.bbv.julklapp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class JulklappActivity extends Activity implements OnCheckedChangeListener {

	private static final String TAG = JulklappActivity.class.getSimpleName();
	private RadioGroup radioGroup;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Start Julklapp Application.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		radioGroup = (RadioGroup) findViewById(R.id.WorkflowSelection);
		radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int arg1) {

		switch (group.getCheckedRadioButtonId()) {
		case R.id.createCircle:
			startActivity(new Intent(getBaseContext(), CreateCircleActivity.class));
			break;
		case R.id.showCircles:
			startActivity(new Intent(getBaseContext(), CirclesActivity.class));
			break;
		case R.id.solve:
			startActivity(new Intent(getBaseContext(), LoginActivity.class));
			break;
		default:
			break;
		}
	}
}