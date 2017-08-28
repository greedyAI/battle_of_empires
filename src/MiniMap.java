import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

public class MiniMap {
	
	private static int courtWidth1;
	private static int courtHeight1;
	private static int[][] arraymap1 = new int[161][161];

	public MiniMap(int courtWidth, int courtHeight) {
		courtHeight1 = courtHeight;
		courtWidth1 = courtWidth;
	}
	
	
	public static void updateMap(int courtWidth, int courtHeight) {
		int[][] arraymap = new int[courtWidth/25 + 1][courtHeight/25 + 1];
		for (GameTroop playerunits: GameTroop.getPlayerUnitList()) {
			arraymap[(int) ((playerunits.pos_y + playerunits.height/2)/25)][(int) ((playerunits.pos_x + playerunits.width/2)/25)] += 1;
		}
		for (ComputerInfantry infantry: ComputerInfantry.getCPUInfantry()) {
			arraymap[(int) ((infantry.pos_y + infantry.height/2)/25)][(int) ((infantry.pos_x + infantry.width/2)/25)] += 10;
		}
		for (int i=16; i<24; i++) {
			for (int j=24; j<32; j++) {
				arraymap[j][i] = -4;
			}
		}
		for (int i=24; i<32; i++) {
			for (int j=24; j<32; j++) {
				arraymap[j][i] = -3;
			}
		}
		for (int i=128; i<136; i++) {
			for (int j=128; j<136; j++) {
				arraymap[j][i] = -1;
			}
		}
		for (int i=136; i<144; i++) {
			for (int j=128; j<136; j++) {
				arraymap[j][i] = -2;
			}
		}
		arraymap1 = arraymap;
	}
	
	public static void draw(Graphics g) {
	    Graphics2D gg = (Graphics2D)g.create();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gg.setColor(new Color(195, 253, 184));
		gg.fillRect(GameCourt.SCROLL_X + 30, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y), 160, 160);
		gg.setColor(Color.BLACK);
		gg.drawRect(GameCourt.SCROLL_X + 30, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y), 160, 160);
		for (int i=0; i<161; i++) {
			for (int j=0; j<161; j++) {
				if (arraymap1[j][i] == -4) {
					gg.setColor(Color.ORANGE);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] == -3) {
					gg.setColor(Color.CYAN);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] == -2) {
					gg.setColor(Color.ORANGE);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] == -1) {
					gg.setColor(Color.MAGENTA);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] == 1 && arraymap1[j][i] <= 9) {
					gg.setColor(Color.BLUE);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] == 10) {
					gg.setColor(Color.RED);	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				} else if (arraymap1[j][i] >= 11) {
					gg.setColor(new Color(128,0,128));	
					gg.fillRect(GameCourt.SCROLL_X + 30 + i, (int) (screenSize.getHeight() - 210 + GameCourt.SCROLL_Y + j), 2, 2);					
				}
			}
		}
		gg.setColor(Color.BLACK);
		gg.drawRect(GameCourt.SCROLL_X/25 + GameCourt.SCROLL_X + 30, (int) (GameCourt.SCROLL_Y/25 + screenSize.getHeight() - 210 + GameCourt.SCROLL_Y), (int) screenSize.getWidth()/25, (int) screenSize.getHeight()/25);
	}
}
