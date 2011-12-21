package ch.bbv.julklapp.android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.WichteliDto;

public class LoginActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private Spinner spinner;

	private EditText username;
	private EditText password;

	private EditText firstname;

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
        firstname = (EditText)findViewById(R.id.loginFirstname);
        
        Button button = (Button)findViewById(R.id.loginSolveButton);
        button.setOnClickListener(this);
       
        
    }

	@Override
	public void onClick(View button) {
		ClientFacade facade = new ClientFacade(Config.URL);	
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString();
		String firstnameString = firstname.getText().toString();
		CircleDto circle = (CircleDto) spinner.getSelectedItem();
		WichteliDto wichteli = facade.queryWichetli(circle.getName(), firstnameString, usernameString, passwordString);
		if(wichteli != null){
			Intent intent = new Intent(getBaseContext(), SolveActivity.class);
			intent.putExtra(Constants.EXTRA_WICHTELI_FIRSTNAME, wichteli.getFirstname());
			intent.putExtra(Constants.EXTRA_WICHTELI_NAME, wichteli.getName());
			startActivity(intent);	
			finish();
		}else{
			password.getText().clear();
		}
	}

	private List<CircleDto> getCircles() {
		ClientFacade facade = new ClientFacade(Config.URL);
		return facade.getCircles();
	}
	
}