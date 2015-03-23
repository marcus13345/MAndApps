package MAndApps.apps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import MAndEngine.AppHelper;
import MAndEngine.BasicApp;
import MAndEngine.Engine;
import MAndEngine.Variable;
import static MAndEngine.Engine.ANIMATION_CONSTANT;

public class MainMenu implements BasicApp {

	public static Variable wallpaperPath;
	
	private static Image BKG;
	private Color GREY, GREEN, WHITE, BLACK;
	private int xMargin = 300, yMargin = 303, yGap = 50, selection = 0;
	private double yOffset = 0;
	private Color[] appColors;
	private String[] appNames;
	private int visibleApps;
	private double backgroundScroll = 0;
	private int backgroundRange = 0;

	@Override
	public Dimension getResolution() {
		// TODO Auto-generated method stub
		return new Dimension(800, 600);
	}

	/**
	 * THIS PART STARTS STUFF!
	 * WOO!
	 */
	@Override
	public void initialize() {
		
		if(wallpaperPath == null) wallpaperPath =  new Variable("MAndWorks\\MAndApps\\Settings", "wallpaper", "http://www.controltheweb.com/images/desktop-background-large/milkyWay.jpg", true);

		//COLORS! Because ... wait actually why?
		//we don't use the colors.
		GREY = new Color(50, 50, 50);
		GREEN = new Color(137, 198, 35);
		WHITE = new Color(200, 200, 200);
		BLACK = new Color(10, 10, 10);
		
		//WE HAVE APPS THAT ARE LIKE ... VISIBLE. LETS COUNT THEM
		visibleApps = 0;
		
		for (int i = 0, app = 0; app < Engine.apps.length; i++, app++) {
			if (Engine.apps[app].visibleInMenu()) {
				//oneee......
				//wat no...
				//i.......
				visibleApps++;
			} else {
				i--;
			}
		}

		//these are names, because we can't render null now can we timmy.
		//yeah he fucked up the basic course in Graphics2D... 
		//timmy was me
		//2 days ago.
		appNames = new String[visibleApps];
		
		appColors = new Color[visibleApps];

		//loopshit. what are we looping? 
		//FILL THE ARRAYS WITH ALLLLL LTHE DATA!
		for (int i = 0, app = 0; app < Engine.apps.length; i++, app++) {
			if (Engine.apps[app].visibleInMenu()) {
				appNames[i] = Engine.apps[app].getTitle();
				appColors[i] = Engine.apps[app].getColor();
			} else {
				i--;
			}
		}

		backgroundScroll = 0;
		
		try {
			BufferedImage image = ImageIO.read(new URL(wallpaperPath.getValue()));
			image = getScaledImage(image, 800);
			BKG = image;
			backgroundRange = image.getHeight() - 600;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pauseApp() {
		//why the actual fuck would we pause the main. fucking. menu.
	}
	
	@Override
	public void resumeApp() {
		//see pause.
	}

	@Override
	public void tick() {
		int desiredYOffset = selection * yGap;
		yOffset -= (yOffset - desiredYOffset) / ANIMATION_CONSTANT;

		int desiredBackgroundScroll = (int) (backgroundRange * ((double)selection/(visibleApps - 1)));
		backgroundScroll -= ((double)backgroundScroll - desiredBackgroundScroll)/ANIMATION_CONSTANT;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(BKG, 0, (int) (0 - backgroundScroll), null);

		g.setColor(appColors[selection]);
		g.fillRect(0, 290, 800, 20);
		g.setColor(BLACK);
		g.fillRect(0, 291, 800, 18);
		g.setColor(WHITE);

		final int clearMargin = 190;
		final int range = 100;

		for (int i = 0; i < visibleApps; i++) {

			int y = yMargin + (i * yGap) - (int) yOffset;
			float opacity = y;
			opacity -= clearMargin;
			opacity = opacity < 0 ? 0 : opacity;

			opacity /= range;

			opacity = opacity > 1 ? 1 : opacity;

			g.setColor(createColor(WHITE, opacity));

			g.drawString(appNames[i], xMargin, y);
		}
		
		g.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 30));
		
		g.setColor(Color.WHITE);
		g.drawString("MAndApps", 300, 100);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'w')
			if (selection > 0)
				selection--;
			else
				selection = visibleApps - 1; 
		if (e.getKeyChar() == 's')
			if (selection < visibleApps - 1)
				selection++;
			else
				selection = 0;
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			int openApp = AppHelper.getIDbyName(appNames[selection]);
			Engine.switchApps(openApp);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//WE DUNN DO SHIT.
	}

	@Override
	public String getTitle() {
		return "Main Menu";
	}

	@Override
	public Color getColor() {
		return new Color(255, 255, 255);
	}

	@Override
	public int getFramerate() {
		return 50;
	}

	@Override
	public boolean getResizable() {
		return false;
	}

	@Override
	public boolean visibleInMenu() {
		return false;
	}

	/**
	 * im sorry about creating a new color object every tick. <br/>
	 * jk, #soznosoz
	 * 
	 * @param base
	 * @param opacity
	 * @return
	 */
	public static Color createColor(Color base, float opacity) {
		int r = base.getRed();
		int g = base.getGreen();
		int b = base.getBlue();
		int a = (int) (opacity * 255);
		return new Color(r, g, b, a);
	}
	
	public static BufferedImage getScaledImage(BufferedImage image, int width) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleX);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, (int)(imageHeight * scaleX), image.getType()));
	}

	@Override
	public void resized(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void click() {
		// TODO Auto-generated method stub
		
	}

}