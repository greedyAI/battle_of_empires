
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

public class ArmorRam extends GameTroop{

	
	//public static final String img_file = "Infantry.png";
	public static Random generator = new Random();
	
	public static final int SIZE = 40;
	public static double INIT_POS_X = 400 + 200*Math.random();
	public static double INIT_POS_Y = 800 + 200*Math.random();
	public static final double INIT_VEL_X = 0;
	public static final double INIT_VEL_Y = 0;
	public static int INIT_HEALTH = 540;
	public static final int INIT_COST = 20;	
	public static final int INIT_DAMAGE = 8;
	public static final String INIT_TYPE = "INF";
	public static final int INIT_REGEN = 0;
	public static final int INIT_PROJSPEED = 0;
	public static final int INIT_PROJDAMAGE = 0;	
	public static final int INIT_RELOAD = 0;
	public static final int INIT_RANGE = 0;
	public static final int INIT_ARMOR = 0;
	public static int INIT_CLOCKCOUNTER = generator.nextInt(20);
	public static final int INIT_TRAINTIME = 3;
	public static final String INIT_FAVTARGET = "NONE";
	public static final boolean INIT_SPLASH = false;
	public static final int INIT_UPKEEP = 1;
	public static final int INIT_MASS = 25;	
	public static final double INIT_MOVE = 5;
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

	static LinkedList<ArmorRam> ramlist = new LinkedList<ArmorRam>();
	
	public ArmorRam(int courtWidth, int courtHeight) {
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
	
	public static void newArmorRam() {
		if (trainingcounter == -1) {
				trainingcounter = 86;
			}
	}
		
	public static LinkedList<ArmorRam> getArmorRam() {
		return ramlist;
	}
	
	public static void die(ArmorRam ram) {
		if (ram.healthpoints<=0) {
			ramlist.remove(ram);
			GameTroop.playerunitlist.remove(ram);
			GameCourt.armorramclicked = false;
		}
	}
	
	
	public static void baseChargeCommand() {
		int x = (int) ComputerBase.INIT_POS_X + ComputerBase.SIZE/2;
		int y = (int) ComputerBase.INIT_POS_Y + ComputerBase.SIZE/2;
			for (ArmorRam ram : ArmorRam.ramlist) {
				if (GameCourt.PlayerFiringRate % 20 == ram.clockcounterarmordamage) {
				if (ram.usercontrolled == false) {
				double unitx = ram.pos_x + ram.width/2;
				double unity = ram.pos_y + ram.height/2;
				double dx = x - unitx;
				double dy = y - unity;
				double basedistance = Math.sqrt(dx*dx + dy*dy);			
					ram.v_x = ram.movespeed * dx/ basedistance;
					ram.v_y = ram.movespeed * dy/ basedistance;			
				ram.bulletvx = ram.v_x;
				ram.bulletvy = ram.v_y;				}					
				}
			}
	}
	
	public static void targetClosestCommand() {
		if (ComputerInfantry.getCPUInfantry().size() >= 1) {
			for (ArmorRam ram : ArmorRam.ramlist) {
				if (GameCourt.PlayerFiringRate % 20 == ram.clockcounterarmordamage) {
				if (ram.usercontrolled == false) {
				double unitx = ram.pos_x + ram.width/2;
				double unity = ram.pos_y + ram.height/2;
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
					ram.v_x = ram.movespeed * mindx/ mindistance;
					ram.v_y = ram.movespeed * mindy/ mindistance;					
				ram.bulletvx = ram.v_x;
				ram.bulletvy = ram.v_y;			
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
			for (ArmorRam ram : ArmorRam.ramlist) {
				double k = Math.random();
				if (GameCourt.PlayerFiringRate % 20 == ram.clockcounterarmordamage && k >= 0.333) {
				if (ram.usercontrolled == false) {
				double unitx = ram.pos_x + ram.width/2;
				double unity = ram.pos_y + ram.height/2;
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
						ram.v_x = ram.movespeed * mindx/ mindistance;
						ram.v_y = ram.movespeed * mindy/ mindistance;								
					ram.bulletvx = ram.v_x;
					ram.bulletvy = ram.v_y;
				}				
				}					
				}
				}			
		} else {
			baseChargeCommand();
		}				
			}	
		
	
	public static void defendBaseCommand() {
			for (ArmorRam ram : ArmorRam.ramlist) {
				double m = Math.random();
				if (GameCourt.PlayerFiringRate % 10 == ram.clockcounterarmordamage && m >=0.333) {
				if (ram.usercontrolled == false) {
				double unitx = ram.pos_x + ram.width/2;
				double unity = ram.pos_y + ram.height/2;
				Random generator = new Random(); 
				int j = 2* generator.nextInt(3) - 1;
				int k = 2* generator.nextInt(3) - 1;				
				double patroldx = PlayerBase.INIT_POS_X + 250*j - 375 - unitx;
				double patroldy = PlayerBase.INIT_POS_Y + 125*k + 25 - unity;				
				double patroldistance = Math.sqrt(patroldx*patroldx + patroldy*patroldy);
				if (ComputerInfantry.getCPUInfantry().size()>=1) {
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
				if (patroldistance >= 500) {
					ram.v_x = ram.movespeed * patroldx/ patroldistance;
					ram.v_y = ram.movespeed * patroldy/ patroldistance;	
				} else {
					ram.v_x = ram.movespeed * mindx/ mindistance;
					ram.v_y = ram.movespeed * mindy/ mindistance;					
				}	
				} else {
					if (patroldistance >= 300) {
					ram.v_x = ram.movespeed * patroldx/ patroldistance;
					ram.v_y = ram.movespeed * patroldy/ patroldistance;
					} else {
						ram.v_x = 0;
						ram.v_y = 0;						
					}
					
				}
				
				ram.bulletvx = ram.v_x;
				ram.bulletvy = ram.v_y;			
				}					
				}
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
		
    	double dx1 = ArmorRam.getArmorRam().get(i).bulletvx;
		double dy1 = ArmorRam.getArmorRam().get(i).bulletvy;
		double angle1 = Math.atan2(dy1, dx1);

		int[] x = {(int) pos_x+width/2, (int) (pos_x+1.2*width), (int) pos_x+width/2, (int) (pos_x-0.2*width)};
		int[] y = {(int) (pos_y-0.2*height), (int) pos_y+height/2,  (int) (pos_y+1.2*height), (int) pos_y+height/2};
		if (this.healthpoints>= 0) {
			gg.rotate((GameCourt.PlayerFiringRate + this.clockcounterarmordamage*18) * Math.PI/30, pos_x+width/2, pos_y+height/2);
			gg.setColor(Color.BLUE);
			gg.fillRect((int) pos_x, (int) pos_y, width, height);
			gg.rotate(2 * Math.PI-((GameCourt.PlayerFiringRate + this.clockcounterarmordamage*18) * Math.PI/30), pos_x+width/2, pos_y+height/2);
			gg.rotate((GameCourt.PlayerFiringRate + this.clockcounterarmordamage*18 + 45) * Math.PI/30, pos_x+width/2, pos_y+height/2);			
			gg.fillPolygon(x, y, 4);
			gg.setColor(new Color(153, 153, 153));
		gg.fillOval((int) pos_x, (int) pos_y, width, height);
		gg.setColor(Color.BLUE);
			/**gg.setColor(Color.GRAY);
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
	    }*/
	    
		gg.rotate(2 * Math.PI-((GameCourt.PlayerFiringRate + this.clockcounterarmordamage*18 + 45) * Math.PI/30), pos_x+width/2, pos_y+height/2);
		
		gg.setColor(new Color(60,179,113));
		gg.fillRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width * healthpoints/INIT_HEALTH, height/6, 2, 2);
		gg.setColor(Color.BLACK);
		gg.drawRoundRect((int) pos_x, (int) (pos_y + 1.2* height), width, height/6, 2, 2);

	    
		}
	}
}
