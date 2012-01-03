package ch.bbv.julklapp.image.blur;

import ch.bbv.julklapp.image.ImageFilter;
import ch.bbv.julklapp.image.ImageTransformer;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class ImageTransformerBlur implements ImageTransformer {

	@Override
	public Image transformImage(Image image, int level) {
		Image transformedImage = image;
		// Image compositeImage = image;
		if (level > 0) {
			Transform vertikalFlip = ImagesServiceFactory.makeVerticalFlip();
			// Composite composite =
			// ImagesServiceFactory.makeComposite(transformedImage, 10, 10,
			// 1.0f, Anchor.TOP_LEFT);
			// long color = 0x0fff0000;
			// compositeImage =
			// ImagesServiceFactory.getImagesService().composite(Arrays.asList(composite),
			// transformedImage.getWidth(), transformedImage.getHeight(),
			// color);
			// transformedImage =
			// ImagesServiceFactory.getImagesService().applyTransform(vertikalFlip,
			// image);
			ImageFilter filter = new GaussianBlurFilter();
			transformedImage = filter.apply(image);
		}
		return transformedImage;
	}

}
