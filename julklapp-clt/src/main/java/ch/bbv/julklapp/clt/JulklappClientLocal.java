package ch.bbv.julklapp.clt;

public class JulklappClientLocal {

	public static void main(String[] args) {
		JulklappClient client = new JulklappClient("http://localhost:8888/");
		client.run();
	}
}
