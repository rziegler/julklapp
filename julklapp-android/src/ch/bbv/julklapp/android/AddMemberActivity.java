package ch.bbv.julklapp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.MemberDto;

public class AddMemberActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = AddMemberActivity.class.getSimpleName();
	
	private TextView titleField;

	private EditText firstName;
	private Button finishButton;
	private Button addButton;
	private EditText email;
	private EditText name;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        
        titleField = (TextView)findViewById(R.id.addMemberTitle);
        titleField.setText(titleField.getText()+" "+getCircleName());
        
        firstName = (EditText)findViewById(R.id.addMemberEditFirstname);
        name = (EditText)findViewById(R.id.addMemberEditName);
        email = (EditText)findViewById(R.id.addMemberEditEmail);
        
        addButton = (Button)findViewById(R.id.addMemberButtonAdd);
        addButton.setOnClickListener(this);
        finishButton = (Button)findViewById(R.id.addMemberButtonFinish);
        finishButton.setOnClickListener(this);
        
    }

	private String getCircleName() {
		return getIntent().getExtras().getString(Constants.EXTRA_CIRCLE_NAME);
	}

	@Override
	public void onClick(View button) {
		ClientFacade facade = new ClientFacade(Config.URL);
		MemberDto member = extractMemberData();
		facade.putMember(getCircleName(), member);
		if(button.getId() == R.id.addMemberButtonAdd){
			Intent intent = new Intent(getBaseContext(), AddMemberActivity.class);
			intent.putExtra(Constants.EXTRA_CIRCLE_NAME, getCircleName());
			startActivity(intent);
		}else if(button.getId() == R.id.addMemberButtonFinish){
			Intent intent = new Intent(getBaseContext(), CircleActivity.class);
			intent.putExtra(Constants.EXTRA_CIRCLE_NAME, getCircleName());
			startActivity(intent);
		}
		
	}

	private MemberDto extractMemberData() {
		MemberDto member = new MemberDto();
		member.setFirstName(firstName.getText().toString());
		member.setName(name.getText().toString());
		member.setEmail(email.getText().toString());
		return member;
	}
	
	


	

	
}