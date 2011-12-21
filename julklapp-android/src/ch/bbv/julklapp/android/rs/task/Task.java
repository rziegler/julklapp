package ch.bbv.julklapp.android.rs.task;

import ch.bbv.julklapp.android.rs.ClientFacade;

public interface  Task<Result> {
	
	Result execute(ClientFacade facade);
	
	void callback(Result result);
}
