package ch.bbv.julklapp.shuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RuthsAlgorithm<T> implements Shuffler<T> {

	@Override
	public List<Entry<T, T>> shuffle(List<T> members) {
		List<T> shuffled = new ArrayList<T>(members);
		boolean reshuffle = false;
		do {
			Collections.shuffle(shuffled);
			for (int i = 0; i < members.size(); i++) {
				T member = members.get(i);
				T wichteli = shuffled.get(i);
				if (member == wichteli) {
					reshuffle = true;
					break;
				} else {
					reshuffle = false;
				}
			}
		} while (reshuffle);
		List<Entry<T, T>> results = new ArrayList<Map.Entry<T, T>>();
		for (int i = 0; i < members.size(); i++) {
			final T member = members.get(i);
			final T wichteli = shuffled.get(i);
			results.add(new Entry<T, T>() {

				@Override
				public T getKey() {
					return member;
				}

				@Override
				public T getValue() {
					return wichteli;
				}

				@Override
				public T setValue(T value) {
					throw new IllegalStateException();
				}
			});
			
		}

		return results;

	}

}
