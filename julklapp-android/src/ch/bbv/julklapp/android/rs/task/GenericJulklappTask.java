package ch.bbv.julklapp.android.rs.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ch.bbv.julklapp.android.config.Config;
import ch.bbv.julklapp.android.rs.ClientFacade;

public class GenericJulklappTask<Result> extends AsyncTask<Void, Integer, Result> {

	private ClientFacade facade;
	private final Task<Result> task;
	private ProgressDialog progressDialog;
	private final Context context;

	public GenericJulklappTask(Context context, Task<Result> task) {
		this.context = context;
		this.task = task;
		facade = new ClientFacade(Config.URL);
	}

	@Override
	protected final void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "", "Loading...", true);
	}

	@Override
	protected final Result doInBackground(Void... params){
		return task.execute(facade);
	}
	
	@Override
	protected final void onPostExecute(Result result) {
		progressDialog.dismiss();
		task.callback(result);
	}
	
	public  static <T> void execute(Context context, Task<T> task) {
		GenericJulklappTask<T> result = new GenericJulklappTask<T>(context, task);
		result.execute();
	}
}
