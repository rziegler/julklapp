package ch.bbv.julklapp.clt;

public class JulklappClientRemote {

	public static void main(String[] args) {
		JulklappClient client = new JulklappClient("http://julklapp-bbv.appspot.com/");
		// client.initializeCircles();
		// client.initializeMembers();
		client.queryWichteliForFirstCircle();
	}
}
