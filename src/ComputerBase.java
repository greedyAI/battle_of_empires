
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

public class ComputerBase extends GameBase{

	
	//public static final String img_file = "Infantry.png";

	public static final int SIZE = 200;
	public static final double INIT_POS_X = 3200;
	public static final double INIT_POS_Y = 3200;
	public static int INIT_HEALTH = 10000;
	public static final int INIT_UPCOST = 10000;	
	public static final int INIT_MELEEDAMAGE = 5;
	public static final String INIT_TYPE = "CPU1";
	public static final int INIT_REGEN = 0;
	public static final int INIT_ARMOR = 0;
	public static final int INIT_ARMORDAMAGE = 0;

	
	//private static BufferedImage img;
	
	public ComputerBase(int courtWidth, int courtHeight) {
		super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
				courtWidth, courtHeight, INIT_HEALTH, INIT_UPCOST, INIT_TYPE, INIT_REGEN, INIT_ARMOR, INIT_ARMORDAMAGE, INIT_MELEEDAMAGE);
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
	
	//public static void newBase() {
	    //PlayerBase newbase = new PlayerBase(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
	//}
	

		

	@Override
	public void draw(Graphics g) {
		if (this.healthpoints>= 0) {
		g.setColor(Color.RED);
		g.fillRect((int) pos_x, (int) pos_y, width, height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 24));
		g.drawString("CPU BASE", 3240, 3312);
		g.drawString("Health: " + Integer.toString(healthpoints), 3205, 3370);
		g.setColor(new Color(60,179,113));
		g.fillRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10) * healthpoints/INIT_HEALTH, 20, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect((int) (pos_x + 5), (int) (pos_y + 175), (width - 10), 20, 10, 10);
			}
	}
}