package ch.bbv.julklapp.image;

import com.google.appengine.api.images.Image;

public interface ImageTransformer {

	Image transformImage(Image image, int level);
}
