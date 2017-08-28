
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Infantry extends GameTroop{

	
	//public static final String img_file = "Infantry.png";
	public static Random generator = new Random();
	
	public static final int SIZE = 40;
	public static double INIT_POS_X = 400 + 200*Math.random();
	public static double INIT_POS_Y = 800 + 200*Math.random();
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 75;
	public static final int INIT_COST = 10;	
	public static final int INIT_DAMAGE = 20;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 0;
	public static final int INIT_PROJSPEED = 4;
	public static final int INIT_PROJDAMAGE = 5;	
	public static final int INIT_RELOAD = 1;
	public static final int INIT_RANGE = 75;
	public static final int INIT_ARMOR = 0;
	public static int INIT_CLOCKCOUNTER = generator.nextInt(100);
	public static final int INIT_TRAINTIME = 2;
	public static final String INIT_FAVTARGET = "NONE";
	public static final boolean INIT_SPLASH = false;
	public static final int INIT_UPKEEP = 1;
	public static final int INIT_MASS = 75;	
	public static final double INIT_MOVE = 3;
	public static final boolean INIT_CONTROL = false;
	public static LinkedList<GameTroop> projectileprojectilelist = new LinkedList<GameTroop>();
	public static final int INIT_BULLETVX = 0;
	public static final int INIT_BULLETVY = 0;
	public static double INIT_DEST_X = INIT_POS_X;
	public static double INIT_DEST_Y = INIT_POS_Y;
	public static final boolean initdestinationreached = false;

	
	public static int trainingcounter = -1;

	public static boolean rotate = false;
	
	//private static BufferedImage img;

	static LinkedList<Infantry> infantrylist = new LinkedList<Infantry>();
	
	public Infantry(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_COST, INIT_DAMAGE, INIT_TYPE, INIT_REGEN, INIT_PROJSPEED, INIT_PROJDAMAGE, INIT_RELOAD, INIT_RANGE,
				INIT_ARMOR, INIT_CLOCKCOUNTER, INIT_TRAINTIME, INIT_FAVTARGET, INIT_SPLASH, INIT_UPKEEP, INIT_MASS, INIT_MOVE, INIT_CONTROL, projectileprojectilelist,
				INIT_BULLETVX, INIT_BULLETVY, INIT_POS_X+SIZE/2, INIT_POS_Y+SIZE/2, initdestinationreached);
		/**try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}*/
	}
	
	public void regen() {
		healthpoints = Math.min(INIT_HEALTH, healthpoints + regeneration);
	}
	
	public static void newInfantry() {
		if (trainingcounter == -1) {
				trainingcounter = 57;
			}
	}
		
	public static LinkedList<Infantry> getInfantry() {
		return infantrylist;
	}
	
	public static void die(Infantry infantry) {
		if (infantry.healthpoints<=0) {
			infantrylist.remove(infantry);
			GameTroop.playerunitlist.remove(infantry);
			GameCourt.infantryclicked = false;
		}
	}

	public void draw(Graphics g, int i) {
	    Graphics2D gg = (Graphics2D)g.create();
	    int rotatesettings = 0;
	    //width = img.getWidth();
	    //height = img.getHeight();
	    
		double dx = GameCourt.mousex - (this.pos_x + this.width/2) + GameCourt.SCROLL_X;
		double dy = GameCourt.mousey - (this.pos_y + this.height/2) + GameCourt.SCROLL_Y;
		double angle = Math.atan2(dy, dx);
		
    	double dx1 = Infantry.getInfantry().get(i).bulletvx;
		double dy1 = Infantry.getInfantry().get(i).bulletvy;
		double angle1 = Math.atan2(dy1, dx1);
		
		if (this.healthpoints>= 0) {
			gg.setColor(Color.BLUE);
		gg.fillOval((int) pos_x, (int) pos_y, width, height);
			gg.setColor(Color.GRAY);
		Rectangle barrel = new Rectangle((int) (pos_x + width/3.2), (int) (pos_y + height*0.77), (int) (width/2.5), (int) (height*0.5));
		
			//AffineTransform tx = AffineTransform.getRotateInstance(angle-Math.PI/2 * 1.2, width/2, height/2);
			//AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			//gg.drawImage(op.filter(img, null), (int) pos_x + width/2, (int) pos_y + height/2, null);
		if (rotate == true) {
			gg.rotate(angle-Math.PI/2, barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());
			rotatesettings = 1;
		} else if (Infantry.getInfantry().get(i).projectilelist.size()>=1) {
			gg.rotate(angle1-Math.PI/2, barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());
			rotatesettings = 2;
		}
	    gg.draw(barrel);
	    gg.fill(barrel);
	    if (rotatesettings == 1) {
	    	gg.rotate(2 * Math.PI-(angle-Math.PI/2), barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());
	    } else if (rotatesettings == 2) {
			gg.rotate(2 * Math.PI-(angle1-Math.PI/2), barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());	    	
	    }
	    
		gg.setColor(new Color(60,179,113));
		gg.fillRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width * healthpoints/INIT_HEALTH, height/6, 2, 2);
		gg.setColor(Color.BLACK);
		gg.drawRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width, height/6, 2, 2);
	    
		}
	}
}
