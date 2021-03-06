package MAndApps.apps.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Item {

	private static int scaleToHeight;
	
	//cropped to a thumb nail, of size 80*80
	private final BufferedImage thumbnail;
	
	//the original image, not cropped or anything.
	private final BufferedImage image;
	
	//Absolute path, i think.
	private final String path;
	private String name;

	// we need this because im scared.
	// if literally anything in the creating of an item
	// goes MODERATELY wrong, you set this to FALSE.
	// then we don't try and render something that
	// makes no sense.
	// set this to false by the end of the constructor
	// and when it comes back to being added to the list,
	// it'll be thrown out.
	private boolean seemsLegit = true;

	public Item(String path) {

		BufferedImage thumbnail = null;
		BufferedImage image = null;

		try {
			File file = new File(path);
			if (file.isDirectory()) {
				// then lets do the directory thing!

				thumbnail = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
				Graphics thumbnailGraphics = thumbnail.getGraphics();
				thumbnailGraphics.setColor(new Color(0, 0, 255));
				thumbnailGraphics.fillRect(0, 0, 80, 80);
				
				image = new BufferedImage((int)((scaleToHeight/3d)*4d), scaleToHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics imageGraphics = image.getGraphics();
				imageGraphics.fillRect(0, 0, (int)((scaleToHeight/3d)*4d), scaleToHeight);
				
				imageGraphics.setColor(Color.black);
				imageGraphics.drawString("" + path, 100, 100);
				
				name = file.getName();
				
			} else {

				// try and do the image thing!
				image = ImageIO.read(file);
				image = getScaledImage(image, scaleToHeight);
				thumbnail = (getScaledImageFill(image, 80, 80));
				path = file.getAbsolutePath();

				// discard it as it won't fit the screen.
				if (!(((double) image.getWidth() / image.getHeight()) <= (4 / 3d))) {
					seemsLegit = false;
				}

				name = "";
			}
		} catch (Exception e) {
			seemsLegit = false;
		}
		
		this.image = image;
		this.path = path;
		this.thumbnail = thumbnail;
	}
	
	public boolean getSeemsLegit() {
		return seemsLegit;
	}

	private static BufferedImage getScaledImage(BufferedImage image, int height) throws IOException {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		double scale = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		return bilinearScaleOp.filter(image, new BufferedImage((int) (imageWidth * scale), height, image.getType()));
	}

	private static BufferedImage getScaledImageFill(BufferedImage image, int width, int height) throws IOException {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		double scale = scaleX > scaleY ? scaleX : scaleY;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getThumbnail() {
		return thumbnail;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}
}