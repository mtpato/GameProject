package gameProject;

public class Vehicle{
	
	int health;
	int speed;
	int owner;
	
	int orderInHand;
	
	float angle;
	float xLocation;
	float yLocation;
	
	
	
	boolean active=false;
	
	
	public Vehicle(int owner,float xLocation,float yLocation,int orderInHand,float angle) 
	{
		
		this.owner = owner;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.orderInHand=orderInHand;
		this.angle=angle;
		
	}
	
	
	
}




















