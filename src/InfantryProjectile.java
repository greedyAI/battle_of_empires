
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Random;

public class InfantryProjectile extends GameTroop {
	
	public static final int SIZE = 10;
	public static double INIT_POS_X = Infantry.INIT_POS_X;
	public static double INIT_POS_Y = Infantry.INIT_POS_Y;
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 10;
	public static final int INIT_COST = 0;	
	public static final int INIT_DAMAGE = 3;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 0;
	public static final int INIT_PROJSPEED = Infantry.INIT_PROJSPEED;
	public static final int INIT_PROJDAMAGE = Infantry.INIT_PROJDAMAGE;	
	public static final int INIT_RELOAD = Infantry.INIT_RELOAD;
	public static final int INIT_RANGE = Infantry.INIT_RANGE;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_ARMORDAMAGE = 0;
	public static final int INIT_TRAINTIME = 0;
	public static final String INIT_FAVTARGET = Infantry.INIT_FAVTARGET;
	public static final boolean INIT_SPLASH = Infantry.INIT_SPLASH;
	public static final int INIT_UPKEEP = 0;
	public static final int INIT_MASS = 2;	
	public static final int INIT_MOVE = 10;
	public static final boolean INIT_CONTROL = false;
	public static LinkedList<GameTroop> projectileprojectilelist = null;
	public static final int INIT_BULLETVX = 0;
	public static final int INIT_BULLETVY = 0;
	public static double INIT_DEST_X = INIT_POS_X;
	public static double INIT_DEST_Y = INIT_POS_Y;
	public static final boolean initdestinationreached = false;



	static LinkedList<InfantryProjectile> projectilelist = new LinkedList<InfantryProjectile>();
	
	public InfantryProjectile(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_COST, INIT_DAMAGE, INIT_TYPE, INIT_REGEN, INIT_PROJSPEED, INIT_PROJDAMAGE, INIT_RELOAD, INIT_RANGE,
				INIT_ARMOR, INIT_ARMORDAMAGE, INIT_TRAINTIME, INIT_FAVTARGET, INIT_SPLASH, INIT_UPKEEP, INIT_MASS, INIT_MOVE, INIT_CONTROL, projectileprojectilelist,
				INIT_BULLETVX, INIT_BULLETVY, INIT_POS_X+SIZE/2, INIT_POS_Y+SIZE/2, initdestinationreached);
	}
	
	public static void fire() {
		int x = GameCourt.mousex + GameCourt.SCROLL_X;
		int y = GameCourt.mousey + GameCourt.SCROLL_Y;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT) {
			newInfantryProjectile();
			InfantryProjectile newprojectile = projectilelist.getLast();
			Infantry infantry = Infantry.getInfantry().get(0);
			//projectilelist.add(newprojectile);
			double unitx = infantry.pos_x + infantry.width/2;
			double unity = infantry.pos_y + infantry.height/2;
			double dx = x - unitx;
			double dy = y - unity;
			newprojectile.pos_x = unitx - newprojectile.width / 2;
			newprojectile.pos_y = unity  - newprojectile.height / 2;
			double distance = Math.sqrt(dx*dx + dy*dy);
			newprojectile.v_x = newprojectile.movespeed * dx/ distance;
			newprojectile.v_y = newprojectile.movespeed * dy/ distance;
			//System.out.println(x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT);
			//System.out.println(newprojectile.v_y);
		}
	}
	
	public static void fireAtMouse() {
		int x = GameCourt.mousex + GameCourt.SCROLL_X;
		int y = GameCourt.mousey + GameCourt.SCROLL_Y;
		newInfantryProjectile();
		InfantryProjectile newprojectile = projectilelist.getLast();
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT) {
			if (GameCourt.infantryID < Infantry.getInfantry().size()) {
			Infantry infantry = Infantry.getInfantry().get(GameCourt.infantryID);
			projectilelist.add(newprojectile);
			double unitx = infantry.pos_x + infantry.width/2;
			double unity = infantry.pos_y + infantry.height/2;
			double dx = x - unitx;
			double dy = y - unity;
			newprojectile.pos_x = unitx - newprojectile.width / 2;
			newprojectile.pos_y = unity  - newprojectile.height / 2;
			double distance = Math.sqrt(dx*dx + dy*dy);
			newprojectile.v_x = newprojectile.movespeed * dx/ distance;
			newprojectile.v_y = newprojectile.movespeed * dy/ distance;
			//System.out.println(x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT);
			//System.out.println(newprojectile.v_y);				
			}					
		}
	}
	
	public static void baseChargeCommand() {
		int x = (int) ComputerBase.INIT_POS_X + ComputerBase.SIZE/2;
		int y = (int) ComputerBase.INIT_POS_Y + ComputerBase.SIZE/2;
			for (Infantry infantry : Infantry.infantrylist) {
				if (GameCourt.PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				if (infantry.usercontrolled == false) {
				newInfantryProjectile();
				InfantryProjectile newprojectile = projectilelist.getLast();
				double unitx = infantry.pos_x + infantry.width/2;
				double unity = infantry.pos_y + infantry.height/2;
				double dx = x - unitx;
				double dy = y - unity;
				newprojectile.pos_x = unitx - newprojectile.width / 2;
				newprojectile.pos_y = unity  - newprojectile.height / 2;
				double basedistance = Math.sqrt(dx*dx + dy*dy);
				
				if (ComputerInfantry.getCPUInfantry().size()>=1 && basedistance >= 400) {
				ComputerInfantry mindistancetarget = ComputerInfantry.getCPUInfantry().get(0);
				double mindistance = 999999999;
			for (int i = 0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				ComputerInfantry target = ComputerInfantry.getCPUInfantry().get(i);
				int targetx = (int) target.pos_x + target.width/2;
				int targety = (int) target.pos_y + target.height/2;	
				double targetdx = targetx - unitx;
				double targetdy = targety - unity;
				double distance = Math.sqrt(targetdx*targetdx + targetdy*targetdy);
				if (distance < mindistance) {
					mindistance = distance;
					mindistancetarget = target;
				}
			}
			double mindx = (mindistancetarget.pos_x + mindistancetarget.width/2) - unitx;
			double mindy = (mindistancetarget.pos_y + mindistancetarget.height/2) - unity;
				newprojectile.v_x = newprojectile.movespeed * mindx/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_x;
				newprojectile.v_y = newprojectile.movespeed * mindy/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_y;	
				infantry.v_x = infantry.movespeed * dx/ basedistance;
				infantry.v_y = infantry.movespeed * dy/ basedistance;
				} else if (basedistance >= 400) {
					newprojectile.v_x = newprojectile.movespeed * dx/ basedistance;
					newprojectile.v_y = newprojectile.movespeed * dy/ basedistance;
					infantry.v_x = infantry.movespeed * dx/ basedistance;
					infantry.v_y = infantry.movespeed * dy/ basedistance;
				}
				if (basedistance < 400) {
					newprojectile.v_x = newprojectile.movespeed * dx/ basedistance;
					newprojectile.v_y = newprojectile.movespeed * dy/ basedistance;
					infantry.v_x *= 0;
					infantry.v_y *= 0;					
				}
				infantry.bulletvx = newprojectile.v_x;
				infantry.bulletvy = newprojectile.v_y;
				infantry.projectilelist.add(newprojectile);					
				}					
				}
			}
	}
	
	public static void targetClosestCommand() {
		if (ComputerInfantry.getCPUInfantry().size() >= 1) {
			for (Infantry infantry : Infantry.infantrylist) {
				if (GameCourt.PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				if (infantry.usercontrolled == false) {
				newInfantryProjectile();
				InfantryProjectile newprojectile = projectilelist.getLast();
				double unitx = infantry.pos_x + infantry.width/2;
				double unity = infantry.pos_y + infantry.height/2;
					ComputerInfantry mindistancetarget = ComputerInfantry.getCPUInfantry().get(0);
					double mindistance = 999999999;
				for (int i = 0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					ComputerInfantry target = ComputerInfantry.getCPUInfantry().get(i);
					int x = (int) target.pos_x + target.width/2;
					int y = (int) target.pos_y + target.height/2;	
					double dx = x - unitx;
					double dy = y - unity;
					double distance = Math.sqrt(dx*dx + dy*dy);
					if (distance < mindistance) {
						mindistance = distance;
						mindistancetarget = target;
					}
				}
				double mindx = (mindistancetarget.pos_x + mindistancetarget.width/2) - unitx;
				double mindy = (mindistancetarget.pos_y + mindistancetarget.height/2) - unity;
				newprojectile.pos_x = unitx - newprojectile.width / 2;
				newprojectile.pos_y = unity  - newprojectile.height / 2;
				newprojectile.v_x = newprojectile.movespeed * mindx/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_x;
				newprojectile.v_y = newprojectile.movespeed * mindy/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_y;
				if (mindistance >= 300) {
				infantry.v_x = infantry.movespeed * mindx/ mindistance;
				infantry.v_y = infantry.movespeed * mindy/ mindistance;	
				} else {
					infantry.v_x *= 0;
					infantry.v_y *= 0;					
				}
				infantry.bulletvx = newprojectile.v_x;
				infantry.bulletvy = newprojectile.v_y;
				infantry.projectilelist.add(newprojectile);				
				}					
				}					
				}			
		} else {
			baseChargeCommand();
		}				
			}
	
	
	public static void ambushClosestCommand() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (ComputerInfantry.getCPUInfantry().size() >= 1) {
			for (Infantry infantry : Infantry.infantrylist) {
				double k = Math.random();
				if (GameCourt.PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30 && k >= 0.333) {
				if (infantry.usercontrolled == false) {
				double unitx = infantry.pos_x + infantry.width/2;
				double unity = infantry.pos_y + infantry.height/2;
					ComputerInfantry mindistancetarget = ComputerInfantry.getCPUInfantry().get(0);
					double mindistance = 999999999;
				for (int i = 0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					ComputerInfantry target = ComputerInfantry.getCPUInfantry().get(i);
					int x = (int) target.pos_x + target.width/2;
					int y = (int) target.pos_y + target.height/2;	
					double dx = x - unitx;
					double dy = y - unity;
					double distance = Math.sqrt(dx*dx + dy*dy);
					if (distance < mindistance) {
						mindistance = distance;
						mindistancetarget = target;
					}
				}
				double mindx = (mindistancetarget.pos_x + mindistancetarget.width/2) - unitx;
				double mindy = (mindistancetarget.pos_y + mindistancetarget.height/2) - unity;
				if (mindistance < Math.sqrt(screenSize.getWidth()/2* screenSize.getWidth()/2 + screenSize.getHeight()/2 * screenSize.getHeight()/2)) {
				newInfantryProjectile();
				InfantryProjectile newprojectile = projectilelist.getLast();
				newprojectile.pos_x = unitx - newprojectile.width / 2;
				newprojectile.pos_y = unity  - newprojectile.height / 2;	
				newprojectile.v_x = 1.5* newprojectile.movespeed * mindx/ mindistance + (Math.random())*mindistancetarget.v_x;
				newprojectile.v_y = 1.5* newprojectile.movespeed * mindy/ mindistance + (Math.random())*mindistancetarget.v_y;	
				infantry.projectilelist.add(newprojectile);			
				infantry.bulletvx = newprojectile.v_x;
				infantry.bulletvy = newprojectile.v_y;
				}
				if (mindistance >= 300) {
				infantry.v_x = 1.5*infantry.movespeed * mindx/ mindistance;
				infantry.v_y = 1.5*infantry.movespeed * mindy/ mindistance;	
				} else {
					infantry.v_x *= 0;
					infantry.v_y *= 0;					
				}					
				}					
				}
				}			
		} else {
			baseChargeCommand();
		}				
			}	
		
	
	public static void defendBaseCommand() {
			for (Infantry infantry : Infantry.infantrylist) {
				double m = Math.random();
				if (GameCourt.PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30 && m >=0.333) {
				if (infantry.usercontrolled == false) {
				newInfantryProjectile();
				InfantryProjectile newprojectile = projectilelist.getLast();
				double unitx = infantry.pos_x + infantry.width/2;
				double unity = infantry.pos_y + infantry.height/2;
				Random generator = new Random(); 
				int j = 2* generator.nextInt(3) - 1;
				int k = 2* generator.nextInt(3) - 1;				
				double patroldx = PlayerBase.INIT_POS_X + 250*j - 375 - unitx;
				double patroldy = PlayerBase.INIT_POS_Y + 125*k + 25 - unity;				
				double patroldistance = Math.sqrt(patroldx*patroldx + patroldy*patroldy);				
				newprojectile.pos_x = unitx - newprojectile.width / 2;
				newprojectile.pos_y = unity  - newprojectile.height / 2;
				if ( ComputerInfantry.getCPUInfantry().size()>=1) {
				ComputerInfantry mindistancetarget = ComputerInfantry.getCPUInfantry().get(0);
				double mindistance = 999999999;
			for (int i = 0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				ComputerInfantry target = ComputerInfantry.getCPUInfantry().get(i);
				int targetx = (int) target.pos_x + target.width/2;
				int targety = (int) target.pos_y + target.height/2;	
				double targetdx = targetx - unitx;
				double targetdy = targety - unity;
				double distance = Math.sqrt(targetdx*targetdx + targetdy*targetdy);
				if (distance < mindistance) {
					mindistance = distance;
					mindistancetarget = target;
				}
			}
			double mindx = (mindistancetarget.pos_x + mindistancetarget.width/2) - unitx;
			double mindy = (mindistancetarget.pos_y + mindistancetarget.height/2) - unity;
				newprojectile.v_x = newprojectile.movespeed * mindx/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_x;
				newprojectile.v_y = newprojectile.movespeed * mindy/ mindistance + (2.5*Math.random()-0.5)*mindistancetarget.v_y;
				if (patroldistance >= 250) {
				infantry.v_x = infantry.movespeed * patroldx/ patroldistance;
				infantry.v_y = infantry.movespeed * patroldy/ patroldistance;	
				} else {
					infantry.v_x *= 0;
					infantry.v_y *= 0;					
				}	
				} else {
					if (patroldistance >= 250) {
						infantry.v_x = infantry.movespeed * patroldx/ patroldistance;
						infantry.v_y = infantry.movespeed * patroldy/ patroldistance;	
						} else {
							infantry.v_x *= 0;
							infantry.v_y *= 0;					
						}
				}
				
				infantry.bulletvx = newprojectile.v_x;
				infantry.bulletvy = newprojectile.v_y;
				infantry.projectilelist.add(newprojectile);					
				}					
				}
			}		
			}
	
	public static void bulletDodgeCommand() {
		Random generator = new Random();
		int j = 2 * generator.nextInt(2) - 1;
		for (Infantry infantry : Infantry.infantrylist) {
			if (infantry.usercontrolled == false) {
			double xadjust = 0;
			double yadjust = 0;
			for (CPUInfantryProjectile projectile : CPUInfantryProjectile.projectilelist) {
				if (projectile.willIntersect(infantry)) {
					xadjust += -j * projectile.v_y * 2;
					yadjust += j* projectile.v_x * 2;					
				}
			}
			if (Math.abs(xadjust) <= 2 && Math.abs(yadjust) <= 2) {
				infantry.pos_x += (4*Math.random()-2) * infantry.v_x;
				infantry.pos_y += (4*Math.random()-2) * infantry.v_y;				
			} else {
			infantry.pos_x += xadjust;
			infantry.pos_y += yadjust;				
			}				
			}
		}
	}	
	
	public static void newInfantryProjectile() {
	    InfantryProjectile newprojectile = new InfantryProjectile(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		projectilelist.add(newprojectile);
	}
		
	public static LinkedList<InfantryProjectile> getInfantryProjectile() {
		return projectilelist;
	}
	
	public void regen() {
		healthpoints = Math.min(INIT_HEALTH, healthpoints + regeneration);
	}
	
	public static void die(InfantryProjectile projectile) {
		if (projectile.healthpoints<=0 || projectile.range <=0) {
			projectilelist.remove(projectile);
		}
	}

	@Override
	public void draw(Graphics g) {
		//for (InfantryProjectile projectile: projectilelist) {
			//if (projectile.healthpoints >= 0) {
				g.setColor(Color.BLUE);
				g.fillOval((int) pos_x, (int) pos_y, width, height);
					//System.out.println(projectilelist.getLast().healthpoints);
			//}	
		//}
	}
}