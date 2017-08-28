/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	public static int infantryID;
	public static int ramID;
	public static boolean friendlyfire;
	
	public static boolean retreatWhenDamaged;
	
	private boolean keydown = false;
	private boolean firing = true;
	private PlayerBase playerbase;
	private Barracks barracks;
	private GameBase cpu1;
	private ComputerBarracks cpu1barracks;
	public static Hero hero;
	public static int heromode=1;
	public static boolean heroclicked = true;
	public static int herofiredrate = 0;
	
	public static int gameendcondition = 0;

	public static boolean barracksclicked;
	public static boolean playerbaseclicked;
	public static boolean infantryclicked;
	public static boolean armorramclicked;
	public static int barracksmousemode;
	public static int playercommandmode = 0;
	
	public static boolean dodgeBullet = false;
	public static int dodgeBulletTimer;
	
	public static int EDGE_SCROLL = 50;
	public static int SCROLL_X;
	public static int SCROLL_Y;
	public static int SCROLL_SPEED = 10;
	public static int mousex;
	public static int mousey;
	
	public static int playercash = 60;
	public static boolean lowcashwarning;
	
	public static int CPUInfantrySpawnRate = 0;
	public static int PlayerFiringRate = 0;
	
	public static BufferedImage background;
	
	public static boolean mousedrag = false;
	public static int mousedragxstart;
	public static int mousedragystart;
	public static int mousedragxend;
	public static int mousedragyend;

	public boolean playing = false; // whether the game is running
	//private JLabel status; // Current status text (i.e. Running...)

	// Game constants
	public static int COURT_WIDTH = 4000;
	public static int COURT_HEIGHT = 4000;
	// Update interval for timer, in milliseconds
	public static int INTERVAL =  (int) (35/ ((Game.speed1case ? 1 : 0) + 1.5 * (Game.speed15case ? 1 : 0) + 2 * (Game.speed2case ? 1 : 0)));

	public GameCourt() {
				
		try {
			if (background == null) {
				background = ImageIO.read(new File("PlayingBackground.jpg"));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		// creates border around the court area, JComponent method
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// This key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually
		// moves the square.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
					Infantry.newInfantry();
					barracksmousemode = 1;	
				} else if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
					ArmorRam.newArmorRam();
					barracksmousemode = 2;	
				} else if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
					barracksmousemode = 3;
					Hero.newHero();	
				}
				if (heroclicked == true && hero != null) {
					if (e.getKeyCode() == KeyEvent.VK_A) {
						hero.v_x = -hero.movespeed;	
					keydown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					hero.v_x = hero.movespeed;	
					keydown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					hero.v_y = hero.movespeed;
					keydown = true;	
				}
				if (e.getKeyCode() == KeyEvent.VK_W) {
					hero.v_y = -hero.movespeed;
					keydown = true;	
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE && herofiredrate >= 2) {
					HeroMissile.fireAtMouse();
					herofiredrate -= 2;
				}			
				}
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					SCROLL_X = Math.max(SCROLL_X - SCROLL_SPEED, 0);	
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				SCROLL_X = Math.min((SCROLL_X + SCROLL_SPEED), (int) (COURT_WIDTH - screenSize.getWidth()));	
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				SCROLL_Y = Math.min(SCROLL_Y + SCROLL_SPEED, (int) (COURT_HEIGHT - screenSize.getHeight()));
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				SCROLL_Y = Math.max(SCROLL_Y - SCROLL_SPEED, 0);
			}
			}

			public void keyReleased(KeyEvent e) {
				keydown = false;
			}
		});
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent arg0) {
				mousex = arg0.getX();
				mousey = arg0.getY();
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					if (arg0.getX()+SCROLL_X >400 && arg0.getX()+SCROLL_X <=600 && arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=800 ) {
					if (arg0.getX()+SCROLL_X >400 && arg0.getX()+SCROLL_X <=400+Barracks.infantry.getWidth() 
							&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+Barracks.infantry.getHeight() && barracksclicked == true) {
						Infantry.newInfantry();
						barracksmousemode = 1;
					}
					if (arg0.getX()+SCROLL_X >400+Barracks.infantry.getWidth() && arg0.getX()+SCROLL_X <=400+Barracks.infantry.getWidth()+Barracks.ram.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+Barracks.ram.getHeight() && barracksclicked == true) {
						ArmorRam.newArmorRam();
						barracksmousemode = 2;
					}
					if (arg0.getX()+SCROLL_X >400+Barracks.infantry.getWidth()+Barracks.ram.getWidth() && arg0.getX()+SCROLL_X <=400+Barracks.infantry.getWidth()+Barracks.ram.getWidth()+ Barracks.hero.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+Barracks.hero.getHeight() && barracksclicked == true) {
						barracksmousemode = 3;
						Hero.newHero();
					}
					
					barracksclicked = true;
					playerbaseclicked = false;
				} else if (arg0.getX()+SCROLL_X >600 && arg0.getX()+SCROLL_X <=800 && arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=800 ) {
					if (arg0.getX()+SCROLL_X >600 && arg0.getX()+SCROLL_X <=600+PlayerBase.baseDefend.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+PlayerBase.baseDefend.getHeight() && playerbaseclicked == true) {
						playercommandmode = 1;
					} else if (arg0.getX()+SCROLL_X >650 && arg0.getX()+SCROLL_X <=650+PlayerBase.generalAttack.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+PlayerBase.generalAttack.getHeight() && playerbaseclicked == true) {
						playercommandmode = 2;
					} else if (arg0.getX()+SCROLL_X >700 && arg0.getX()+SCROLL_X <=700+PlayerBase.baseCharge.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+PlayerBase.baseCharge.getHeight() && playerbaseclicked == true) {
						playercommandmode = 3;
					} else if (arg0.getX()+SCROLL_X >750 && arg0.getX()+SCROLL_X <=750+PlayerBase.ambushAttack.getWidth() 
					&& arg0.getY()+SCROLL_Y >600 && arg0.getY()+SCROLL_Y <=600+PlayerBase.ambushAttack.getHeight() && playerbaseclicked == true) {
						playercommandmode = 4;	
					}  else if (arg0.getX()+SCROLL_X >650 && arg0.getX()+SCROLL_X <=650+PlayerBase.manualhero.getWidth() 
					&& arg0.getY()+SCROLL_Y >650 && arg0.getY()+SCROLL_Y <=650+PlayerBase.manualhero.getHeight() && playerbaseclicked == true) {
						heromode = 1;
					} else if (arg0.getX()+SCROLL_X >700 && arg0.getX()+SCROLL_X <=700+PlayerBase.autohero.getWidth() 
					&& arg0.getY()+SCROLL_Y >650 && arg0.getY()+SCROLL_Y <=650+PlayerBase.autohero.getHeight() && playerbaseclicked == true) {
						heromode = 2;
					} else if (arg0.getX()+SCROLL_X >650 && arg0.getX()+SCROLL_X <=650+PlayerBase.dodgeBullet.getWidth() 
					&& arg0.getY()+SCROLL_Y >700 && arg0.getY()+SCROLL_Y <=700+PlayerBase.dodgeBullet.getHeight() && playerbaseclicked == true) {
						if (playercash >= 40) {
							dodgeBullet = true;
							playercash -= 40;
						if (dodgeBulletTimer == 0) {
							dodgeBulletTimer = 286;
						}
						} else {
							lowcashwarning = true;
						}
					} else if (arg0.getX()+SCROLL_X >700 && arg0.getX()+SCROLL_X <=700+PlayerBase.resetCommand.getWidth() 
					&& arg0.getY()+SCROLL_Y >700 && arg0.getY()+SCROLL_Y <=700+PlayerBase.resetCommand.getHeight() && playerbaseclicked == true) {
						playercommandmode = 0;
						for (Infantry infantry: Infantry.getInfantry()) {
							infantry.usercontrolled = false;
							infantryclicked = false;
							Infantry.rotate = false;
						}
						for (ArmorRam ram: ArmorRam.getArmorRam()) {
							ram.usercontrolled = false;
							armorramclicked = false;
							ArmorRam.rotate = false;
						}
					}
					barracksclicked= false;
					playerbaseclicked = true;
					barracksmousemode = 0;					
				} else {
					barracksclicked= false;
					playerbaseclicked = false;
					barracksmousemode = 0;
				}
															
				}				
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent arg0) {
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					mousex = arg0.getX();
					mousey = arg0.getY();
				
				
				/**for (int k=0; k<Infantry.getInfantry().size(); k++) {
					if (k != infantryID) {
						Infantry.getInfantry().get(k).usercontrolled = false;
					}
				}*/
				}/**  else if (arg0.getButton() == MouseEvent.BUTTON3) {
					infantryclicked = false;
					Infantry.rotate = false;
					for (int j=0; j<Infantry.getInfantry().size(); j++) {
							Infantry.getInfantry().get(j).usercontrolled = false;
					}
				}*/
				
				if(arg0.getButton() == MouseEvent.BUTTON1) {
				mousedragxstart = arg0.getX() + SCROLL_X;
				mousedragystart = arg0.getY() + SCROLL_Y;
				mousedrag  = true;						
				}
				if(arg0.getButton() == MouseEvent.BUTTON3) {
					keydown = true;
					mousex = arg0.getX();
					mousey = arg0.getY();					
					int x = mousex + SCROLL_X;
					int y = mousey + SCROLL_Y;
					if (x>0 && x< GameCourt.COURT_WIDTH && y>0 && y< GameCourt.COURT_HEIGHT) {
						for (Infantry infantry : Infantry.getInfantry() ) {
							if (infantry.usercontrolled == true && infantry.destinationreached == true) {
							infantry.destination_x = x;
							infantry.destination_y = y;
							double unitx = infantry.pos_x + infantry.width/2;
							double unity = infantry.pos_y + infantry.height/2;
							double dx = infantry.destination_x - unitx;
							double dy = infantry.destination_y - unity;
							infantry.pos_x = unitx - infantry.width / 2;
							infantry.pos_y = unity  - infantry.height / 2;
							double distance = Math.sqrt(dx*dx + dy*dy);						
							infantry.v_x = infantry.movespeed * dx/ distance;
							infantry.v_y = infantry.movespeed * dy/ distance;
							if (distance >= 2*infantry.movespeed) {
							infantry.destinationreached = false;								
							}

							}
					
						}
						for (ArmorRam ram : ArmorRam.getArmorRam() ) {
							if (ram.usercontrolled == true && ram.destinationreached == true) {
							ram.destination_x = x;
							ram.destination_y = y;
							double unitx = ram.pos_x + ram.width/2;
							double unity = ram.pos_y + ram.height/2;
							double dx = ram.destination_x - unitx;
							double dy = ram.destination_y - unity;
							double distance = Math.sqrt(dx*dx + dy*dy);
							ram.pos_x = unitx - ram.width / 2;
							ram.pos_y = unity  - ram.height / 2;					
							ram.v_x = ram.movespeed * dx/ distance;
							ram.v_y = ram.movespeed * dy/ distance;
							if (distance >= 2*ram.movespeed) {
								ram.destinationreached = false;							
							}
							}
			
						}
					}
				}
			
				
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent arg0) {
				if(arg0.getButton() == MouseEvent.BUTTON1) {
				mousedrag = false;
				mousedragxend = arg0.getX() + SCROLL_X;
				mousedragyend = arg0.getY() + SCROLL_Y;
				//for (Infantry infantry : Infantry.getInfantry()) {
					//infantry.usercontrolled = false;
				//}
				for (Infantry infantry : Infantry.getInfantry()) {
					if (infantry.pos_x + infantry.width/2 >= mousedragxstart && infantry.pos_x + infantry.width/2 <= mousedragxend
							&& infantry.pos_y + infantry.height/2 >= mousedragystart && infantry.pos_y + infantry.height/2 <= mousedragyend) {
						infantry.usercontrolled = true;
						infantryclicked = true;
						infantry.destinationreached = true;
					}
				}
				for (ArmorRam ram : ArmorRam.getArmorRam()) {
					if (ram.pos_x + ram.width/2 >= mousedragxstart && ram.pos_x + ram.width/2 <= mousedragxend
							&& ram.pos_y + ram.height/2 >= mousedragystart && ram.pos_y + ram.height/2 <= mousedragyend) {
						ram.usercontrolled = true;
						armorramclicked = true;
						ram.destinationreached = true;
					}
				}
				}
			
			}

			
		});

		//this.status = status;
		}
	

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {

		ComputerInfantry.infantrylist.removeAll(ComputerInfantry.infantrylist);
		Infantry.infantrylist.removeAll(Infantry.infantrylist);
		CPUInfantryProjectile.projectilelist.removeAll(CPUInfantryProjectile.projectilelist);
		InfantryProjectile.projectilelist.removeAll(InfantryProjectile.projectilelist);
		playerbase = new PlayerBase(COURT_WIDTH, COURT_HEIGHT);
		barracks = new Barracks(COURT_WIDTH, COURT_HEIGHT);
		cpu1 = new ComputerBase(COURT_WIDTH, COURT_HEIGHT);
		cpu1barracks = new ComputerBarracks(COURT_WIDTH, COURT_HEIGHT);
		hero = new Hero(COURT_WIDTH, COURT_HEIGHT);
		GameTroop.playerunitlist.add(hero);
		playercash = 60;
		CPUInfantrySpawnRate = 0;
		heromode = 1;
		PlayerFiringRate = 0;
		keydown = false;
		herofiredrate = 0;
		heroclicked = true;
		firing = true;
		gameendcondition = 0;
		playercommandmode = 0;
		EDGE_SCROLL = 50;
		SCROLL_SPEED = 10;
		COURT_WIDTH = 4000;
		COURT_HEIGHT = 4000;
		mousedrag = false;
		INTERVAL = (int) (35/ ((Game.speed1case ? 1 : 0) + 1.5 * (Game.speed15case ? 1 : 0) + 2 * (Game.speed2case ? 1 : 0)));
		//infantry = new Infantry(COURT_WIDTH, COURT_HEIGHT);
		//Infantry.newInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		ComputerInfantry.newCPUInfantry();
		//square = new Square(COURT_WIDTH, COURT_HEIGHT);
		//poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
		//snitch = new Circle(COURT_WIDTH, COURT_HEIGHT);

		
		playing = true;
		//status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	
	
	void tick() {
		if (!playing && gameendcondition == 1) {
			System.out.println(1);
			this.setLayout(new BorderLayout());		
			JLabel gameover = new JLabel("GAME OVER! YOU LOST!");
			gameover.setHorizontalAlignment(JLabel.CENTER);
			gameover.setForeground(Color.RED);
			gameover.setFont(new Font("Rockwell", Font.BOLD, 64));
		   	this.add(gameover, BorderLayout.CENTER);
		   	JButton restart = new JButton("Restart");
		   	restart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	                Game.main(null);
				}
			});
		   	this.add(restart, BorderLayout.SOUTH);
		   	this.setOpaque(false);
			super.validate();
		} else if (!playing && gameendcondition == 2) {
			System.out.println(2);
			this.setLayout(new BorderLayout());		
			JLabel gameover = new JLabel("CONGRATULATIONS! YOU WIN!");
			gameover.setHorizontalAlignment(JLabel.CENTER);
			gameover.setForeground(Color.BLUE);
			gameover.setFont(new Font("Rockwell", Font.BOLD, 64));
		   	this.add(gameover, BorderLayout.CENTER);			
		   	JButton restart = new JButton("Restart");
		   	restart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	                Game.main(null);
				}
			});
		   	this.add(restart, BorderLayout.SOUTH);
		   	this.setOpaque(false);
			super.validate();
		}
		
		
		if (playing) {
			
			retreatWhenDamaged = false;
			if (ComputerInfantry.getCPUInfantry().size() >= 1) {
			for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				ComputerInfantry cpuinfantry = ComputerInfantry.getCPUInfantry().get(i);
				if (CPUInfantrySpawnRate % 60 == cpuinfantry.clockcounterarmordamage % 60) {
					if ((CPUInfantrySpawnRate % 4000) <= 3999 && (CPUInfantrySpawnRate % 4000) >= 2000) {	
					if (CPUInfantrySpawnRate % 4000 == 3999) {
						ComputerInfantry.INIT_HEALTH += 10;
					}	
					CPUInfantryProjectile.baseChargeAI();
						//CPUInfantryProjectile.suicideOrRetreatAI();
				} else {
					if (ComputerInfantry.getCPUInfantry().size()>=1) {				
							CPUInfantryProjectile.defendBaseAI();			
					}				
				}
				}
								
			}				
			}
			
			if (CPUInfantrySpawnRate % 100 == 0) {
				ComputerInfantry.newCPUInfantry();				
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 1 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 1.3 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 1.6 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 1.9 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 2.2 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 2.5 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 2.8 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 100 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 3.1 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			if (CPUInfantrySpawnRate % 1000 == 0 && ((Infantry.getInfantry().size() + ArmorRam.getArmorRam().size()) > 3.4 * ComputerInfantry.getCPUInfantry().size())) {
				ComputerInfantry.newCPUInfantry();
			}
			
			herofiredrate = Math.min(herofiredrate + 1, 2);
			
			CPUInfantrySpawnRate += 1;
			
			if (Infantry.getInfantry().size() >= 1) {
			for (int i=0; i<Infantry.getInfantry().size(); i++) {
			Infantry infantry = Infantry.getInfantry().get(i);
			if (playercommandmode == 1 && PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				InfantryProjectile.defendBaseCommand();
			} else if (playercommandmode == 2 && PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				InfantryProjectile.targetClosestCommand();
			} else if (playercommandmode == 3 && PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				InfantryProjectile.baseChargeCommand();
			} else if (playercommandmode == 4 && PlayerFiringRate % 30 == infantry.clockcounterarmordamage % 30) {
				InfantryProjectile.ambushClosestCommand();
			}				
			}				
			}
			
			if (ArmorRam.getArmorRam().size() >= 1) {
			for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
			ArmorRam ram = ArmorRam.getArmorRam().get(i);
			if (playercommandmode == 1 && PlayerFiringRate % 10 == ram.clockcounterarmordamage) {
				ArmorRam.defendBaseCommand();
			} else if (playercommandmode == 2 && PlayerFiringRate % 20 == ram.clockcounterarmordamage) {
				ArmorRam.targetClosestCommand();
			} else if (playercommandmode == 3 && PlayerFiringRate % 20 == ram.clockcounterarmordamage) {
				ArmorRam.baseChargeCommand();
			} else if (playercommandmode == 4 && PlayerFiringRate % 20 == ram.clockcounterarmordamage) {
				ArmorRam.ambushClosestCommand();
			}				
			}				
			}
			
			
			if (CPUInfantrySpawnRate % 10 == 0) {
				MiniMap.updateMap(COURT_WIDTH, COURT_HEIGHT);
			}
			
			if (PlayerFiringRate % 10 == 1) {
				playercash += 1;			
			}			
			
			if (hero != null) {
			if (heromode == 2 && PlayerFiringRate % 2 == hero.clockcounterarmordamage % 2) {
				HeroMissile.fireGuidedMissile();
			}				
			}

			if (hero != null) {
				hero.v_x = hero.v_x * 0.9;
				hero.v_y = hero.v_y * 0.9;
			}
			
			PlayerFiringRate += 1;
			
			if (dodgeBullet == true && dodgeBulletTimer > 0) {
				InfantryProjectile.bulletDodgeCommand();
				dodgeBulletTimer -= 1;
			}
			if (dodgeBulletTimer == 0) {
				dodgeBullet = false;
			}
			
			if (hero != null) {
			if (Hero.trainingcounter > 0 && Hero.trainingcounter <= 343 && playercash >= Hero.INIT_COST) {
				Hero.trainingcounter -= 1;
				hero.regen();
			} else if (Hero.trainingcounter == 0) {
				if (playercash >= Hero.INIT_COST) {
				GameCourt.playercash -= Hero.INIT_COST;
				GameCourt.lowcashwarning = false;		    
				Hero.trainingcounter -= 1;
				barracksmousemode = 0;
			} else {
				GameCourt.lowcashwarning = true;
			}
			}				
			}

			
			if (Infantry.trainingcounter > 0 && Infantry.trainingcounter <= 57) {
				Infantry.trainingcounter -= 1;
			} else if (Infantry.trainingcounter == 0) {
				if (playercash >= Infantry.INIT_COST) {							
				GameCourt.lowcashwarning = false;
				Infantry.INIT_POS_X = 400 + 200*Math.random();
				Infantry.INIT_POS_Y = 800 + 200*Math.random();
				Infantry.INIT_CLOCKCOUNTER = Infantry.generator.nextInt(100);
		    Infantry newinfantry = new Infantry(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		    GameCourt.playercash -= Infantry.INIT_COST;
		    Infantry.infantrylist.add(newinfantry);
		    GameTroop.playerunitlist.add(newinfantry);
		    Infantry.trainingcounter -= 1;
			barracksmousemode = 0;
			} else {
				GameCourt.lowcashwarning = true;
			}
			}
			
			if (ArmorRam.trainingcounter > 0 && ArmorRam.trainingcounter <= 86) {
				ArmorRam.trainingcounter -= 1;
			} else if (ArmorRam.trainingcounter == 0) {
				if (playercash >= ArmorRam.INIT_COST) {							
				GameCourt.lowcashwarning = false;
				ArmorRam.INIT_POS_X = 400 + 200*Math.random();
				ArmorRam.INIT_POS_Y = 800 + 200*Math.random();
				ArmorRam.INIT_CLOCKCOUNTER = ArmorRam.generator.nextInt(20);
				ArmorRam newram = new ArmorRam(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		    GameCourt.playercash -= ArmorRam.INIT_COST;
		    ArmorRam.ramlist.add(newram);
		    GameTroop.playerunitlist.add(newram);
		    ArmorRam.trainingcounter -= 1;
			barracksmousemode = 0;
			} else {
				GameCourt.lowcashwarning = true;
			}
			}
			
			
			if (Infantry.getInfantry().size()>=1) {
				for (int i=0; i<Infantry.getInfantry().size(); i++) {
				Infantry.getInfantry().get(i).move();
				if (infantryclicked == false || Infantry.getInfantry().get(i).usercontrolled == false || keydown == false ) {
					if (playercommandmode == 0 && Infantry.getInfantry().get(i).usercontrolled == false) {
				Infantry.getInfantry().get(i).v_x *= 0.9;
				Infantry.getInfantry().get(i).v_y *= 0.9;						
					}
				}
				if (keydown == false && infantryID == i && infantryclicked == true && Infantry.getInfantry().get(i).usercontrolled == true) {
					Infantry.getInfantry().get(i).v_x *= 0.9;
					Infantry.getInfantry().get(i).v_y *= 0.9;						
				}
				
				
				}
			}
			
			if (ArmorRam.getArmorRam().size()>=1) {
				for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
					ArmorRam.getArmorRam().get(i).move();
				if (armorramclicked == false || ArmorRam.getArmorRam().get(i).usercontrolled == false || keydown == false ) {
					if (playercommandmode == 0 && ArmorRam.getArmorRam().get(i).usercontrolled == false) {
						ArmorRam.getArmorRam().get(i).v_x *= 0.9;
						ArmorRam.getArmorRam().get(i).v_y *= 0.9;						
					}
				}
				if (keydown == false && ramID == i && armorramclicked == true && ArmorRam.getArmorRam().get(i).usercontrolled == true) {
					ArmorRam.getArmorRam().get(i).v_x *= 0.9;
					ArmorRam.getArmorRam().get(i).v_y *= 0.9;						
				}
				
				
				}
			}
			
			if (hero != null) {
					hero.move();
				if (heroclicked == false || hero.usercontrolled == false || keydown == false ) {
					if (playercommandmode == 0 && hero.usercontrolled == false) {
						hero.v_x *= 0.9;
						hero.v_y *= 0.9;						
					}
				}
				if (keydown == false && heroclicked == true && hero.usercontrolled == true) {
					hero.v_x *= 0.9;
					hero.v_y *= 0.9;						
				}				
			}

			//Hero bouncing off other objects
				if (hero != null) {
					if(barracks != null) {
					friendlyfire = true;
				barracks.bounceDirection(hero);						
					}
					if(playerbase != null) {
				friendlyfire = true;
				playerbase.bounceDirection(hero);						
					}
					if(cpu1 != null) {
				friendlyfire = false;
				cpu1.bounceDirection(hero);						
					}
					if(cpu1barracks != null) {
				friendlyfire = false;
				cpu1barracks.bounceDirection(hero);						
					}
				hero.bounce(hero.hitWall());				
				if (ComputerInfantry.getCPUInfantry().size()>=1) {
					for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
						friendlyfire = false;
						hero.bounceDirection(ComputerInfantry.getCPUInfantry().get(i));					
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
						}
						GameTroop.hit = false;
					}				
					}
					if (Infantry.getInfantry().size()>=1) {
					for (int i=0; i<Infantry.getInfantry().size(); i++) {
						friendlyfire = true;
						hero.bounceDirection(Infantry.getInfantry().get(i));					
						if (GameTroop.hit == true) {
							//Infantry.getInfantry().get(i).v_x *= 0.9;
							//Infantry.getInfantry().get(i).v_y *= 0.9;
						}
						GameTroop.hit = false;
					}				
					}
					if (ArmorRam.getArmorRam().size()>=1) {
					for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
						friendlyfire = true;
						hero.bounceDirection(ArmorRam.getArmorRam().get(i));					
						if (GameTroop.hit == true) {
							//Infantry.getInfantry().get(i).v_x *= 0.9;
							//Infantry.getInfantry().get(i).v_y *= 0.9;
						}
						GameTroop.hit = false;
					}				
					}
					if (InfantryProjectile.getInfantryProjectile().size()>=1) {
						for (int i=0; i<InfantryProjectile.getInfantryProjectile().size(); i++) {
							friendlyfire = true;
							//hero.bounceDirection(InfantryProjectile.getInfantryProjectile().get(i));						
							if (GameTroop.hit == true) {
								//InfantryProjectile.getInfantryProjectile().get(i).v_x *= 0.9;
								//InfantryProjectile.getInfantryProjectile().get(i).v_y *= 0.9;
							}
							GameTroop.hit = false;					
						}				
					}
					if (CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
						for (int i=0; i<CPUInfantryProjectile.getCPUInfantryProjectile().size(); i++) {
							friendlyfire = false;
							hero.bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));						
							if (GameTroop.hit == true) {
								//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_x *= 0.9;
								//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_y *= 0.9;
							}
							GameTroop.hit = false;						
						}				
					}
				}
				
				
				
			
			//Barracks bouncing off other objects
			if (ComputerInfantry.getCPUInfantry().size()>=1) {
			for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				friendlyfire = false;
				if (barracks != null) {
				barracks.bounceDirection(ComputerInfantry.getCPUInfantry().get(i));					
				}
				if (GameTroop.hit == true) {
					//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
					//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (Infantry.getInfantry().size()>=1) {
			for (int i=0; i<Infantry.getInfantry().size(); i++) {
				friendlyfire = true;
				if (barracks != null) {
				barracks.bounceDirection(Infantry.getInfantry().get(i));					
				}
				if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (ArmorRam.getArmorRam().size()>=1) {
			for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
				friendlyfire = true;
				if (barracks != null) {
				barracks.bounceDirection(ArmorRam.getArmorRam().get(i));					
				}
				if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (InfantryProjectile.getInfantryProjectile().size()>=1) {
				for (int i=0; i<InfantryProjectile.getInfantryProjectile().size(); i++) {
					friendlyfire = true;
					if (barracks != null) {
					barracks.bounceDirection(InfantryProjectile.getInfantryProjectile().get(i));						
					}
					if (GameTroop.hit == true) {
						//InfantryProjectile.getInfantryProjectile().get(i).v_x *= 0.9;
						//InfantryProjectile.getInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;					
				}				
			}
			if (HeroMissile.getHeroMissile().size()>=1) {
				for (int i=0; i<HeroMissile.getHeroMissile().size(); i++) {
					friendlyfire = true;
					if (barracks != null) {
					barracks.bounceDirection(HeroMissile.getHeroMissile().get(i));						
					}
					if (GameTroop.hit == true) {
						//InfantryProjectile.getInfantryProjectile().get(i).v_x *= 0.9;
						//InfantryProjectile.getInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;					
				}				
			}			
			if (CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
				for (int i=0; i<CPUInfantryProjectile.getCPUInfantryProjectile().size(); i++) {
					friendlyfire = false;
					if (barracks != null) {
					barracks.bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));						
					}
					if (GameTroop.hit == true) {
						//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_x *= 0.9;
						//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;						
				}				
			}

			
			//CPUBarracks bouncing off other objects
			if (ComputerInfantry.getCPUInfantry().size()>=1) {
			for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				friendlyfire = true;
				if (cpu1barracks != null) {
				cpu1barracks.bounceDirection(ComputerInfantry.getCPUInfantry().get(i));					
				}
				if (GameTroop.hit == true) {
					//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
					//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (Infantry.getInfantry().size()>=1) {
			for (int i=0; i<Infantry.getInfantry().size(); i++) {
				friendlyfire = false;
				if (cpu1barracks != null) {
				cpu1barracks.bounceDirection(Infantry.getInfantry().get(i));					
				}
				if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (ArmorRam.getArmorRam().size()>=1) {
			for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
				friendlyfire = false;
				if (cpu1barracks != null) {
					cpu1barracks.bounceDirection(ArmorRam.getArmorRam().get(i));					
				}
				if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
				}
				GameTroop.hit = false;
			}				
			}
			if (InfantryProjectile.getInfantryProjectile().size()>=1) {
				for (int i=0; i<InfantryProjectile.getInfantryProjectile().size(); i++) {
					friendlyfire = false;
					if (cpu1barracks != null) {
					cpu1barracks.bounceDirection(InfantryProjectile.getInfantryProjectile().get(i));						
					}
					if (GameTroop.hit == true) {
						//InfantryProjectile.getInfantryProjectile().get(i).v_x *= 0.9;
						//InfantryProjectile.getInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;					
				}				
			}
			if (HeroMissile.getHeroMissile().size()>=1) {
				for (int i=0; i<HeroMissile.getHeroMissile().size(); i++) {
					friendlyfire = false;
					if (cpu1barracks != null) {
					cpu1barracks.bounceDirection(HeroMissile.getHeroMissile().get(i));
					}
					if (GameTroop.hit == true) {
						//InfantryProjectile.getInfantryProjectile().get(i).v_x *= 0.9;
						//InfantryProjectile.getInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;					
				}				
			}
			if (CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
				for (int i=0; i<CPUInfantryProjectile.getCPUInfantryProjectile().size(); i++) {
					friendlyfire = true;
					if (cpu1barracks != null) {
					cpu1barracks.bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));						
					}
					if (GameTroop.hit == true) {
						//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_x *= 0.9;
						//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;						
				}				
			}			
			
			
			//ComputerInfantry bounce off each other
			if (ComputerInfantry.getCPUInfantry().size()>=2) {
				for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					ComputerInfantry.getCPUInfantry().get(i).move();											
					for (int j=i+1; j<ComputerInfantry.getCPUInfantry().size(); j++) {
						friendlyfire = true;
						ComputerInfantry.getCPUInfantry().get(i).bounceDirection(ComputerInfantry.getCPUInfantry().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(j).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(j).v_y *= 0.9;
						}
						GameTroop.hit = false;
					}
					friendlyfire = false;
					if (playerbase != null) {
					playerbase.bounceDirection(ComputerInfantry.getCPUInfantry().get(i));						
					}
					if (GameTroop.hit == true) {
						//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
						//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;
					friendlyfire = true;
					if (cpu1 != null) {
					cpu1.bounceDirection(ComputerInfantry.getCPUInfantry().get(i));						
					}
					if (GameTroop.hit == true) {
						//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
						//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
					}
					GameTroop.hit = false;
					ComputerInfantry.getCPUInfantry().get(i).bounce(ComputerInfantry.getCPUInfantry().get(i).hitWall());
					ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(i));
				}
			} else if (ComputerInfantry.getCPUInfantry().size()>=1) {
				ComputerInfantry.getCPUInfantry().get(0).move();
				friendlyfire = false;
				if (playerbase != null) {
				playerbase.bounceDirection(ComputerInfantry.getCPUInfantry().get(0));					
				}
				if (GameTroop.hit == true) {
					//ComputerInfantry.getCPUInfantry().get(0).v_x *= 0.9;
					//ComputerInfantry.getCPUInfantry().get(0).v_y *= 0.9;
				}
				GameTroop.hit = false;
				friendlyfire = true;
				if (cpu1 != null) {
				cpu1.bounceDirection(ComputerInfantry.getCPUInfantry().get(0));					
				}
				if (GameTroop.hit == true) {
					//ComputerInfantry.getCPUInfantry().get(0).v_x *= 0.9;
					//ComputerInfantry.getCPUInfantry().get(0).v_y *= 0.9;
				}
				GameTroop.hit = false;
				ComputerInfantry.getCPUInfantry().get(0).bounce(ComputerInfantry.getCPUInfantry().get(0).hitWall());
				ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(0));
			}
			
			
			//ComputerInfantry bounce off Infantry
			if (ComputerInfantry.getCPUInfantry().size()>=1 && Infantry.getInfantry().size()>=1) {
				for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					for (int j=0; j<Infantry.getInfantry().size(); j++) {
						friendlyfire = false;
						ComputerInfantry.getCPUInfantry().get(i).bounceDirection(Infantry.getInfantry().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
							//Infantry.getInfantry().get(j).v_x *= 0.9;
							//Infantry.getInfantry().get(j).v_y *= 0.9;
						}
						GameTroop.hit = false;
						Infantry.die(Infantry.getInfantry().get(j));
					}
					ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(i));
				}
			}
						
			//ComputerInfantry bounce off ArmorRam
			if (ComputerInfantry.getCPUInfantry().size()>=1 && ArmorRam.getArmorRam().size()>=1) {
				for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					for (int j=0; j<ArmorRam.getArmorRam().size(); j++) {
						friendlyfire = false;
						ComputerInfantry.getCPUInfantry().get(i).bounceDirection(ArmorRam.getArmorRam().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
							//Infantry.getInfantry().get(j).v_x *= 0.9;
							//Infantry.getInfantry().get(j).v_y *= 0.9;
						}
						GameTroop.hit = false;
						ArmorRam.die(ArmorRam.getArmorRam().get(j));
					}
					ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(i));
				}
			}
			
			//Infantry bounce off ArmorRam
			if (Infantry.getInfantry().size()>=1 && ArmorRam.getArmorRam().size()>=1) {
				for (int i=0; i<Infantry.getInfantry().size(); i++) {
					for (int j=0; j<ArmorRam.getArmorRam().size(); j++) {
						friendlyfire = true;
						Infantry.getInfantry().get(i).bounceDirection(ArmorRam.getArmorRam().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
							//Infantry.getInfantry().get(j).v_x *= 0.9;
							//Infantry.getInfantry().get(j).v_y *= 0.9;
						}
						GameTroop.hit = false;
						ArmorRam.die(ArmorRam.getArmorRam().get(j));
					}
					Infantry.die(Infantry.getInfantry().get(i));
				}
			}
			
			
			//InfantryProjectile bounces off ComputerInfantry
			if (ComputerInfantry.getCPUInfantry().size()>=1 && InfantryProjectile.getInfantryProjectile().size()>=1) {
				for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					for (int j=0; j<InfantryProjectile.getInfantryProjectile().size(); j++) {
						friendlyfire = false;
						ComputerInfantry.getCPUInfantry().get(i).bounceDirection(InfantryProjectile.getInfantryProjectile().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
						}
						GameTroop.hit = false;
						InfantryProjectile.die(InfantryProjectile.getInfantryProjectile().get(j));
					}
					ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(i));
				}
			}
			
			//HeroMissile bounces off ComputerInfantry
			if (ComputerInfantry.getCPUInfantry().size()>=1 && HeroMissile.getHeroMissile().size()>=1) {
				for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
					for (int j=0; j<HeroMissile.getHeroMissile().size(); j++) {
						friendlyfire = false;
						ComputerInfantry.getCPUInfantry().get(i).bounceDirection(HeroMissile.getHeroMissile().get(j));
						if (GameTroop.hit == true) {
							//ComputerInfantry.getCPUInfantry().get(i).v_x *= 0.9;
							//ComputerInfantry.getCPUInfantry().get(i).v_y *= 0.9;
						}
						GameTroop.hit = false;
						if(barracks != null) {
						friendlyfire = true;
						barracks.bounceDirection(HeroMissile.getHeroMissile().get(j));							
						}
						if(playerbase != null) {
						friendlyfire = true;
						playerbase.bounceDirection(HeroMissile.getHeroMissile().get(j));								
						}
						if(cpu1 != null) {
						friendlyfire = false;
						cpu1.bounceDirection(HeroMissile.getHeroMissile().get(j));							
						}
						if(cpu1barracks != null) {
						friendlyfire = false;
						cpu1barracks.bounceDirection(HeroMissile.getHeroMissile().get(j));							
						}
						HeroMissile.die(HeroMissile.getHeroMissile().get(j));
					}
					ComputerInfantry.die(ComputerInfantry.getCPUInfantry().get(i));
				}
			}
			
			//CPUInfantryProjectile bounces off Infantry
			if (Infantry.getInfantry().size()>=1 && CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
				for (int i=0; i<Infantry.getInfantry().size(); i++) {
					for (int j=0; j<CPUInfantryProjectile.getCPUInfantryProjectile().size(); j++) {
						friendlyfire = false;
						Infantry.getInfantry().get(i).bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(j));
						if (GameTroop.hit == true) {
						//Infantry.getInfantry().get(i).v_x *= 0.9;
						//Infantry.getInfantry().get(i).v_y *= 0.9;							
						}
						GameTroop.hit = false;
						CPUInfantryProjectile.die(CPUInfantryProjectile.getCPUInfantryProjectile().get(j));
					}
					Infantry.die(Infantry.getInfantry().get(i));
				}
			}
			
			//CPUInfantryProjectile bounces off ArmorRam
			if (ArmorRam.getArmorRam().size()>=1 && CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
				for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
					for (int j=0; j<CPUInfantryProjectile.getCPUInfantryProjectile().size(); j++) {
						friendlyfire = false;
						ArmorRam.getArmorRam().get(i).bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(j));
						if (GameTroop.hit == true) {
						//Infantry.getInfantry().get(i).v_x *= 0.9;
						//Infantry.getInfantry().get(i).v_y *= 0.9;							
						}
						GameTroop.hit = false;
						CPUInfantryProjectile.die(CPUInfantryProjectile.getCPUInfantryProjectile().get(j));
					}
					ArmorRam.die(ArmorRam.getArmorRam().get(i));
				}
			}
			
			//Infantry bounce off each other
			if (Infantry.getInfantry().size()>=2) {
				for (int i=0; i<Infantry.getInfantry().size(); i++) {
					if (Infantry.getInfantry().get(i).usercontrolled == false && playercommandmode == 0) {
						Infantry.getInfantry().get(i).v_x = Math.random() -0.5;
						Infantry.getInfantry().get(i).v_y = Math.random() -0.5;
					}
					for (int j=i+1; j<Infantry.getInfantry().size(); j++) {
					friendlyfire = true;
					Infantry.getInfantry().get(i).bounceDirection(Infantry.getInfantry().get(j));
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
					//Infantry.getInfantry().get(j).v_x *= 0.9;
					//Infantry.getInfantry().get(j).v_y *= 0.9;					
					}
					GameTroop.hit = false;
					}
					friendlyfire = true;
					if (playerbase != null) {
					playerbase.bounceDirection(Infantry.getInfantry().get(i));						
					}
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;		
					}
					GameTroop.hit = false;
					friendlyfire = false;
					if (cpu1 != null) {
					cpu1.bounceDirection(Infantry.getInfantry().get(i));						
					}
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;				
					}
					GameTroop.hit = false;
					Infantry.getInfantry().get(i).bounce(Infantry.getInfantry().get(i).hitWall());
					Infantry.die(Infantry.getInfantry().get(i));
				}
		
			} else if (Infantry.getInfantry().size()>=1) {
				if (Infantry.getInfantry().get(0).usercontrolled == false && playercommandmode == 0) {
				Infantry.getInfantry().get(0).v_x = Math.random() -0.5;
				Infantry.getInfantry().get(0).v_y = Math.random() -0.5;
				}
				friendlyfire = true;
				if (playerbase != null) {
				playerbase.bounceDirection(Infantry.getInfantry().get(0));					
				}
				if (GameTroop.hit == true) {
				//Infantry.getInfantry().get(0).v_x *= 0.9;
				//Infantry.getInfantry().get(0).v_y *= 0.9;				
				}
				GameTroop.hit = false;
				friendlyfire = false;
				if (cpu1 != null) {
				cpu1.bounceDirection(Infantry.getInfantry().get(0));					
				}
				if (GameTroop.hit == true) {
				//Infantry.getInfantry().get(0).v_x *= 0.9;
				//Infantry.getInfantry().get(0).v_y *= 0.9;					
				}
				GameTroop.hit = false;
				Infantry.getInfantry().get(0).bounce(Infantry.getInfantry().get(0).hitWall());
				Infantry.die(Infantry.getInfantry().get(0));
			}
						
			//InfantryProjectile bounce off each other
			if (InfantryProjectile.getInfantryProjectile().size()>=1) {
				for (int i=0; i<InfantryProjectile.getInfantryProjectile().size(); i++) {
					//if (InfantryProjectile.getInfantryProjectile().size()>=2) {
						//for (int j=i+1; j<InfantryProjectile.getInfantryProjectile().size(); j++) {
							//friendlyfire = true;
						//InfantryProjectile.getInfantryProjectile().get(i).bounceDirection(InfantryProjectile.getInfantryProjectile().get(j));
						//}
					//}
					friendlyfire = true;
					if (playerbase != null) {
					playerbase.bounceDirection(InfantryProjectile.getInfantryProjectile().get(i));						
					}
					friendlyfire = false;
					if (cpu1 != null) {
					cpu1.bounceDirection(InfantryProjectile.getInfantryProjectile().get(i));						
					}
					InfantryProjectile.die(InfantryProjectile.getInfantryProjectile().get(i));			
					}
					}
			
			//CPUInfantryProjectile bounce off each other
			if (CPUInfantryProjectile.getCPUInfantryProjectile().size()>=1) {
				for (int i=0; i<CPUInfantryProjectile.getCPUInfantryProjectile().size(); i++) {
					//if (CPUInfantryProjectile.getCPUInfantryProjectile().size()>=2) {
						//for (int j=i+1; j<CPUInfantryProjectile.getCPUInfantryProjectile().size(); j++) {
							//friendlyfire = true;
							//CPUInfantryProjectile.getCPUInfantryProjectile().get(i).bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(j));
						//}
					//}
					friendlyfire = false;
					if (playerbase != null) {
					playerbase.bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));						
					}
					friendlyfire = true;
					if (cpu1 != null) {
					cpu1.bounceDirection(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));						
					}
					CPUInfantryProjectile.die(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));			
					}
					}
			
			
			//ArmorRam bounce off each other
			if (ArmorRam.getArmorRam().size()>=2) {
				for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
					if (ArmorRam.getArmorRam().get(i).usercontrolled == false && playercommandmode == 0) {
						ArmorRam.getArmorRam().get(i).v_x = Math.random() -0.5;
						ArmorRam.getArmorRam().get(i).v_y = Math.random() -0.5;
					}
					for (int j=i+1; j<ArmorRam.getArmorRam().size(); j++) {
					friendlyfire = true;
					ArmorRam.getArmorRam().get(i).bounceDirection(ArmorRam.getArmorRam().get(j));
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;
					//Infantry.getInfantry().get(j).v_x *= 0.9;
					//Infantry.getInfantry().get(j).v_y *= 0.9;					
					}
					GameTroop.hit = false;
					}
					friendlyfire = true;
					if (playerbase != null) {
					playerbase.bounceDirection(ArmorRam.getArmorRam().get(i));						
					}
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;		
					}
					GameTroop.hit = false;
					friendlyfire = false;
					if (cpu1 != null) {
					cpu1.bounceDirection(ArmorRam.getArmorRam().get(i));						
					}
					if (GameTroop.hit == true) {
					//Infantry.getInfantry().get(i).v_x *= 0.9;
					//Infantry.getInfantry().get(i).v_y *= 0.9;				
					}
					GameTroop.hit = false;
					ArmorRam.getArmorRam().get(i).bounce(ArmorRam.getArmorRam().get(i).hitWall());
					ArmorRam.die(ArmorRam.getArmorRam().get(i));
				}
		
			} else if (ArmorRam.getArmorRam().size()>=1) {
				if (ArmorRam.getArmorRam().get(0).usercontrolled == false && playercommandmode == 0) {
					ArmorRam.getArmorRam().get(0).v_x = Math.random() -0.5;
					ArmorRam.getArmorRam().get(0).v_y = Math.random() -0.5;
				}
				friendlyfire = true;
				if (playerbase != null) {
				playerbase.bounceDirection(ArmorRam.getArmorRam().get(0));					
				}
				if (GameTroop.hit == true) {
				//Infantry.getInfantry().get(0).v_x *= 0.9;
				//Infantry.getInfantry().get(0).v_y *= 0.9;				
				}
				GameTroop.hit = false;
				friendlyfire = false;
				if (cpu1 != null) {
				cpu1.bounceDirection(ArmorRam.getArmorRam().get(0));					
				}
				if (GameTroop.hit == true) {
				//Infantry.getInfantry().get(0).v_x *= 0.9;
				//Infantry.getInfantry().get(0).v_y *= 0.9;					
				}
				GameTroop.hit = false;
				ArmorRam.getArmorRam().get(0).bounce(ArmorRam.getArmorRam().get(0).hitWall());
				ArmorRam.die(ArmorRam.getArmorRam().get(0));
			}
			
			//check for mouse scroll
			Point point = MouseInfo.getPointerInfo().getLocation();
			int x = (int) point.getX();
			int y = (int) point.getY();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			if (x>screenSize.getWidth()-EDGE_SCROLL && SCROLL_X<COURT_WIDTH-x) {
				SCROLL_X = Math.min(SCROLL_X + SCROLL_SPEED, COURT_WIDTH-x);
			} else if (x<EDGE_SCROLL && SCROLL_X>0) {
				SCROLL_X = Math.max(SCROLL_X -SCROLL_SPEED, 0);
			}
			if (y>screenSize.getHeight()-EDGE_SCROLL && SCROLL_Y<COURT_HEIGHT-y+50) {
				SCROLL_Y = Math.min(SCROLL_Y + SCROLL_SPEED, COURT_HEIGHT-y+50);
			} else if (y<EDGE_SCROLL && SCROLL_Y>0) {
				SCROLL_Y = Math.max(SCROLL_Y - SCROLL_SPEED, 0);
			}
			
			
			
			// check for the game end conditions
			if (playerbase == null || playerbase.healthpoints <= 0) {
				SCROLL_X = 0;
				SCROLL_Y = 0;
				playing = false;
				gameendcondition = 1;
				//status.setText("You lose!");

			} else if (cpu1 == null || cpu1.healthpoints <= 0) {
				SCROLL_X = 0;
				SCROLL_Y = 0;
				playing = false;
				gameendcondition = 2;
				//status.setText("You win!");
			}
				for (InfantryProjectile projectile: InfantryProjectile.getInfantryProjectile()) {
					projectile.move();
				}
				if (firing == true) {
				for (HeroMissile projectile: HeroMissile.getHeroMissile()) {
					projectile.move();					
				}
				//InfantryProjectile.getInfantryProjectile().getLast().draw(g);
				//firing = false;
			}
			// update the display
			repaint();
		}
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, -SCROLL_X, -SCROLL_Y, null);
		g.translate(-SCROLL_X, -SCROLL_Y);
		
		
		if (Infantry.getInfantry().size()>=1) {
			for (int i=0; i<Infantry.getInfantry().size(); i++) {
				if (Infantry.getInfantry().get(i).usercontrolled == true) {
					Infantry.rotate = true;
				} else {
					Infantry.rotate = false;
				}
				Infantry.getInfantry().get(i).draw(g, i);
			}
		}	
		
		if (ArmorRam.getArmorRam().size()>=1) {
			for (int i=0; i<ArmorRam.getArmorRam().size(); i++) {
				if (ArmorRam.getArmorRam().get(i).usercontrolled == true) {
					ArmorRam.rotate = true;
				} else {
					ArmorRam.rotate = false;
				}
				ArmorRam.getArmorRam().get(i).draw(g, i);
			}
		}
				
			for (int i=0; i<InfantryProjectile.getInfantryProjectile().size(); i++) {
				if (InfantryProjectile.getInfantryProjectile().get(i).healthpoints >= 0 && InfantryProjectile.getInfantryProjectile().get(i).hitWall() == null) {
					InfantryProjectile.getInfantryProjectile().get(i).draw(g);
					InfantryProjectile.getInfantryProjectile().get(i).range -= 1;
				InfantryProjectile.die(InfantryProjectile.getInfantryProjectile().get(i));			
				}
				/** COULD INCREASE CODE EFFICIENCY BY GETTING RID OF BAD PROJECTILES HERE*/
			}

		
		if (firing == true || firing == false) {
			for (int i=0; i<HeroMissile.getHeroMissile().size(); i++) {
				if (HeroMissile.getHeroMissile().get(i).healthpoints > 0 && HeroMissile.getHeroMissile().get(i).hitWall() == null) {
					HeroMissile.getHeroMissile().get(i).draw(g);
					HeroMissile.getHeroMissile().get(i).range -= 1;
				HeroMissile.die(HeroMissile.getHeroMissile().get(i));			
				}
			}

		}
		
		if (ComputerInfantry.getCPUInfantry().size()>=1) {
			for (int i=0; i<ComputerInfantry.getCPUInfantry().size(); i++) {
				ComputerInfantry.getCPUInfantry().get(i).draw(g, i);
			}
		}
		
		for (int i=0; i<CPUInfantryProjectile.getCPUInfantryProjectile().size(); i++) {
			if (CPUInfantryProjectile.getCPUInfantryProjectile().get(i).healthpoints >= 0 && CPUInfantryProjectile.getCPUInfantryProjectile().get(i).hitWall() == null) {
				CPUInfantryProjectile.getCPUInfantryProjectile().get(i).move();
				CPUInfantryProjectile.getCPUInfantryProjectile().get(i).draw(g);
				CPUInfantryProjectile.getCPUInfantryProjectile().get(i).range -= 1;
			CPUInfantryProjectile.die(CPUInfantryProjectile.getCPUInfantryProjectile().get(i));			
			}
			/** COULD INCREASE CODE EFFICIENCY BY GETTING RID OF BAD PROJECTILES HERE*/
		}

		if (playerbase != null) {
		if (playerbase.healthpoints >= 0) {
			playerbase.draw(g);
		} else {
			playerbase = null;
		}			
		}

		if (barracks != null) {
		if (barracks.healthpoints >= 0) {
			barracks.draw(g);
		} else {
			barracks = null;
		}			
		}

		if (cpu1 != null) {
		if (cpu1.healthpoints >= 0) {
		cpu1.draw(g);			
		} else {
			cpu1 = null;
		}			
		}

		if (cpu1barracks != null) {
		if (cpu1barracks.healthpoints >= 0) {
		cpu1barracks.draw(g);			
		} else {
			cpu1barracks = null;
		}			
		}


		MiniMap.draw(g);
		if (hero != null) {
			if (hero.healthpoints >= 0) {
				hero.draw(g);				
				} else if (hero.healthpoints < 0) {
				if (hero != null) {
					Hero.die(hero);
					}
				hero = null;
				}	
			}
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
		if (mousedrag == true) {
			g.setColor(Color.WHITE);
			Point point = MouseInfo.getPointerInfo().getLocation();
			int x = (int) point.getX();
			int y = (int) point.getY();
			g.drawRect(mousedragxstart, mousedragystart, x+SCROLL_X-mousedragxstart-7, y+SCROLL_Y-mousedragystart-30);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
