
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class HeroMissile extends GameTroop {
	
	public static final int SIZE = 20;
	public static double INIT_POS_X = Hero.INIT_POS_X;
	public static double INIT_POS_Y = Hero.INIT_POS_Y;
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 10;
	public static final int INIT_COST = 0;	
	public static final int INIT_DAMAGE = 1;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 0;
	public static final int INIT_PROJSPEED = Hero.INIT_PROJSPEED;
	public static final int INIT_PROJDAMAGE = Hero.INIT_PROJDAMAGE;	
	public static final int INIT_RELOAD = Hero.INIT_RELOAD;
	public static final int INIT_RANGE = Hero.INIT_RANGE;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_ARMORDAMAGE = 0;
	public static final int INIT_TRAINTIME = 0;
	public static final String INIT_FAVTARGET = Hero.INIT_FAVTARGET;
	public static final boolean INIT_SPLASH = Hero.INIT_SPLASH;
	public static final int INIT_UPKEEP = 0;
	public static final int INIT_MASS = 5;	
	public static final int INIT_MOVE = 20;
	public static final boolean INIT_CONTROL = false;
	public static LinkedList<GameTroop> projectileprojectilelist = null;
	public static final int INIT_BULLETVX = 0;
	public static final int INIT_BULLETVY = 0;
	public static double INIT_DEST_X = INIT_POS_X;
	public static double INIT_DEST_Y = INIT_POS_Y;
	public static final boolean initdestinationreached = false;



	static LinkedList<HeroMissile> projectilelist = new LinkedList<HeroMissile>();
	//public static BufferedImage infantry;
	
	public HeroMissile(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_COST, INIT_DAMAGE, INIT_TYPE, INIT_REGEN, INIT_PROJSPEED, INIT_PROJDAMAGE, INIT_RELOAD, INIT_RANGE,
				INIT_ARMOR, INIT_ARMORDAMAGE, INIT_TRAINTIME, INIT_FAVTARGET, INIT_SPLASH, INIT_UPKEEP, INIT_MASS, INIT_MOVE, INIT_CONTROL, projectileprojectilelist,
				INIT_BULLETVX, INIT_BULLETVY, INIT_POS_X+SIZE/2, INIT_POS_Y+SIZE/2, initdestinationreached);
		/**try {
			if (infantry == null) {
				infantry = ImageIO.read(new File("Infantry.png"));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}*/
	}
	

	public static void fireAtMouse() {
		Point point = MouseInfo.getPointerInfo().getLocation();
		int x = (int) point.getX() + GameCourt.SCROLL_X;
		int y = (int) point.getY() + GameCourt.SCROLL_Y;
		newHeroMissile();
		HeroMissile newprojectile = projectilelist.getLast();
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT) {
			if (GameCourt.hero.healthpoints >=0) {
			double unitx = GameCourt.hero.pos_x + GameCourt.hero.width/2;
			double unity = GameCourt.hero.pos_y + GameCourt.hero.height/2;
			double dx = x - unitx;
			double dy = y - unity;
			newprojectile.pos_x = unitx - newprojectile.width / 2;
			newprojectile.pos_y = unity  - newprojectile.height / 2;
			double distance = Math.sqrt(dx*dx + dy*dy);
			newprojectile.v_x = newprojectile.movespeed * dx/ distance;
			newprojectile.v_y = newprojectile.movespeed * dy/ distance;
			GameCourt.hero.bulletvx = newprojectile.v_x;
			GameCourt.hero.bulletvy = newprojectile.v_y;
			GameCourt.hero.projectilelist.add(newprojectile);			
			}					
		}
	}
	
	public static void fireGuidedMissile() {
		int x = (int) ComputerBase.INIT_POS_X + ComputerBase.SIZE/2;
		int y = (int) ComputerBase.INIT_POS_Y + ComputerBase.SIZE/2;
				if (GameCourt.PlayerFiringRate % 2 == GameCourt.hero.clockcounterarmordamage % 2) {
					double unitx = GameCourt.hero.pos_x + GameCourt.hero.width/2;
					double unity = GameCourt.hero.pos_y + GameCourt.hero.height/2;
					double dx = x - unitx;
					double dy = y - unity;
					double basedistance = Math.sqrt(dx*dx + dy*dy);
					if (GameCourt.herofiredrate >=2) {
				newHeroMissile();
				GameCourt.herofiredrate -=2;
				HeroMissile newestprojectile = projectilelist.getLast();
				newestprojectile.pos_x = unitx - newestprojectile.width / 2;
				newestprojectile.pos_y = unity  - newestprojectile.height / 2;
					}
				for (HeroMissile newprojectile : projectilelist) {
				
				if (ComputerInfantry.getCPUInfantry().size()>=1 && basedistance >= 400) {
				ComputerInfantry mindistancetarget = ComputerInfantry.getCPUInfantry().get(0);
				double mindistance = 999999999;
			for (int i = 0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				ComputerInfantry target = ComputerInfantry.getCPUInfantry().get(i);
				int targetx = (int) target.pos_x + target.width/2;
				int targety = (int) target.pos_y + target.height/2;	
				double targetdx = targetx - (newprojectile.pos_x + newprojectile.width/2);
				double targetdy = targety - (newprojectile.pos_y + newprojectile.height/2);
				double distance = Math.sqrt(targetdx*targetdx + targetdy*targetdy);
				if (distance < mindistance) {
					mindistance = distance;
					mindistancetarget = target;
				}
			}
			double mindx = (mindistancetarget.pos_x + mindistancetarget.width/2) - (newprojectile.pos_x + newprojectile.width/2);
			double mindy = (mindistancetarget.pos_y + mindistancetarget.height/2) - (newprojectile.pos_y + newprojectile.height/2);
				newprojectile.v_x = newprojectile.movespeed * mindx/ mindistance;
				newprojectile.v_y = newprojectile.movespeed * mindy/ mindistance;
				//GameCourt.hero.v_x = GameCourt.hero.movespeed * mindx/ mindistance;
				//GameCourt.hero.v_y = GameCourt.hero.movespeed * mindy/ mindistance;
				//GameCourt.hero.v_x = GameCourt.hero.movespeed * dx/ basedistance;
				//GameCourt.hero.v_y = GameCourt.hero.movespeed * dy/ basedistance;
				} else if (basedistance >= 400) {
					newprojectile.v_x = newprojectile.movespeed * dx/ basedistance;
					newprojectile.v_y = newprojectile.movespeed * dy/ basedistance;
					//GameCourt.hero.v_x = GameCourt.hero.movespeed * dx/ basedistance;
					//GameCourt.hero.v_y = GameCourt.hero.movespeed * dy/ basedistance;
				}
				if (basedistance < 400) {
					newprojectile.v_x = newprojectile.movespeed * dx/ basedistance;
					newprojectile.v_y = newprojectile.movespeed * dy/ basedistance;	
					//GameCourt.hero.v_x = 0;
					//GameCourt.hero.v_y = 0;
				}
				GameCourt.hero.bulletvx = newprojectile.v_x;
				GameCourt.hero.bulletvy = newprojectile.v_y;
				GameCourt.hero.projectilelist.add(newprojectile);					
				}

					
				}					
	}
	
	
	public static void newHeroMissile() {
	    HeroMissile newprojectile = new HeroMissile(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		projectilelist.add(newprojectile);
	}
		
	public static LinkedList<HeroMissile> getHeroMissile() {
		return projectilelist;
	}
	
	public void regen() {
		healthpoints = Math.min(INIT_HEALTH, healthpoints + regeneration);
	}
	
	public static void die(HeroMissile projectile) {
		if (projectile.healthpoints<=0 || projectile.range <=0) {
			projectilelist.remove(projectile);
		}
	}

	@Override
	public void draw(Graphics g) {
		//for (InfantryProjectile projectile: projectilelist) {
			//if (healthpoints >= 10) {
				g.setColor(Color.BLUE);
				//g.drawImage(infantry, (int) pos_x, (int) pos_y, null);
				g.fillOval((int) pos_x, (int) pos_y, width, height);
					//System.out.println(projectilelist.getLast().healthpoints);
			//}
		//}
	}
}