package ch.bbv.julklapp.android;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.CircleDto;

public class LoginActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private Spinner spinner;

	private EditText username;
	private EditText password;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        spinner = (Spinner)findViewById(R.id.loginSpinner);
        ArrayAdapter<CircleDto> arrayAdapter = new ArrayAdapter<CircleDto>(getBaseContext(), android.R.layout.simple_list_item_1, getCircles());
       
        spinner.setAdapter(arrayAdapter);
        
        username = (EditText)findViewById(R.id.loginUsername);
        password = (EditText)findViewById(R.id.loginPassword);
       
        
    }

	@Override
	public void onClick(View button) {
		ClientFacade facade = new ClientFacade(Config.URL);	
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString();
		CircleDto circle = (CircleDto) spinner.getSelectedItem();
		facade.queryWichetli(circle.getName(), usernameString, passwordString);
	}

	private List<CircleDto> getCircles() {
		ClientFacade facade = new ClientFacade(Config.URL);
		return facade.getCircles();
	}
	
}