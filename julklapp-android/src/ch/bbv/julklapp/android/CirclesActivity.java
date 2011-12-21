package ch.bbv.julklapp.android;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.CircleDto;

public class CirclesActivity extends Activity implements OnClickListener  {
   
	private static final String TAG = CirclesActivity.class.getSimpleName();
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.circles);
        
      
        
        ListView memberList = (ListView)findViewById(R.id.circlesListView);
        ArrayAdapter<CircleDto> arrayAdapter = new ArrayAdapter<CircleDto>(getBaseContext(), android.R.layout.simple_list_item_1, getCircles());
		memberList.setAdapter(arrayAdapter);
        
    }

	private List<CircleDto> getCircles() {
		ClientFacade facade = new ClientFacade(Config.URL);
		return facade.getCircles();
	}

	

	@Override
	public void onClick(View button) {	
		
	}
}