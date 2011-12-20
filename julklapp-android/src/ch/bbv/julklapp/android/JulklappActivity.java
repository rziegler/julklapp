package ch.bbv.julklapp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class JulklappActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup radioGroup;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("einTag", "sowas von doof");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		radioGroup = (RadioGroup) findViewById(R.id.WorkflowSelection);
		radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int arg1) {

		if (group.getCheckedRadioButtonId() == R.id.createCircle) {
			startActivity(new Intent(getBaseContext(),
					CreateCircleActivity.class));
		}

	}
}