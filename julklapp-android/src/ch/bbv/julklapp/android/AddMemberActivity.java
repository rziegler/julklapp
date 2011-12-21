package ch.bbv.julklapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.android.rs.task.GenericJulklappTask;
import ch.bbv.julklapp.android.rs.task.Task;
import ch.bbv.julklapp.dto.MemberDto;

public class AddMemberActivity extends AbstractCircleActivity implements OnClickListener  {
   
	private static final String TAG = AddMemberActivity.class.getSimpleName();
	
	private TextView titleField;

	private EditText firstName;
	private Button finishButton;
	private Button addButton;
	private EditText email;
	private EditText name;

	
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


	@Override
	public void onClick(final View button) {
		
		GenericJulklappTask.execute(this, new Task<Void>(){

			@Override
			public Void execute(ClientFacade facade) {
				MemberDto member = extractMemberData();
				facade.putMember(getCircleName(), member);
				return null;
			}

			@Override
			public void callback(Void v) {
				callbackAfterPuttingTheMember(button);	
			}
		});
	}

	private void callbackAfterPuttingTheMember(View button) {
		if(button.getId() == R.id.addMemberButtonAdd){
			startNextActivity(AddMemberActivity.class);
		}else if(button.getId() == R.id.addMemberButtonFinish){
			startNextActivity(CircleActivity.class);
		}
	}


	private void startNextActivity(Class<?> nextActicity) {
		Intent intent = new Intent(getBaseContext(), nextActicity);
		intent.putExtra(Constants.EXTRA_CIRCLE_NAME, getCircleName());
		finish();
		startActivity(intent);
	}

	private MemberDto extractMemberData() {
		MemberDto member = new MemberDto();
		member.setFirstName(firstName.getText().toString());
		member.setName(name.getText().toString());
		member.setEmail(email.getText().toString());
		return member;
	}
}