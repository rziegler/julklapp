package ch.bbv.julklapp.image.blur;

import ch.bbv.julklapp.image.ImageFilter;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

public class GaussianBlurFilter implements ImageFilter {

	@Override
	public Image apply(Image srcImage) {
		final GaussianBlur filter = new GaussianBlur();
		byte[] transformedImageData = filter.blurImage(srcImage.getImageData());
		return ImagesServiceFactory.makeImage(transformedImageData);
	}
}
