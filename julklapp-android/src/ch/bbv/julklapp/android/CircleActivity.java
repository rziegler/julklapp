package ch.bbv.julklapp.android;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;

public class CircleActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = CircleActivity.class.getSimpleName();
	
	private TextView titleField;

	private Button exitButton;
	private Button shuffleButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.circle);
        
        titleField = (TextView)findViewById(R.id.circleTitle);
        titleField.setText(titleField.getText()+" "+getCircleName());
        
        ListView memberList = (ListView)findViewById(R.id.circleMemberList);
        ArrayAdapter<MemberDto> arrayAdapter = new ArrayAdapter<MemberDto>(getBaseContext(), android.R.layout.simple_list_item_1, getMembers());
		memberList.setAdapter(arrayAdapter);
        
    }

	private List<MemberDto> getMembers() {
		ClientFacade facade = new ClientFacade(Config.URL);
		CircleDto circle = facade.getCircle(getCircleName());
		return circle.getMemberList();
	}

	private String getCircleName() {
		return getIntent().getExtras().getString(Constants.EXTRA_CIRCLE_NAME);
	}

	@Override
	public void onClick(View button) {	
		
	}
}