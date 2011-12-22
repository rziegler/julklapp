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
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.android.rs.task.GenericJulklappTask;
import ch.bbv.julklapp.android.rs.task.Task;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.WichteliDto;

public class LoginActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private Spinner spinner;

	private EditText username;
	private EditText password;

	private EditText firstname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        spinner = (Spinner)findViewById(R.id.loginSpinner);
        username = (EditText)findViewById(R.id.loginUsername);
        password = (EditText)findViewById(R.id.loginPassword);
        firstname = (EditText)findViewById(R.id.loginFirstname);

        Button button = (Button)findViewById(R.id.loginSolveButton);
        button.setOnClickListener(this);
        
        initCircleSpinner();
    }

	private void initCircleSpinner( ) {
		GenericJulklappTask.execute(this, new Task<List<CircleDto>>(){

			@Override
			public List<CircleDto> execute(ClientFacade facade) {
				return facade.getCircles();
			}

			@Override
			public void callback(List<CircleDto> circles) {
				callbackInitCircleSpinner(circles);
			}
			
		});
	}
	
	private void callbackInitCircleSpinner(List<CircleDto> circles){
		ArrayAdapter<CircleDto> arrayAdapter = new ArrayAdapter<CircleDto>(getBaseContext(), android.R.layout.simple_list_item_1, circles);
	    spinner.setAdapter(arrayAdapter);
	}

	@Override
	public void onClick(View button) {
		
		
		GenericJulklappTask.execute(this, new Task<WichteliDto>(){

			@Override
			public WichteliDto execute(ClientFacade facade) {
				final String usernameString = username.getText().toString();
				final String passwordString = password.getText().toString();
				final String firstnameString = firstname.getText().toString();
				final CircleDto circle = (CircleDto) spinner.getSelectedItem();
				return facade.queryWichetli(circle.getName(), firstnameString, usernameString, passwordString);
			}

			@Override
			public void callback(WichteliDto wichteli) {
				callbackRetrieveWichel(wichteli);
			}
		});	
	}
	
	private void callbackRetrieveWichel(WichteliDto wichteli){
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
}