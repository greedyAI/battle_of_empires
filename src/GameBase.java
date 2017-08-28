/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;

/** A troop in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameBase {

	/** Current position of the object (in terms of graphics coordinates)
	 *  
	 * Coordinates are given by the upper-left hand corner of the object.
	 * This position should always be within bounds.
	 *  0 <= pos_x <= max_x 
	 *  0 <= pos_y <= max_y 
	 */
	public double pos_x; 
	public double pos_y;

	/** Size of object, in pixels */
	public int width;
	public int height;
	public int meleedamage;
	public int healthpoints;
	public int upgradecost;
	public String type;
	public int regeneration;
	public int armor;
	public int armordamage;
	

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;

	/**
	 * Constructor
	 */
	public GameBase(double pos_x, double pos_y, int width, int height, int court_width, int court_height,
		int healthpoints, int upgradecost, String type, int regeneration, int armor, int armordamage, int meleedamage){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
		this.healthpoints = healthpoints;
		this.upgradecost = upgradecost;
		this.type = type;
		this.regeneration = regeneration;
		this.armor = armor;
		this.armordamage = armordamage;
		this.meleedamage = meleedamage;
		
		// take the width and height into account when setting the 
		// bounds for the upper left corner of the object.
		this.max_x = court_width - width;
		this.max_y = court_height - height;

	}

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 */ 
	public void clip(){
		if (pos_x < 0) pos_x = 0;
		else if (pos_x > max_x) pos_x = max_x;

		if (pos_y < 0) pos_y = 0;
		else if (pos_y > max_y) pos_y = max_y;
	}

	/**
	 * Determine whether this game object is currently intersecting
	 * another object.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the 
	 * bounding boxes overlap, then an intersection is considered to occur.
	 * 
	 * @param obj : other object
	 * @return whether this object intersects the other object.
	 */
	public boolean intersects(GameTroop obj){
		return (pos_x + width >= obj.pos_x
				&& pos_y + height >= obj.pos_y
				&& obj.pos_x + obj.width >= pos_x 
				&& obj.pos_y + obj.height >= pos_y);
	}

	
	public void bounceDirection(GameTroop troop) {
			double dx = troop.pos_x + troop.width/2 - (pos_x + width/2 );
			double dy = troop.pos_y + troop.height/2 - (pos_y + height/2);
	        double distSquared = dx*dx + dy*dy;
	        if(distSquared <= 0.25* (Math.min(width, height) + Math.min(troop.width, troop.height))*(Math.min(width, height) + Math.min(troop.width, troop.height))) {
			double xVelocity = troop.v_x;
            double yVelocity = troop.v_y;
            double dotProduct = dx*xVelocity + dy*yVelocity;
            //Neat vector maths, used for checking if the objects moves towards one another.
            if(dotProduct < 0){
                double collisionScale = dotProduct / distSquared;
                double xCollision = dx * collisionScale;
                double yCollision = dy * collisionScale;
                //The Collision vector is the speed difference projected on the Dist vector,
                //thus it is the component of the speed difference needed for the collision.
                double combinedMass = 99999 + troop.mass;
                //double collisionWeightA = 2 * troop.mass / combinedMass;
                double collisionWeightB = 2 * 99999 / combinedMass;
                troop.v_x -= collisionWeightB * xCollision;
                troop.v_y -= collisionWeightB * yCollision;
                troop.v_x *= 0.75;
               troop.v_y *= 0.75;
            }
            if(GameCourt.friendlyfire == false && distSquared <= 0.25* (Math.min(width, height) + Math.min(troop.width, troop.height))*(Math.min(width, height) + Math.min(troop.width, troop.height))) {
            	troop.healthpoints -= meleedamage;
            	healthpoints = healthpoints - troop.damage;
            }
			//System.out.println(healthpoints);
	       }
			//v_x = (v_x * mass + troop.v_x * troop.mass + troop.mass * 0.6 * (troop.v_x - v_x)) / (mass + troop.mass);
			//v_y = (v_y * mass + troop.v_y * troop.mass + troop.mass * 0.6 * (troop.v_y - v_y)) / (mass + troop.mass);
		}
	
	
	

	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public void draw(Graphics g) {
	}
	
}
