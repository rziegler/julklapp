package ch.bbv.julklapp.image;

import com.google.appengine.api.images.Image;

public interface ImageFilter {

	Image apply(Image srcImage);
}
