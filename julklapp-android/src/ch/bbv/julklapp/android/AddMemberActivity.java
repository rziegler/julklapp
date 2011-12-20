package ch.bbv.julklapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AddMemberActivity extends Activity  {
   
	private TextView titleField;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        
        titleField = (TextView)findViewById(R.id.addMemberTitle);
        titleField.setText(titleField.getText()+" "+getCircleName());
    }

	private String getCircleName() {
		return getIntent().getExtras().getString("circleName");
	}

	

	
}