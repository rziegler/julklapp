package ch.bbv.julklapp.image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.images.Composite;
import com.google.appengine.api.images.Composite.Anchor;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class CopyOfImageTransformerTiles implements ImageTransformer {

	private static final Logger log = LoggerFactory.getLogger(CopyOfImageTransformerTiles.class);

	private static final float OFFSET_X = 0.0625f;
	private static final float OFFSET_Y = 0.0625f;

	private static final long BG_COLOR = 0x0fffffff;

	@Override
	public Image transformImage(final Image image, final int level) {
		Image transformedImage = image;

		if (level > 0) {
			transformedImage = tileImage(image);
		}
		return transformedImage;
	}

	private Image tileImage(final Image image) {
		Image transformedImage;
		int width = image.getWidth();
		int height = image.getHeight();
		List<Composite> composites = new ArrayList<Composite>();

		for (float j = 0; j < 1.0f; j += OFFSET_Y) {
			List<Composite> compositesPerRow = new ArrayList<Composite>();

			for (float i = 0; i < 1.0f; i += OFFSET_X) {
				final Image originalImage = ImagesServiceFactory.makeImage(image.getImageData());

				final float startX = i;
				final float startY = j;
				final float endX = Math.min((startX + OFFSET_X), 1.0f);
				final float endY = Math.min((startY + OFFSET_Y), 1.0f);
				log.trace(String.format("(%.1f, %.1f), (%.1f, %.1f)", startX, startY, endX, endY));

				Transform crop = ImagesServiceFactory.makeCrop(startX, startY, endX, endY);
				Image croppedImage = ImagesServiceFactory.getImagesService().applyTransform(crop, originalImage);

				Image tileImage = createTileImage(croppedImage);

				compositesPerRow.add(ImagesServiceFactory.makeComposite(tileImage, (int) Math.floor(width * i), 0,
						1.0f, Anchor.TOP_LEFT));
			}

			Image imagePerRow = ImagesServiceFactory.getImagesService().composite(compositesPerRow, image.getWidth(),
					image.getHeight(), BG_COLOR);

			composites.add(ImagesServiceFactory.makeComposite(imagePerRow, 0, (int) Math.floor(height * j), 1.0f,
					Anchor.TOP_LEFT));
		}

		transformedImage = ImagesServiceFactory.getImagesService().composite(composites, image.getWidth(),
				image.getHeight(), BG_COLOR);
		return transformedImage;
	}

	private Image createTileImage(Image croppedImage) {
		long tileColor = calculateTileColor(croppedImage);

		// create a composite with oppacity of 0.0 -> it is transparent
		Composite composite = ImagesServiceFactory.makeComposite(croppedImage, 0, 0, 0.0f, Anchor.TOP_LEFT);
		// create the tile image with the given tile color as background
		// since the composite is transparent, the tile color is seen
		Image tileImage = ImagesServiceFactory.getImagesService().composite(Arrays.asList(composite),
				croppedImage.getWidth(), croppedImage.getHeight(), tileColor);
		return tileImage;
	}

	private long calculateTileColor(Image croppedImage) {
		int[][] histogram = ImagesServiceFactory.getImagesService().histogram(croppedImage);
		int[] rgb = new int[3];

		for (int channel = 0; channel < histogram.length; channel++) {
			int[] channelValues = histogram[channel];
			int maxValuePerChannel = -1;
			int maxPositionPerChannel = -1;

			for (int position = 0; position < channelValues.length; position++) {
				int value = channelValues[position];
				if (value > maxValuePerChannel) {
					maxValuePerChannel = value;
					maxPositionPerChannel = position;
				}
			}
			log.trace("Max per channel: " + maxValuePerChannel + " at " + maxPositionPerChannel);
			rgb[channel] = maxPositionPerChannel;
		}

		String argbValue = String.format("ff%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
		log.trace("Max RGB 0x" + argbValue);
		return Long.parseLong(argbValue, 16);
	}
}
