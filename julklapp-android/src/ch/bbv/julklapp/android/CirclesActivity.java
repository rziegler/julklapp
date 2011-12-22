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
import ch.bbv.julklapp.android.rs.ClientFacade;
import ch.bbv.julklapp.android.rs.task.GenericJulklappTask;
import ch.bbv.julklapp.android.rs.task.Task;
import ch.bbv.julklapp.dto.CircleDto;

public class CirclesActivity extends Activity implements OnItemClickListener  {
   
	private static final String TAG = CirclesActivity.class.getSimpleName();
	private ListView circleList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Add member activity.");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.circles);
        circleList = (ListView)findViewById(R.id.circlesListView);
        initCircleList();
    }
    
    private void initCircleList() {
		GenericJulklappTask.execute(this, new Task<List<CircleDto>>(){

			@Override
			public List<CircleDto> execute(ClientFacade facade) {
				return facade.getCircles();
			}

			@Override
			public void callback(List<CircleDto> circles) {
				initCircleListCallback(circles);
			}
			
		});
	}
	
	private void initCircleListCallback(List<CircleDto> circles){
		ArrayAdapter<CircleDto> arrayAdapter = new ArrayAdapter<CircleDto>(getBaseContext(), android.R.layout.simple_list_item_1, circles);
		circleList.setAdapter(arrayAdapter);
		circleList.setOnItemClickListener(this);  
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CircleDto circle = (CircleDto)parent.getAdapter().getItem(position);
		Intent intent = new Intent(getBaseContext(), CircleActivity.class);
		intent.putExtra(Constants.EXTRA_CIRCLE_NAME, circle.getName());
		startActivity(intent);	
	}
}