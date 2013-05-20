package gameProject;



public class Vehicle{
	
	int health;
	int speed;
	int owner;
	int VehicleType;
	int orderInHand;
	
	float angle;
	float xLocation;
	float yLocation;
	
	boolean active=false;
	
	
	public Vehicle(int owner, int health,int speed,int VehicleType,float xLocation,float yLocation,int orderInHand,float angle) 
	{
		
		this.health=health;
		this.speed = speed;
		this.owner = owner;
		this.VehicleType=VehicleType;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.orderInHand=orderInHand;
		this.angle=angle;
		
	}
	
	
}

