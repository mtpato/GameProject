package gameProject;


//Class that defines the cards that people play in the game


public class Vehicle{
	
	int owner;//which player controls the vehicle
	int orderInHand;//the order in which it must be played
	float angle;//its angle with respect to the screen; will be used in determining hits
	double xLocation;//where it is on map
	double yLocation;//where it is on the map
	
	
	
	//the types of vehicles available
	public enum vehicleType {OneCar,TwoCar,ThreeCar,FourCar,FiveCar,SixCar,SevenCar,EightCar,NineCar,TenCar};

	int health;//How much damage a vehicle can sustain before it is destroyed
	int speed;//determines the order in which the vehicles fire at the end of the round
	
	
	int numWeapons;
	Weapon[] armament=new Weapon[numWeapons];
	
	
	
	
	
	//this determines the vehicle's type...that is, what weapons it has, what angles they fire at, health, speed etc..
	vehicleType type;
	
	//whether it is in the hand or on the board
	boolean active=false;
	
	//constructor
	public Vehicle(int owner,vehicleType type, float xLocation,float yLocation,int orderInHand,float angle) 
	{
		this.owner = owner;
		this.type=type;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.orderInHand=orderInHand;
		this.angle=angle;
		
		//attributes of different types are set when the vehicles is instantiated
		//long term we may want separate classes, but this makes game logic much easier
		//as long as vehicles don't have distinct attribute types this is better
		
		if(type==vehicleType.OneCar)
		{
			this.health=1;
			this.speed=1;
			this.numWeapons=1;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		

		
		if(type==vehicleType.TwoCar)
		{
			this.health=1;
			this.speed=2;
			this.numWeapons=1;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.ThreeCar)
		{
			this.health=1;
			this.speed=3;
			this.numWeapons=2;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.FourCar)
		{
			this.health=2;
			this.speed=4;
			this.numWeapons=2;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.FiveCar)
		{
			this.health=2;
			this.speed=5;
			this.numWeapons=2;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.SixCar)
		{
			this.health=2;
			this.speed=6;
			this.numWeapons=3;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.SevenCar)
		{
			this.health=3;
			this.speed=7;
			this.numWeapons=3;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.EightCar)
		{
			this.health=3;
			this.speed=8;
			this.numWeapons=4;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.NineCar)
		{
			this.health=4;
			this.speed=9;
			this.numWeapons=4;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		if(type==vehicleType.TenCar)
		{
			this.health=5;
			this.speed=10;
			this.numWeapons=5
					;
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			this.armament[0]=new Weapon(.4,.21212,2.0);
			
		};
		
		
	}
	


	private class Weapon
	{

		double WeaponPosition;
		double WeaponAngle;
		double WeaponPower;
		
		private Weapon(double position,double angle,double WeaponPower)
		{
			this.WeaponPosition=position;
			this.WeaponAngle=angle;
			this.WeaponPower=WeaponPower;
		}
		
	}
	
	
}




















