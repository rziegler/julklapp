package ch.bbv.julklapp.password;

import java.util.Random;

public class CharPasswordGenerator implements PasswordGenerator{
	
	public String generatePassword(){
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 8; i++){
			int value = random.nextInt(26);
			sb.append((char)((int)'a'+value));
			// ob das gut ist?
		}
		return sb.toString();
	}

}
