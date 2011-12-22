package ch.bbv.julklapp.android;

import android.app.Activity;

public class AbstractCircleActivity extends Activity{


	protected String getCircleName() {
		return getIntent().getExtras().getString(Constants.EXTRA_CIRCLE_NAME);
	}
}
