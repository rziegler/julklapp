/*
 ** Copyright 2005 Huxtable.com. All rights reserved.
 */

package ch.bbv.julklapp.image;

import com.google.appengine.api.images.Image;

/**
 * A convenience class which implements those methods of BufferedImageOp which
 * are rarely changed.
 */
public abstract class AbstractImageOp {

	public Image createCompatibleDestImage(Image src) {
		if (dstCM == null)
			dstCM = src.getColorModel();
		new Buffered
		return new Image(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				dstCM.isAlphaPremultiplied(), null);
	}

	// public Rectangle2D getBounds2D(Image src) {
	// return new Rectangle(0, 0, src.getWidth(), src.getHeight());
	// }
	//
	// public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
	// if (dstPt == null)
	// dstPt = new Point2D.Double();
	// dstPt.setLocation(srcPt.getX(), srcPt.getY());
	// return dstPt;
	// }
	//
	// public RenderingHints getRenderingHints() {
	// return null;
	// }

	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to
	 * avoid the performance penalty of BufferedImage.getRGB unmanaging the
	 * image.
	 */
	public int[] getRGB(Image image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == Image.TYPE_INT_ARGB || type == Image.TYPE_INT_RGB)
			return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
		return image.getRGB(x, y, width, height, pixels, 0, width);
	}

	/**
	 * A convenience method for setting ARGB pixels in an image. This tries to
	 * avoid the performance penalty of BufferedImage.setRGB unmanaging the
	 * image.
	 */
	public void setRGB(Image image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == Image.TYPE_INT_ARGB || type == Image.TYPE_INT_RGB)
			image.getRaster().setDataElements(x, y, width, height, pixels);
		else
			image.setRGB(x, y, width, height, pixels, 0, width);
	}
}
