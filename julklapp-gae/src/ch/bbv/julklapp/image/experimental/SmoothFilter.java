package ch.bbv.julklapp.image.experimental;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.filters.Smooth;

public class SmoothFilter {

	public void smooth() {
		Smooth filter = new Smooth(Jimi.getImageProducer(""), 2);
		// filter.
	}
}
