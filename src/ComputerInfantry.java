
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class ComputerInfantry extends GameTroop{

	
	//public static final String img_file = "Infantry.png";	
	
	private static Random generator = new Random();
	
	public static final int SIZE = 40;
	public static double INIT_POS_X = 3400 + 200*Math.random();
	public static double INIT_POS_Y = 3000 + 200*Math.random();
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 100;
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
	public static final int INIT_BULLETVX = 0;
	public static final int INIT_BULLETVY = 0;
	public static double INIT_DEST_X = INIT_POS_X;
	public static double INIT_DEST_Y = INIT_POS_Y;
	public static final boolean initdestinationreached = false;

	
	public static LinkedList<GameTroop> projectileprojectilelist = new LinkedList<GameTroop>();
	
	public static boolean rotate = false;
	
	//private static BufferedImage img;

	static LinkedList<ComputerInfantry> infantrylist = new LinkedList<ComputerInfantry>();
	
	public ComputerInfantry(int courtWidth, int courtHeight) {
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
	
	public static void newCPUInfantry() {
		INIT_POS_X = 3400 + 200*Math.random();
		INIT_POS_Y = 3000 + 200*Math.random();
		INIT_CLOCKCOUNTER = generator.nextInt(100);
	    ComputerInfantry newinfantry = new ComputerInfantry(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		infantrylist.add(newinfantry);
	}
		
	public static LinkedList<ComputerInfantry> getCPUInfantry() {
		return infantrylist;
	}
	
	public static void die(ComputerInfantry infantry) {
		if (infantry.healthpoints<=0) {
			infantrylist.remove(infantry);
		}
	}
	

	public void draw(Graphics g, int i) {
	    Graphics2D gg = (Graphics2D)g.create();

	    //width = img.getWidth();
	    //height = img.getHeight();
    	double dx = ComputerInfantry.getCPUInfantry().get(i).bulletvx;
		double dy = ComputerInfantry.getCPUInfantry().get(i).bulletvy;
		double angle = Math.atan2(dy, dx);
	    
			if (ComputerInfantry.getCPUInfantry().get(i).healthpoints>=0) {
			gg.setColor(Color.RED);
			gg.fillOval((int) pos_x, (int) pos_y, width, height);
			gg.setColor(Color.GRAY);
			Rectangle barrel = new Rectangle((int) (pos_x + width/3.2), (int) (pos_y + height*0.77), (int) (width/2.5), (int) (height*0.5));
		    if (ComputerInfantry.getCPUInfantry().get(i).projectilelist.size()>=1) {
				gg.rotate(angle-Math.PI/2, barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());

			}
		    gg.draw(barrel);
		    gg.fill(barrel);
		    
	    	gg.rotate(2 * Math.PI-(angle-Math.PI/2), barrel.getCenterX(), barrel.getCenterY()-barrel.getHeight());

			gg.setColor(new Color(60,179,113));
			gg.fillRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width * healthpoints/INIT_HEALTH, height/6, 2, 2);
			gg.setColor(Color.BLACK);
			gg.drawRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width, height/6, 2, 2);
	    }	

	}
}
