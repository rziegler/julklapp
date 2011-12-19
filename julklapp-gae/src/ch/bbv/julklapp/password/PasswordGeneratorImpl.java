package ch.bbv.julklapp.password;

import java.util.Random;

public class PasswordGeneratorImpl implements PasswordGenerator{
	
	public String generatePassword(){
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 8; i++){
			int value = random.nextInt(26);
			sb.append((char)((int)'a'+value));
		}
		return sb.toString();
	}

}
