package gameProject;

public class Vehicle{
	
	int health;
	int speed;
	int owner;
	int orderInHand;
	float angle;
	float xLocation;
	float yLocation;
	
	public enum vehicleType {OneCar,TwoCar,ThreeCar,FourCar,FiveCar,SixCar,SevenCar,EightCar,NineCar,TenCar};
	
	vehicleType type;
	
	boolean active=false;
	
	
	public Vehicle(int owner,vehicleType type, float xLocation,float yLocation,int orderInHand,float angle) 
	{
		
		
		this.owner = owner;
		this.type=type;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.orderInHand=orderInHand;
		this.angle=angle;
		
		
		
	}
	
	
	
}




















