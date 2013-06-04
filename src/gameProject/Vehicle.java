package gameProject;

import java.util.HashMap;


//Class that defines the cards that people play in the game


public class Vehicle{
	
	int owner;//which player controls the vehicle
	int orderInHand;//the order in which it must be played
	double angle;//its angle with respect to the screen; will be used in determining hits
	double xLocation;//where it is on map
	double yLocation;//where it is on the map
	
	//the types of vehicles available
	public enum vehicleType 
	{OneCar,TwoCar,ThreeCar,FourCar,FiveCar,SixCar,SevenCar,EightCar,NineCar,TenCar;}

	int health;//How much damage a vehicle can sustain before it is destroyed
	int speed;//determines the order in which the vehicles fire at the end of the round
	double size;
	
	//this is an array that holds the weapons
	HashMap<Integer,Weapon> weapons;
	
	//this determines the vehicle's type...that is, what weapons it has, what angles they fire at, health, speed etc..
	vehicleType type;
	
	//whether it is in the hand or on the board
	boolean active;
	
	//constructor
	public Vehicle(int owner,vehicleType type, double xLocation,double yLocation,int orderInHand,double angle) 
	{
		this.owner = owner;
		this.type=type;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.orderInHand=orderInHand;
		this.angle=angle;
		this.active=false;
		this.size=.01;
		this.weapons=new HashMap<Integer,Weapon>();
		//attributes of different types are set when the vehicles is instantiated
		//long term we may want separate classes, but this makes game logic much easier
		//as long as vehicles don't have distinct attribute types this is better
		
		if(type==vehicleType.OneCar)
		{
			this.health=1;
			this.speed=1;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.TwoCar)
		{
			this.health=1;
			this.speed=2;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.ThreeCar)
		{
			this.health=1;
			this.speed=3;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			this.weapons.put(2, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.FourCar)
		{
			this.health=2;
			this.speed=4;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.FiveCar)
		{
			this.health=2;
			this.speed=5;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.SixCar)
		{
			this.health=2;
			this.speed=6;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.SevenCar)
		{
			this.health=3;
			this.speed=7;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.EightCar)
		{
			this.health=3;
			this.speed=8;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.NineCar)
		{
			this.health=4;
			this.speed=9;
			
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		if(type==vehicleType.TenCar)
		{
			this.health=5;
			this.speed=10;
			
					
			this.weapons.put(1, new Weapon(.4,0,2.0,1));
			
		};
		
		
	}
	

	//private class that defines the weapons attributes

	public class Weapon
	{

		double WeaponPosition;
		double WeaponAngle;
		double WeaponPower;
		double WeaponRange;
		
		private Weapon(double position,double angle,double WeaponPower,double WeaponRange)
		{
			this.WeaponPosition=position;
			this.WeaponAngle=angle;
			this.WeaponPower=WeaponPower;
			this.WeaponRange=WeaponRange;
		}
		
	}
	
	public void changeLocation(double xlocation,double ylocation,double angle)
	{
		this.xLocation=xlocation;
		this.yLocation=ylocation;
		this.angle=angle;
	}
	
	public void makeInactive()
	{
		this.active=false;
	}
	
	
	
	public void shootWeapons(CarWarsGameState s,Vehicle shooter)
	{


		

		
			//then through all weapons for that vehicle
			for(Weapon w:shooter.weapons.values())
			{
				
		
				

			}
			
		

		
	}
	
	
	private double[] rotate(double x,double y, double angle)
	{
		
		double[] coords = new double[2];
		
		coords[0]=x*Math.cos(angle)-y*Math.sin(angle);
		coords[1]=x*Math.sin(angle)+y*Math.cos(angle);
		
		return coords;
	}
	

	

	
	
}




















