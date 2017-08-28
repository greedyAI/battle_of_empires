/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;
import java.util.LinkedList;

/** A troop in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameTroop {

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
	public int mass;
	public int width;
	public int height;
	public int healthpoints;
	public int cost;
	public int damage;
	public String type;
	public int regeneration;
	public int projectilespeed;
	public int projectiledamage;
	public int reload;
	public int range;
	public int armor;
	public int clockcounterarmordamage;
	public int trainingtime;
	public String favoritetarget;
	public boolean splash;
	public int upkeep;
	public double movespeed;
	public boolean usercontrolled;
	public LinkedList<GameTroop> projectilelist;
	public double bulletvx;
	public double bulletvy;
	public double destination_x;
	public double destination_y;
	public boolean destinationreached;
	
	public static boolean hit = false;
	
	static LinkedList<GameTroop> playerunitlist = new LinkedList<GameTroop>();
	

	/** Velocity: number of pixels to move every time move() is called */
	public double v_x;
	public double v_y;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;

	/**
	 * Constructor
	 */
	public GameTroop(double v_x, double v_y, double pos_x, double pos_y, 
		int width, int height, int court_width, int court_height,
		int healthpoints, int cost, int damage, String type,
		int regeneration, int projectilespeed, int projectiledamage,
		int reload, int range, int armor, int clockcounterarmordamage, int trainingtime,
		String favoritetarget, boolean splash, int upkeep, int mass, double movespeed, boolean usercontrolled, LinkedList<GameTroop> projectilelist,
		double bulletvx, double bulletvy, double destination_x, double destination_y, boolean destinationreached){
		this.v_x = v_x;
		this.v_y = v_y;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
		this.healthpoints = healthpoints;
		this.cost = cost;
		this.damage = damage;
		this.type = type;
		this.regeneration = regeneration;
		this.projectilespeed = projectilespeed;
		this.projectiledamage = projectiledamage;
		this.reload = reload;
		this.range = range;
		this.armor = armor;
		this.clockcounterarmordamage = clockcounterarmordamage;
		this.trainingtime = trainingtime;
		this.favoritetarget = favoritetarget;
		this.splash = splash;
		this.upkeep = upkeep;
		this.mass = mass;
		this.movespeed = movespeed;
		this.usercontrolled = usercontrolled;
		this.bulletvx = bulletvx;
		this.bulletvy = bulletvy;
		this.destination_x = destination_x;
		this.destination_y = destination_y;
		this.destinationreached = destinationreached;
		
		// take the width and height into account when setting the 
		// bounds for the upper left corner of the object.
		this.max_x = court_width - width;
		this.max_y = court_height - height;
		this.projectilelist = projectilelist;

	}


	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public void move(){
		pos_x += v_x;
		pos_y += v_y;
		
		if (this.usercontrolled == true && this.destinationreached == false) {
			double unitx = this.pos_x + this.width/2;
			double unity = this.pos_y + this.height/2;
			double dx = this.destination_x - unitx;
			double dy = this.destination_y - unity;
			double distance = Math.sqrt(dx*dx + dy*dy);
			this.v_x = this.movespeed * dx/ distance;
			this.v_y = this.movespeed * dy/ distance;
			if (distance <= 2*this.movespeed) {
			this.v_x *= 0;
			this.v_y *= 0;
			this.usercontrolled = false;
			this.destinationreached = true;
			}								
			}

		clip();
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

	
	/**
	 * Determine whether this game object will intersect another in the
	 * next time step, assuming that both objects continue with their 
	 * current velocity.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the 
	 * bounding boxes (for the next time step) overlap, then an 
	 * intersection is considered to occur.
	 * 
	 * @param obj : other object
	 * @return whether an intersection will occur.
	 */
	public boolean willIntersect(GameTroop obj){
		double next_x = pos_x + v_x;
		double next_y = pos_y + v_y;
		double next_obj_x = obj.pos_x + obj.v_x;
		double next_obj_y = obj.pos_y + obj.v_y;
		return (next_x + width >= next_obj_x
				&& next_y + height >= next_obj_y
				&& next_obj_x + obj.width >= next_x 
				&& next_obj_y + obj.height >= next_y);
	}

	
	/** Update the velocity of the object in response to hitting
	 *  an obstacle in the given direction. If the direction is
	 *  null, this method has no effect on the object. */
	public void bounce(Direction d) {
		if (d == null) return;
		switch (d) {
		case UP:    v_y = Math.abs(v_y) *0.6; v_x *= 0.6; break;  
		case DOWN:  v_y = -Math.abs(v_y) *0.6; v_x *= 0.6; break;
		case LEFT:  v_x = Math.abs(v_x) *0.6; v_y *= 0.6; break;
		case RIGHT: v_x = -Math.abs(v_x) *0.6; v_y *= 0.6; break;
		}
	}
	
	/** Determine whether the game object will hit a 
	 *  wall in the next time step. If so, return the direction
	 *  of the wall in relation to this game object.
	 *  
	 * @return direction of impending wall, null if all clear.
	 */
	public Direction hitWall() {
		if (pos_x + v_x < 0) {
			return Direction.LEFT;}
		else if (pos_x + v_x > max_x) {
			return Direction.RIGHT;}
		if (pos_y + v_y < 0) {
			return Direction.UP;}
		else if (pos_y + v_y > max_y) {
			return Direction.DOWN;}
		else return null;
	}

	/** Determine whether the game object will hit another 
	 *  object in the next time step. If so, return the direction
	 *  of the other object in relation to this game object.
	 *  
	 * @return direction of impending object, null if all clear.
	 */
	
		public Direction hitObj(GameTroop other) {

		if (this.willIntersect(other)) {
			double dx = other.pos_x + other.width /2 - (pos_x + width /2);
			double dy = other.pos_y + other.height/2 - (pos_y + height/2);

			double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy *dy)));
			double diagTheta = Math.atan2(height / 2, width / 2);

   if (theta <= diagTheta ) {
     return Direction.RIGHT;
   } else if ( theta > diagTheta && theta <= Math.PI - diagTheta ) {
     if ( dy > 0 ) {
       // Coordinate system for GUIs is switched
       return Direction.DOWN;
     } else {
       return Direction.UP;
     }
   } else {
     return Direction.LEFT;
   }
		} else {
			return null;
		}

	}
	
		public static LinkedList<GameTroop> getPlayerUnitList() {
			return playerunitlist;
		}	
		
	public void bounceDirection(GameTroop troop) {
			double dx = troop.pos_x + troop.width/2 - (pos_x + width/2 );
			double dy = troop.pos_y + troop.height/2 - (pos_y + height/2);
	        double distSquared = dx*dx + dy*dy;
	        if(distSquared <= 0.25* (Math.min(width, height) + Math.min(troop.width, troop.height))*(Math.min(width, height) + Math.min(troop.width, troop.height))) {
			hit = true;
	        double xVelocity = troop.v_x - v_x;
            double yVelocity = troop.v_y - v_y;
            double dotProduct = dx*xVelocity + dy*yVelocity;
            //Vector mathematics
            if(dotProduct < 0){
                double collisionScale = dotProduct / distSquared;
                double xCollision = dx * collisionScale;
                double yCollision = dy * collisionScale;
                double combinedMass = mass + troop.mass;
                double collisionWeightA = 2 * troop.mass / combinedMass;
                double collisionWeightB = 2 * mass / combinedMass;
                v_x += collisionWeightA * xCollision;
                v_y += collisionWeightA * yCollision;
                troop.v_x -= collisionWeightB * xCollision;
                troop.v_y -= collisionWeightB * yCollision;
                if (this instanceof ArmorRam || troop instanceof ArmorRam) {
                } else {
                 v_x *= 0.6;
                v_y *= 0.6;
                troop.v_x *= 0.6;
                troop.v_y *= 0.6;               	
                }
            }
            if (GameCourt.friendlyfire == false) {
            	healthpoints = healthpoints - troop.damage;
            	troop.healthpoints = troop.healthpoints - damage;
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
