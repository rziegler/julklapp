package ch.bbv.julklapp.shuffle;

import java.util.List;
import java.util.Map.Entry;

public interface Shuffler<T> {
	
	List<Entry<T, T>>  shuffle(List<T> members);

}
