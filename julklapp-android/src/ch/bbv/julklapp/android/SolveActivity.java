package ch.bbv.julklapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SolveActivity extends Activity {
   
	private static final String TAG = SolveActivity.class.getSimpleName();
	
	private TextView titleField;

	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Show wichtel.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.circle);
        
        titleField = (TextView)findViewById(R.id.solveWichtel);
        titleField.setText(titleField.getText()+getFirstName()+" "+getName());     
    }



	private String getFirstName() {
		return getIntent().getExtras().getString(Constants.EXTRA_WICHTELI_FIRSTNAME);
	}
	
	private String getName() {
		return getIntent().getExtras().getString(Constants.EXTRA_WICHTELI_NAME);
	}

	
}