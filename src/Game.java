// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {

	public static boolean speed1case = true;
	public static boolean speed15case = false;
	public static boolean speed2case = false;
	
	public void run() {
		// NOTE : recall that the 'final' keyword notes immutability
		// even for local variables.
		// Top-level frame in which game components live
		// Be sure to change "TOP LEVEL FRAME" to the name of your game
		final JFrame frame = new JFrame("Battle of Empires");
		frame.setLayout(new BorderLayout());
		frame.setContentPane(new JLabel(new ImageIcon("Background.jpg")));
		frame.setLayout(new GridBagLayout());

		frame.setLocation(0, 0);

		// Status panel
		//final JPanel status_panel = new JPanel();
		//frame.add(status_panel, BorderLayout.SOUTH);
		//final JLabel status = new JLabel("Running...");
		//status_panel.add(status);

		
		final JButton startgame = new JButton("Start Game");
		startgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//startgame.setText("Loading...");
				startgame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				final GameCourt court = new GameCourt();
				frame.setContentPane(court);
				frame.validate();
				court.reset();
			}
		});

		final JPanel start_panel = new JPanel();
		start_panel.setLayout(new BorderLayout(0, 100));		
		JLabel title = new JLabel("Battle of Empires");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.RED);
		title.setFont(new Font("Rockwell", Font.BOLD, 64));
		JLabel subtitle = new JLabel("A battle to create a New World Order");
		subtitle.setForeground(Color.BLUE);
		subtitle.setHorizontalAlignment(JLabel.CENTER);
		subtitle.setFont(new Font("Rockwell", Font.BOLD, 36));
	   	start_panel.add(title, BorderLayout.NORTH);
	   	start_panel.add(subtitle, BorderLayout.CENTER);
	   	start_panel.setOpaque(false);

	   	
	   	final JPanel startbuttons = new JPanel();
	   	JButton Instructions = new JButton("Instructions");
	   	Instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                JFrame instructions_panel = new JFrame("Instructions");
                instructions_panel.setLayout(new BorderLayout(0, 0));		
        		JLabel instructions_title = new JLabel("Instructions");
        		instructions_title.setHorizontalAlignment(JLabel.CENTER);
        		instructions_title.setForeground(Color.BLACK);
        		instructions_title.setFont(new Font("Rockwell", Font.BOLD, 32));
        		JLabel instructions = new JLabel("<html>" + "In this game, you and the computer both start at your own army base on opposite corners of a large battlefield. " + 
        										 "Your goal is to use the cash that you gain over time to train troops (you have a choice between a standard infantry unit and a tank ramming unit) and then command those troops to destroy " + 
        										 "the computer's base before it destroys yours. You also have a powerful hero at your command. You can control your hero's movement using the WASD keys and then using your left mouse button to shoot, or you can delegate the command of your hero to the AI. To control other units, click on them or drag a rectangle " +
        										 "on top of them, and then use the right mouse button to deploy them whereever you want. You can also command all of your (non-hero) troops at once using one of 5 general commands and 1 special skill. " + 
        										 "The computer's AI changes depending on your actions, so make sure to deploy your units strategically in order to " +
        										 "destroy the computer's units and base quickly. You cannot train new heros during the battle. However, we have given your hero a purchasable Heal skill. " + 
        										 "When your Hero's health is low and you have the money to purchase the Heal skill, you should consider healing him. You can scroll in all four directions of the warzone by hovering your mouse near the edge of the screen or by using the Up, Down, Left, and Right keys. A minimap is available on the bottom left hand corner for your military intelligence purposes." + "</html>");
        		instructions.setForeground(Color.BLACK);
        		instructions.setHorizontalAlignment(JLabel.CENTER);
        		Border border = instructions.getBorder();
        		Border margin = new EmptyBorder(20,20,20,20);
        		instructions.setBorder(new CompoundBorder(border, margin));
        		instructions.setFont(new Font("Rockwell", Font.PLAIN, 18));
        			
        		instructions_panel.add(instructions_title, BorderLayout.NORTH);
        		instructions_panel.add(instructions, BorderLayout.CENTER);
        		instructions_panel.setSize(800, 800);
        		instructions_panel.setLocationByPlatform(true);
        		instructions_panel.setVisible(true);
			}
		});
	   	
	   	
	   	final JPanel speedsettings = new JPanel();
   		JRadioButton speed1 = new JRadioButton("Speed 1x");
	    JRadioButton speed15 = new JRadioButton("Speed 1.5x");
	    JRadioButton speed2 = new JRadioButton("Speed 2x");
	    ButtonGroup bG = new ButtonGroup();
	     bG.add(speed1);
	     bG.add(speed15);
	     bG.add(speed2);
	     speedsettings.setLayout( new FlowLayout());
	     speedsettings.setSize(100, 200);
	     speed1.setSelected(true);
	     speedsettings.add(speed1);  
	     speedsettings.add(speed15);
	     speedsettings.add(speed2);
	   	
	   	Instructions.setFont(new Font("Rockwell", Font.BOLD, 24));
	   	JButton Settings = new JButton("Settings");
	   	Settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                JFrame settings_panel = new JFrame("Settings");
                settings_panel.setLayout(new BorderLayout());		
        		JLabel settings_title = new JLabel("Settings");
        		settings_title.setHorizontalAlignment(JLabel.CENTER);
        		settings_title.setForeground(Color.BLACK);
        		settings_title.setFont(new Font("Rockwell", Font.BOLD, 32));
        		JLabel instructions = new JLabel("<html>" + "Feel free to change the speed of the game using the buttons below. Once you start the game, changing the settings below will have no effect." + "</html>");
        		instructions.setForeground(Color.BLACK);
        		instructions.setHorizontalAlignment(JLabel.CENTER);
        		Border border = instructions.getBorder();
        		Border margin = new EmptyBorder(20,20,20,20);
        		instructions.setBorder(new CompoundBorder(border, margin));
        		instructions.setFont(new Font("Rockwell", Font.PLAIN, 18));  			
        		settings_panel.add(settings_title, BorderLayout.NORTH);
        		settings_panel.add(instructions, BorderLayout.CENTER);
        		settings_panel.add(speedsettings, BorderLayout.SOUTH);        		
        		settings_panel.setSize(650, 400);
        		settings_panel.setLocationByPlatform(true);
        		settings_panel.setVisible(true);
        		settings_panel.addWindowListener(new WindowAdapter()
        		{
        		    public void windowClosing(WindowEvent e)
        		    {
        	        speed1case = speed1.isSelected();
        	    	speed15case = speed15.isSelected();
        	    	speed2case = speed2.isSelected();	
        		    }       		    	
        		});
			}
		});
	   	
	   	
	   	Settings.setFont(new Font("Rockwell", Font.BOLD, 24));
	   	startgame.setFont(new Font("Rockwell", Font.BOLD, 24));
	   	startbuttons.add(Instructions);
	   	startbuttons.add(startgame);
	   	startbuttons.add(Settings);
	   	
	   	start_panel.add(startbuttons, BorderLayout.SOUTH);
	   	startbuttons.setOpaque(false);
	   	
		frame.getContentPane().add(start_panel, new GridBagConstraints());
	
		
		// Main playing area
		//final GameCourt court = new GameCourt();
		//frame.add(court, BorderLayout.CENTER);

		// Reset button
		//final JPanel control_panel = new JPanel();
		//frame.add(control_panel, BorderLayout.NORTH);
		
		//final JPanel sidebar_panel = new JPanel();
		//frame.add(sidebar_panel, BorderLayout.WEST);
		
		//final JButton reset1 = new JButton("Reset");
		//reset1.addActionListener(new ActionListener() {
			//public void actionPerformed(ActionEvent e) {
				//court.reset();
			//}
		//});
		//sidebar_panel.add(reset1);

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		/**final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});
		control_panel.add(reset);*/

		// Put the frame on the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		//court.reset();
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
	
	
}
