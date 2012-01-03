package ch.bbv.julklapp.image.blur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLine;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;

public class GaussianBlur {

	private ImageInfo imageInfo;

	private Rgb[][] rgbData;

	private final double[][] kernel = {
			{ 0.00000067, 0.00002292, 0.00019117, 0.00038771, 0.00019117, 0.00002292, 0.00000067 },
			{ 0.00002292, 0.00078633, 0.00655965, 0.01330373, 0.00655965, 0.00078633, 0.00002292 },
			{ 0.00019117, 0.00655965, 0.05472157, 0.11098164, 0.05472157, 0.00655965, 0.00019117 },
			{ 0.00038771, 0.01330373, 0.11098164, 0.22508352, 0.11098164, 0.01330373, 0.00038771 },
			{ 0.00019117, 0.00655965, 0.05472157, 0.11098164, 0.05472157, 0.00655965, 0.00019117 },
			{ 0.00002292, 0.00078633, 0.00655965, 0.01330373, 0.00655965, 0.00078633, 0.00002292 },
			{ 0.00000067, 0.00002292, 0.00019117, 0.00038771, 0.00019117, 0.00002292, 0.00000067 } };

	static class Rgb {
		public Rgb(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public Rgb() {

		}

		int r;
		int g;
		int b;
	}

	public byte[] blurImage(byte[] rawImageData) {

		try {
			loadImage(rawImageData);
			doBlur();
			return getFilteredImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void doBlur() {
		Rgb[][] result = new Rgb[imageInfo.cols][imageInfo.rows];
		// find center position of kernel (half of kernel size)
		int kCenterX = kernel.length / 2;
		int kCenterY = kernel[0].length / 2;

		for (int i = 0; i < imageInfo.rows; ++i) // rows
		{
			for (int j = 0; j < imageInfo.cols; ++j) // columns
			{
				Rgb out = new Rgb();
				result[j][i] = out;

				for (int m = 0; m < kernel[0].length; ++m) // kernel rows
				{
					int mm = kernel[0].length - 1 - m; // row index of flipped
														// kernel

					for (int n = 0; n < kernel.length; ++n) // kernel columns
					{
						int nn = kernel.length - 1 - n; // column index of
														// flipped kernel

						// index of input signal, used for checking boundary
						int ii = i + (m - kCenterY);
						int jj = j + (n - kCenterX);

						// ignore input samples which are out of bound
						if (ii >= 0 && ii < imageInfo.rows && jj >= 0 && jj < imageInfo.cols) {
							Rgb origin = rgbData[jj][ii];
							out.r += origin.r * kernel[nn][mm];
							out.g += origin.g * kernel[nn][mm];
							out.b += origin.b * kernel[nn][mm];
						}
					}
				}
			}
		}
		rgbData = result;
	}

	private void loadImage(byte[] rawImage) throws IOException {
		PngReader pngr = new PngReader(new ByteArrayInputStream(rawImage));
		imageInfo = pngr.imgInfo;
		int channels = pngr.imgInfo.channels;
		if (channels < 3)
			throw new RuntimeException("Only for truecolour images");
		rgbData = new Rgb[pngr.imgInfo.cols][pngr.imgInfo.rows];

		for (int row = 0; row < pngr.imgInfo.rows; row++) {
			ImageLine l1 = pngr.readRow(row);
			for (int j = 0; j < pngr.imgInfo.cols; j++) {
				rgbData[j][row] = new Rgb( //
						l1.scanline[j * channels], //
						l1.scanline[j * channels + 1],//
						l1.scanline[j * channels + 2]);
			}
		}
		pngr.end();
	}

	private byte[] getFilteredImage() throws FileNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PngWriter pngw = new PngWriter(baos, imageInfo);
		for (int row = 0; row < imageInfo.rows; row++) {
			ImageLine l1 = new ImageLine(imageInfo);
			l1.setRown(row);
			for (int j = 0; j < imageInfo.cols; j++) {
				Rgb rgb = rgbData[j][row];
				l1.scanline[j * 3] = rgb.r; //
				l1.scanline[j * 3 + 1] = rgb.g;//
				l1.scanline[j * 3 + 2] = rgb.b;
			}
			pngw.writeRow(l1);
		}
		pngw.end();
		return baos.toByteArray();
	}
}
