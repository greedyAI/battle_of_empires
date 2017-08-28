/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.util.LinkedList;

/**
 * A basic game object displayed as a yellow circle, starting in the upper left
 * corner of the game court.
 * 
 */
public class Circle extends GameTroop {

	public static final int SIZE = 20;
	public static double INIT_POS_X = 350;
	public static double INIT_POS_Y = 100;
	public static final double INIT_VEL_X = 15;
	public static final double INIT_VEL_Y = 15;
	public static final int INIT_HEALTH = 50;
	public static final int INIT_COST = 10;	
	public static final int INIT_DAMAGE = 25;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 0;
	public static final int INIT_PROJSPEED = 0;
	public static final int INIT_PROJDAMAGE = 0;	
	public static final int INIT_RELOAD = 1;
	public static final int INIT_RANGE = 0;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_CLOCKCOUNTER = 0;
	public static final int INIT_TRAINTIME = 2;
	public static final String INIT_FAVTARGET = "NONE";
	public static final boolean INIT_SPLASH = false;
	public static final int INIT_UPKEEP = 1;
	public static final int INIT_MASS = 10;	
	public static final double INIT_MOVE = 3;
	public static final boolean INIT_CONTROL = false;
	public static LinkedList<GameTroop> projectileprojectilelist = new LinkedList<GameTroop>();
	public static final int INIT_BULLETVX = 0;
	public static final int INIT_BULLETVY = 0;
	public static double INIT_DEST_X = INIT_POS_X;
	public static double INIT_DEST_Y = INIT_POS_Y;
	public static final boolean initdestinationreached = false;


	public Circle(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_COST, INIT_DAMAGE, INIT_TYPE, INIT_REGEN, INIT_PROJSPEED, INIT_PROJDAMAGE, INIT_RELOAD, INIT_RANGE,
				INIT_ARMOR, INIT_CLOCKCOUNTER, INIT_TRAINTIME, INIT_FAVTARGET, INIT_SPLASH, INIT_UPKEEP, INIT_MASS, INIT_MOVE, INIT_CONTROL, projectileprojectilelist,
				INIT_BULLETVX, INIT_BULLETVY, INIT_POS_X+SIZE/2, INIT_POS_Y+SIZE/2, initdestinationreached);
	}
	
	public void regen() {
		healthpoints = Math.min(INIT_HEALTH, healthpoints + regeneration);
	}

	@Override
	public void draw(Graphics g) {
		if (this.healthpoints!= 5000) {
		g.setColor(Color.YELLOW);
		g.fillOval((int) pos_x, (int) pos_y, width, height);
	}
	}

}
