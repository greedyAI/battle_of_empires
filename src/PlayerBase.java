
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PlayerBase extends GameBase{

	
	//public static final String img_file = "Infantry.png";

	public static final int SIZE = 200;
	public static final double INIT_POS_X = 600;
	public static final double INIT_POS_Y = 600;
	public static int INIT_HEALTH = 10000;
	public static final int INIT_UPCOST = 10000;	
	public static final int INIT_MELEEDAMAGE = 5;
	public static final String INIT_TYPE = "PLAYER";
	public static final int INIT_REGEN = 0;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_ARMORDAMAGE = 0;

	
	public static BufferedImage ambushAttack;
	public static BufferedImage baseCharge;
	public static BufferedImage baseDefend;
	public static BufferedImage dodgeBullet;
	public static BufferedImage generalAttack;
	public static BufferedImage resetCommand;
	public static BufferedImage autohero;
	public static BufferedImage manualhero;
	
	public PlayerBase(int courtWidth, int courtHeight) {
		super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_UPCOST, INIT_TYPE, INIT_REGEN, INIT_ARMOR, INIT_ARMORDAMAGE, INIT_MELEEDAMAGE);
		try {
			if (ambushAttack == null) {
				ambushAttack = ImageIO.read(new File("ambushAttack.png"));
			}
			if (baseCharge == null) {
				baseCharge = ImageIO.read(new File("baseCharge.png"));
			}
			if (baseDefend == null) {
				baseDefend = ImageIO.read(new File("baseDefend.png"));
			}
			if (dodgeBullet == null) {
				dodgeBullet = ImageIO.read(new File("dodgeBullet.png"));
			}
			if (generalAttack == null) {
				generalAttack = ImageIO.read(new File("generalAttack.png"));
			}
			if (resetCommand == null) {
				resetCommand = ImageIO.read(new File("resetCommand.png"));
			}
			if (autohero == null) {
				autohero = ImageIO.read(new File("AutoHero.png"));
			}
			if (manualhero == null) {
				manualhero = ImageIO.read(new File("ManualHero.png"));
			}	
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	public void regen() {
		healthpoints = Math.min(INIT_HEALTH, healthpoints + regeneration);
	}
	
	//public static void newBase() {
	    //PlayerBase newbase = new PlayerBase(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
	//}
	

		

	@Override
	public void draw(Graphics g) {
		if (this.healthpoints>= 0) {
		g.setColor(Color.BLUE);
		g.fillRect((int) pos_x, (int) pos_y, width, height);
		if (GameCourt.playerbaseclicked == true) {
			g.setColor(Color.BLACK);
			g.drawRect((int) pos_x, (int) pos_y, width, height);
			g.drawImage(baseDefend, (int) pos_x, (int) pos_y, null);
			g.drawImage(generalAttack, (int) pos_x + 50, (int) pos_y, null);
			g.drawImage(baseCharge, (int) pos_x + 100, (int) pos_y , null);		
			g.drawImage(ambushAttack, (int) pos_x + 150, (int) pos_y, null);
			g.drawImage(dodgeBullet, (int) pos_x + 50, (int) pos_y + 100, null);
			g.drawImage(resetCommand, (int) pos_x + 100, (int) pos_y + 100, null);
			g.drawImage(manualhero, (int) pos_x + 50, (int) pos_y + 50, null);
			g.drawImage(autohero, (int) pos_x + 100, (int) pos_y + 50, null);
			Point point = MouseInfo.getPointerInfo().getLocation();
			int x = (int) point.getX() + GameCourt.SCROLL_X;
			int y = (int) point.getY() + GameCourt.SCROLL_Y;
			if (x >607 && x<=607+baseDefend.getWidth() && y >628 && y <=628+baseDefend.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Defend Base", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Commands all troops to", 605, 775);
				g.drawString("immediately return to base", 605, 785);
				g.drawString("and encircle it in defense", 605, 795);
			} else if (x >657 && x<=657+generalAttack.getWidth() && y >628 && y <=628+generalAttack.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("General Attack", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Commands all troops to", 605, 775);
				g.drawString("pursue and attack all enemy", 605, 785);
				g.drawString("units on the battlefield", 605, 795);
			} else if (x >707 && x<=707+baseCharge.getWidth() && y >628 && y <=628+baseCharge.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Attack Base", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Commands all troops to seek", 605, 775);
				g.drawString("out the enemy base and destroy", 605, 785);
				g.drawString("it and any units blocking it", 605, 795);
			} else if (x >757 && x<=757+ambushAttack.getWidth() && y >628 && y <=628+ambushAttack.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Ambush Attack", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Commands all troops to seek", 605, 775);
				g.drawString("out the closest enemies and", 605, 785);
				g.drawString("ambush them at close quarters", 605, 795);				
			} else if (x >657 && x<=657+baseCharge.getWidth() && y >678 && y <=678+baseCharge.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Manual Hero Control", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Control your hero manually,", 605, 775);
				g.drawString("using the WASD keys to move", 605, 785);
				g.drawString("and mouse key to fire missiles", 605, 795);
			} else if (x >707 && x<=707+ambushAttack.getWidth() && y >678 && y <=678+ambushAttack.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Auto-Hero Control", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Your hero will charge towards", 605, 775);
				g.drawString("the enemy base and fire missiles", 605, 785);
				g.drawString("at all enemies in its way", 605, 795);
			} else if (x >657 && x<=657+dodgeBullet.getWidth() && y >728 && y <=728+dodgeBullet.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Skill: Dodge Enemy Fire", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("For 10 seconds, all friendly troops", 605, 775);
				g.drawString("will attempt to dodge enemy fire", 605, 785);
				g.drawString("with about 50% success rate", 605, 795);
			} else if (x >707 && x<=707+resetCommand.getWidth() && y >728 && y <=728+resetCommand.getHeight()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 16));
				g.drawString("Reset Commands", 605, 765);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				g.drawString("Resets all general commands that", 605, 775);
				g.drawString("you may have issued your troops.", 605, 785);
				g.drawString("Troops will stop automove/autofire.", 605, 795);
			}
			if (GameCourt.playercommandmode == 1) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 600, (int) 600, baseDefend.getWidth(), baseDefend.getHeight());
				g.drawImage(baseDefend, (int) pos_x, (int) pos_y, null);				
				} else if (GameCourt.playercommandmode == 2) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 650, (int) 600, generalAttack.getWidth(), generalAttack.getHeight());
				g.drawImage(generalAttack, (int) pos_x + 50, (int) pos_y, null);				
				} else if (GameCourt.playercommandmode == 3) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 700, (int) 600, baseCharge.getWidth(), baseCharge.getHeight());
				g.drawImage(baseCharge, (int) pos_x + 100, (int) pos_y, null);			
				} else if (GameCourt.playercommandmode == 4) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 750, (int) 600, ambushAttack.getWidth(), ambushAttack.getHeight());					
				g.drawImage(ambushAttack, (int) pos_x + 150, (int) pos_y, null);					
				} else if (GameCourt.playercommandmode == 0) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 700, (int) 700, resetCommand.getWidth(), resetCommand.getHeight());									
				g.drawImage(resetCommand, (int) pos_x + 100, (int) pos_y + 100, null);			
				}
			if (GameCourt.dodgeBullet == true) {
			g.setColor(Color.GRAY);
			g.fillRect((int) 650, (int) 700, dodgeBullet.getWidth(), dodgeBullet.getHeight());									
			g.drawImage(dodgeBullet, (int) pos_x + 50, (int) pos_y + 100, null);
				}
			
			if (GameCourt.heromode == 1) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 650, (int) 650, manualhero.getWidth(), manualhero.getHeight());									
				g.drawImage(manualhero, (int) pos_x + 50, (int) pos_y + 50, null);
			} else if (GameCourt.heromode == 2) {
				g.setColor(Color.GRAY);
				g.fillRect((int) 700, (int) 650, autohero.getWidth(), autohero.getHeight());									
				g.drawImage(autohero, (int) pos_x + 100, (int) pos_y + 50, null);
					}
			
			g.setColor(Color.BLACK);
			g.drawRoundRect((int) 600, (int) 600, baseDefend.getWidth(), baseDefend.getHeight(), 2, 2);
			g.drawRoundRect((int) 650, (int) 600, generalAttack.getWidth(), generalAttack.getHeight(), 2, 2);
			g.drawRoundRect((int) 700, (int) 600, baseCharge.getWidth(), baseCharge.getHeight(), 2, 2);
			g.drawRoundRect((int) 750, (int) 600, ambushAttack.getWidth(), ambushAttack.getHeight(), 2, 2);
			g.drawRoundRect((int) 650, (int) 700, dodgeBullet.getWidth(), dodgeBullet.getHeight(), 2, 2);
			g.drawRoundRect((int) 700, (int) 700, resetCommand.getWidth(), resetCommand.getHeight(), 2, 2);
			g.drawRoundRect((int) 650, (int) 650, manualhero.getWidth(), manualhero.getHeight(), 2, 2);
			g.drawRoundRect((int) 700, (int) 650, autohero.getWidth(), autohero.getHeight(), 2, 2);			
			} else {
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 24));
			if (GameCourt.lowcashwarning == false) {
				g.drawString("Money: " + Integer.toString(GameCourt.playercash), 605, 630);
			} else {
				g.setColor(Color.RED);
				g.drawString("Money: " + Integer.toString(GameCourt.playercash), 605, 630);
				GameCourt.lowcashwarning = false;
			}
			g.setColor(Color.BLACK);
			g.drawString("Health: " + Integer.toString(healthpoints), 605, 770);
			g.setColor(new Color(60,179,113));
			g.fillRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10) * healthpoints/INIT_HEALTH, 20, 10, 10);
			g.setColor(Color.BLACK);
			g.drawRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10), 20, 10, 10);
			}
		}
	}
}