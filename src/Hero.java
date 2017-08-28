
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

public class Hero extends GameTroop{

	
	//public static final String img_file = "Infantry.png";
	public static Random generator = new Random();
	
	public static final int SIZE = 60;
	public static double INIT_POS_X = 400 + 200*Math.random();
	public static double INIT_POS_Y = 800 + 200*Math.random();
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 500;
	public static final int INIT_COST = 150;	
	public static final int INIT_DAMAGE = 50;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 3;
	public static final int INIT_PROJSPEED = 10;
	public static final int INIT_PROJDAMAGE = 25;	
	public static final int INIT_RELOAD = 2;
	public static final int INIT_RANGE = 100         ;
	public static final int INIT_ARMOR = 0;
	public static int INIT_CLOCKCOUNTER = generator.nextInt(100);
	public static final int INIT_TRAINTIME = 10;
	public static final String INIT_FAVTARGET = "NONE";
	public static final boolean INIT_SPLASH = false;
	public static final int INIT_UPKEEP = 1;
	public static final int INIT_MASS = 50;	
	public static final double INIT_MOVE = 4;
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

	
	public Hero(int courtWidth, int courtHeight) {
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
	
	public static void newHero() {
		if (trainingcounter == -1) {
				trainingcounter = 343;
			}
	}
	
	public static void die(Hero hero) {
		if (hero.healthpoints<0) {
			GameTroop.playerunitlist.remove(hero);
		}
	}

	public void draw(Graphics g) {
	    Graphics2D gg = (Graphics2D)g.create();
	    int rotatesettings = 0;
	    //width = img.getWidth();
	    //height = img.getHeight();
	    
		double dx = GameCourt.mousex - (this.pos_x + this.width/2) + GameCourt.SCROLL_X;
		double dy = GameCourt.mousey - (this.pos_y + this.height/2) + GameCourt.SCROLL_Y;
		double angle = Math.atan2(dy, dx);
		
    	double dx1 = GameCourt.hero.bulletvx;
		double dy1 = GameCourt.hero.bulletvy;
		double angle1 = Math.atan2(dy1, dx1);
		
		if (this.healthpoints>= 0) {				

		Rectangle barrel1 = new Rectangle((int) (pos_x + width/3.2), (int) (pos_y + height*0.77), (int) (width/2.5), (int) (height*0.5));
		Rectangle barrel2 = new Rectangle((int) (pos_x + width/3.2), (int) (pos_y - height*0.27), (int) (width/2.5), (int) (height*0.5));
		
		if (GameCourt.heromode == 1) {
			gg.rotate(angle1-Math.PI/2, barrel1.getCenterX(), barrel1.getCenterY()-barrel1.getHeight());
			rotatesettings = 1;
		} else if (GameCourt.heromode == 2) {
			gg.rotate(angle1-Math.PI/2, barrel1.getCenterX(), barrel1.getCenterY()-barrel1.getHeight());
			rotatesettings = 2;
		}
		
		gg.setColor(Color.YELLOW);
		gg.fillArc((int) (pos_x+GameCourt.hero.width/6.6),(int) (pos_y+GameCourt.hero.height/2), (int) (GameCourt.hero.width*0.75), (int) (GameCourt.hero.height*0.75),210, 120);
		gg.fillArc((int) (pos_x+GameCourt.hero.width/6.6),(int) (pos_y-GameCourt.hero.height*0.5/2), (int) (GameCourt.hero.width*0.75), (int) (GameCourt.hero.height*0.75),30, 120);
			
		gg.setColor(new Color(153, 153, 153));
	    gg.draw(barrel1);
	    gg.fill(barrel1);
	    gg.draw(barrel2);
	    gg.fill(barrel2);
	    gg.setColor(Color.BLUE);
		gg.fillOval((int) pos_x, (int) pos_y, width, height);
		
	    if (rotatesettings == 1) {
	    	gg.rotate(2 * Math.PI-(angle1-Math.PI/2), barrel1.getCenterX(), barrel1.getCenterY()-barrel1.getHeight());
	    } else if (rotatesettings == 2) {
			gg.rotate(2 * Math.PI-(angle1-Math.PI/2), barrel1.getCenterX(), barrel1.getCenterY()-barrel1.getHeight());	    	
	    }
	    
		gg.setColor(new Color(60,179,113));
		gg.fillRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width * healthpoints/INIT_HEALTH, height/6, 2, 2);
		gg.setColor(Color.BLACK);
		gg.drawRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width, height/6, 2, 2);
	    
		}
	}
}
