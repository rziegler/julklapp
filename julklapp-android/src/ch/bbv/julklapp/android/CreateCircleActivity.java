package ch.bbv.julklapp.android;

import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateCircleActivity extends Activity implements OnClickListener {
	
	private static final String TAG = CreateCircleActivity.class.getSimpleName();


	private EditText circleNameField;
	private Button nextButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Start create circle activity.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_circle);
		circleNameField = (EditText) findViewById(R.id.circleNameField);
		nextButton = (Button) findViewById(R.id.createCircleButton);
		nextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		String circleName = circleNameField.getText().toString();
		Intent intent = new Intent(getBaseContext(), AddMemberActivity.class);
		intent.putExtra(Constants.EXTRA_CIRCLE_NAME, circleName);
		ClientFacade facade = new ClientFacade(Config.URL);
		facade.putCircle(circleName);
		startActivity(intent);
		finish();
	}

}