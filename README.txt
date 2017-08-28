=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays: This concept implements the mini-map located at the bottom left hand corner of the game screen. This is an appropriate use of arrays
  because I had to scale down the whole playing map into a smaller map, so I had to convert the locations of all of the living units on the giant battle map (which I stored in an array) and scale them down to relative locations
  on the mini-map. In essence, I took in the locations of all the units and buildings on the field and painted dots on the minimap in the corresponding locations. For every 10th call of the tick() function,
  I updated the array containing the locations of each unit and the buildings and repainted the array to update the minimap.

  2. Collections: I used LinkedLists to store collections of each type of troop and each type of bullet (every unit had a list of its own projectiles). These collections were critical to my implementation because I often looped through every element
  of a collection or a pair of collections to check for collision, the movement of the units, as well as implementing the AI (which applies to all units at the same time).

  3.Inheritance/Subtyping: Each one of my troops/bullets had around 30 instance variables associated with it, to store the data that is associated with it at any given time. The GameTroop.java class is the superclass to each type of unit and each type
  of projectile in this game. The GameBase.java class is the superclass to each type of building in this game. Without them, programming each type of object in the game would have been much much uglier.

  4. AI: I programmed a total of 7 AI algorithms. 6 of them are usable by the computer AI, and a different set of 6 of them are usable by the player AI. 5 of the computer AI (and 4 of the player AI) controls all of the non-hero units on one side of the
  battlefield at once (they include bullet dodging ability, swarm attack ability, base attack ability, base defense ability...etc.). I also programmed an AI algorithm for the player's Hero unit, which allows the player to not command it and watch the hero
  inflict havoc upon the computer AI. Obviously, more game balancing may be needed if one intends to play this game seriously.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

ArmorRam: a type of player unit
Barracks: a type of player building
ComputerBarracks: a type of computer building
ComputerBase: a type of computer building
ComputerInfantry: a type of computer unit
CPUInfantryProjectile: the projectiles associated with the ComputerInfantry units
Game: main class--defines the opening screen, instruction panel, and settings panel
GameBase: superclass of all types of buildings
GameCourt: the JPanel containing the game
GameTroop: superclass of all types of units
Hero: a type of player unit
HeroMissile: the projectiles associated with the Hero unit
Infantry: a type of player unit
InfantryProjectile: the projectiles associated with the Infantry unit
MiniMap: implements the minimap feature
PlayerBase: a type of player building


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Not really. But it was tedious and took a long time (many of the algorithms were difficult to implement and I had to introduce many instance/static variables to implement them)


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

There is good separation of functionality because it's quite easy to add new types of troops and buildings. Due to the complexity of the game, private states are only thoroughly used in the main GameCourt class. However, instance variables of most
objects were declared upon construction and made private. If I was to refactor, I would change the algorithms to make them less repetitive and improve their efficiency (looping through many LinkedLists can be very inefficient).

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

The background images were the only sources I used. One of them was a stock image online and one was off of Google Maps.
