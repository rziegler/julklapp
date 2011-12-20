package ch.bbv.julklapp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateCircleActivity extends Activity implements OnClickListener {

	private EditText circleNameField;
	private Button nextButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		intent.putExtra("circleName", circleName);
		startActivity(intent);
	}

}