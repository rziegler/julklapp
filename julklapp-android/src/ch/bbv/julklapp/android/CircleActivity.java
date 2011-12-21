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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.android.rs.task.GenericJulklappTask;
import ch.bbv.julklapp.android.rs.task.Task;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;

public class CircleActivity extends Activity implements OnClickListener {

	private static final String TAG = CircleActivity.class.getSimpleName();

	private TextView titleField;

	private ListView memberList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Add member activity.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle);

		titleField = (TextView) findViewById(R.id.circleTitle);
		titleField.setText(titleField.getText() + " " + getCircleName());

		Button shuffleButton = (Button) findViewById(R.id.circleButtonShuffle);
		shuffleButton.setOnClickListener(this);
		Button addButton = (Button) findViewById(R.id.circleButtonAddMember);
		addButton.setOnClickListener(this);

		memberList = (ListView) findViewById(R.id.circleMemberList);
		initMemberList();

	}

	private void initMemberList() {
		GenericJulklappTask.create(this, new Task<List<MemberDto>>(){

			@Override
			public List<MemberDto> execute(ClientFacade facade) {
				CircleDto circle = facade.getCircle(getCircleName());
				return circle.getMemberList().getMember();
			}

			@Override
			public void callback(List<MemberDto> result) {
				initMemberListCallback(result);
			}
		}).execute();
	}
	
	private void initMemberListCallback(List<MemberDto> members){
		ArrayAdapter<MemberDto> arrayAdapter = new ArrayAdapter<MemberDto>(getBaseContext(),
				android.R.layout.simple_list_item_1, members);
		memberList.setAdapter(arrayAdapter);
	}

	

	private String getCircleName() {
		return getIntent().getExtras().getString(Constants.EXTRA_CIRCLE_NAME);
	}

	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.circleButtonAddMember:
			Intent intent = new Intent(getBaseContext(), AddMemberActivity.class);
			intent.putExtra(Constants.EXTRA_CIRCLE_NAME, getCircleName());
			startActivity(intent);
			finish();
			break;
		case R.id.circleButtonShuffle:
			shuffle(button);
			break;
		}
	}

	private void shuffle(final View button) {
		GenericJulklappTask.create(this, new Task<Void>(){

			@Override
			public Void execute(ClientFacade facade) {
				facade.shuffle(getCircleName());
				return null;
			}

			@Override
			public void callback(Void v) {
				callbackAfterShuffle(button);
			}
		}).execute();
	}
	
	private void callbackAfterShuffle(View button){
		button.setClickable(false);
		Toast.makeText(getBaseContext(), "Suffled...", 3).show();
	}
}