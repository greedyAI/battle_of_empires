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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Barracks extends GameBase{

	
	//public static final String img_file = "Infantry.png";

	public static final int SIZE = 200;
	public static final double INIT_POS_X = 400;
	public static final double INIT_POS_Y = 600;
	public static int INIT_HEALTH = 5000;
	public static final int INIT_UPCOST = 7500;	
	public static final int INIT_MELEEDAMAGE = 20;
	public static final String INIT_TYPE = "PLAYER";
	public static final int INIT_REGEN = 0;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_ARMORDAMAGE = 0;

	
	public static BufferedImage infantry;
	public static BufferedImage ram;
	public static BufferedImage hero;
	
	public Barracks(int courtWidth, int courtHeight) {
		super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_UPCOST, INIT_TYPE, INIT_REGEN, INIT_ARMOR, INIT_ARMORDAMAGE, INIT_MELEEDAMAGE);
		try {
			if (infantry == null) {
				infantry = ImageIO.read(new File("Infantry.png"));
			}
			if (ram == null) {
				ram = ImageIO.read(new File("Ram.png"));
			}
			if (hero == null) {
				hero = ImageIO.read(new File("Hero.png"));
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
			g.setColor(Color.ORANGE);
			g.fillRect((int) pos_x, (int) pos_y, width, height);
			if (GameCourt.barracksclicked == true) {
				g.setColor(Color.BLACK);
				g.drawRect((int) pos_x, (int) pos_y, width, height);
				g.drawImage(infantry, (int) pos_x, (int) pos_y, null);
				g.drawImage(ram, (int) pos_x+infantry.getWidth(), (int) pos_y, null);
				g.drawImage(hero, (int) pos_x+infantry.getWidth()+ram.getWidth(), (int) pos_y, null);				
			}
			if (GameCourt.barracksclicked == true) {
				g.setColor(Color.BLACK);
				g.drawRoundRect((int) 400, (int) 600, infantry.getWidth(), infantry.getHeight(), 2, 2);
				g.drawRoundRect((int) 400+infantry.getWidth(), (int) 600, ram.getWidth(), ram.getHeight(), 2, 2);
				g.drawRoundRect((int) 400+infantry.getWidth()+ram.getWidth(), (int) 600, hero.getWidth(), hero.getHeight(), 2, 2);				
				//if (GameCourt.barracksmousemode == 0) {
				g.setColor(Color.GRAY);
				g.fillArc(400,600,infantry.getWidth(), infantry.getHeight(),90, (Infantry.trainingcounter +1) *360/57);	
				g.fillArc(400+infantry.getWidth(),600,ram.getWidth(), ram.getHeight(),90, (ArmorRam.trainingcounter +1) *360/86);
				g.fillArc(400+infantry.getWidth()+ram.getWidth(),600,hero.getWidth(), hero.getHeight(),90, (Hero.trainingcounter +1) *360/343);
				//}
				Point point = MouseInfo.getPointerInfo().getLocation();
				int x = (int) point.getX() + GameCourt.SCROLL_X;
				int y = (int) point.getY() + GameCourt.SCROLL_Y;
				if (x >407 && x<=407+infantry.getWidth() && y >628 && y <=628+infantry.getHeight()) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("TimesRoman", Font.BOLD, 16));
					g.drawString("Basic Infantry: Mark I", 405, 730);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
					g.drawString("Type: Ground Single Target", 405, 740);
					g.drawString("Cost: 10     HP: 50     Speed: 3", 405, 750);
					g.drawString("Melee Damage: 20     Range Damage: 5", 405, 760);
					g.drawString("Regen: 0     Reload: 1     Armor: 0", 405, 770);
					g.drawString("Range: 200     Range Speed: 4", 405, 780);
					g.drawString("Train Time: 2     Upkeep: 1     Mass: 10", 405, 790);
				} else if (x >407+infantry.getWidth() && x<=407+infantry.getWidth()+ram.getWidth() && y >628 && y <=628+ram.getHeight()) {
						g.setColor(Color.BLACK);
						g.setFont(new Font("TimesRoman", Font.BOLD, 16));
						g.drawString("Basic Ram: Mark I", 405, 730);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
						g.drawString("Type: Ground Single Target", 405, 740);
						g.drawString("Cost: 20     HP: 100     Speed: 5", 405, 750);
						g.drawString("Melee Damage: 30     Range Damage: 0", 405, 760);
						g.drawString("Regen: 0     Reload: 0     Armor: 0", 405, 770);
						g.drawString("Range: 0     Range Speed: 0", 405, 780);
						g.drawString("Train Time: 3     Upkeep: 1     Mass: 30", 405, 790);
					} else if (x >407+infantry.getWidth()+ram.getWidth() && x<=407+infantry.getWidth()+ram.getWidth()+hero.getWidth() && y >628 && y <=628+hero.getHeight()) {
						g.setColor(Color.BLACK);
						g.setFont(new Font("TimesRoman", Font.BOLD, 16));
						g.drawString("Hero: Mark I---Healing", 405, 730);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
						g.drawString("Type: Ground Multi-Target", 405, 740);
						g.drawString("HP: 500     Speed: 4     Melee Damage: 50", 405, 750);
						g.drawString("Range Damage: 25     Reload: 2", 405, 760);
						g.drawString("Regen: 85     Regen Cost: 150     Armor: 0", 405, 770);
						g.drawString("Range: 400     Range Speed: 4", 405, 780);
						g.drawString("Regen Time: 12     Upkeep: 1     Mass: 50", 405, 790);
					}
				} 
			else {
					g.setColor(Color.BLACK);
					g.setFont(new Font("TimesRoman", Font.BOLD, 24));
					g.drawString("Health: " + Integer.toString(healthpoints), 405, 770);	
					g.setColor(new Color(60,179,113));
					g.fillRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10) * healthpoints/INIT_HEALTH, 20, 10, 10);
					g.setColor(Color.BLACK);
					g.drawRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10), 20, 10, 10);
				}
			}
		
		
	}
}