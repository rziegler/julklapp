package ch.bbv.julklapp.android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.dto.CircleDto;

public class CirclesActivity extends Activity implements OnItemClickListener  {
   
	private static final String TAG = CirclesActivity.class.getSimpleName();
	private ListView circleList;
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.circles);
        
        circleList = (ListView)findViewById(R.id.circlesListView);
        ArrayAdapter<CircleDto> arrayAdapter = new ArrayAdapter<CircleDto>(getBaseContext(), android.R.layout.simple_list_item_1, getCircles());
		circleList.setAdapter(arrayAdapter);
		circleList.setOnItemClickListener(this);  
    }

	private List<CircleDto> getCircles() {
		ClientFacade facade = new ClientFacade(Config.URL);
		return facade.getCircles();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CircleDto circle = (CircleDto)parent.getAdapter().getItem(position);
		Intent intent = new Intent(getBaseContext(), CircleActivity.class);
		intent.putExtra(Constants.EXTRA_CIRCLE_NAME, circle.getName());
		startActivity(intent);	
	}
}