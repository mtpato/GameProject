package gameProject;




import gameProject.Vehicle.vehicleType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;



public class CarWarsModel extends GameModel{
	
	Random rand = new Random();
	
	@Override
	protected GameState makeMove(GameState state, String move) {
		
		CarWarsGameState s = (CarWarsGameState) state;
		
		List<String> items = Arrays.asList(move.split(";"));
		
		String moveID=items.get(0);
		Double xlocation=Double.parseDouble(items.get(1));
		Double ylocation=Double.parseDouble(items.get(2));
		
		
		Vehicle moveVehicle = s.vehicles.get(moveID);
		
		
	
		moveVehicle.changeLocation(xlocation, ylocation);
		moveVehicle.makeInactive();
	
		if(isOver(s)) s.over = true;
		
		return s;
	}

	
	
		
	
	@Override
	protected boolean isOver(GameState state) {
		
		CarWarsGameState s = (CarWarsGameState) state;
		
		
		for(int i: s.players)
		{
			int counter=0;
			
				for(Vehicle v:s.vehicles.values()){
					
					if(!v.active) counter=counter+1;
					if(counter>s.numVehicles-1){return true;};
					
					}
				
			}
		
		
		return false;
	
	}

	
	
	
	
	
	
	
	@Override
	protected GameState parseGameState(String s) {
		
		
		
		
		return null;
	}

	
	
	
	
	
	
	
	@Override
	protected String compressGameState(GameState state) {
		
		
		CarWarsGameState s = (CarWarsGameState) state;
		
		String cs="";
		

		for(int i:s.players){
			
			System.out.println(i);
		
		cs=cs+"{"+i+"}"+";";
		
			for(int j = 1; j < s.numVehicles+1; j++) 
			{
			
				Vehicle currentVehicle=s.vehicles.get(i+"-"+j);
				
				
				cs=cs+currentVehicle.health+","+
						currentVehicle.speed+","+
						currentVehicle.xLocation+","+
						currentVehicle.yLocation+","+
						currentVehicle.orderInHand+","+
						currentVehicle.angle
						+";";
						//System.out.println(cs);
			
			}
		
		}
	    
		
		
		
		
		
		return cs;
	}

	
	
	
	
	
	
	@Override
	protected int winner(GameState state) {
		
		// for k types of ships:
			// find lowest numbers on the board
			// fire their guns
			// remove dead ships
			// tally up points
		
		
		
		
		return 0;
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected GameState createNewGame(Set<Integer> users) {
		
		System.out.println("CREATING GAME ");
		
		CarWarsGameState state = new CarWarsGameState(users, 1, 1,10);
		
		for(int p: state.players) {
			state.scores.put(p, 0);//start with scores set to 0
		}

		
		//initialize the vehicles in the 
		createVehicles(state);
		
		
		
		return state;
	}
	
	
	

	@Override
	protected GameState createNewGame(Set<Integer> users, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	@Override
	protected String whatGame() {
		// TODO Auto-generated method stub
		return "Car Wars";
	}

	
	
	
	
	
	@Override
	protected void printState(GameState state) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
	
	
	private void createVehicles(CarWarsGameState state) 
	{
		
		Integer[] order =randomizeVector(state.numVehicles);
		
		
		for(int p: state.players) 
		{
				
				state.vehicles.put(p+"-"+1,new Vehicle(p,vehicleType.OneCar,0,0,order[0]+1, 0));
				state.vehicles.put(p+"-"+2,new Vehicle(p,vehicleType.TwoCar,0,0,order[1]+1, 0));
				state.vehicles.put(p+"-"+3,new Vehicle(p,vehicleType.ThreeCar,0,0,order[2]+1, 0));
				state.vehicles.put(p+"-"+4,new Vehicle(p,vehicleType.FourCar,0,0,order[3]+1, 0));
				state.vehicles.put(p+"-"+5,new Vehicle(p,vehicleType.FiveCar,0,0,order[4]+1, 0));
				state.vehicles.put(p+"-"+6,new Vehicle(p,vehicleType.SixCar,0,0,order[5]+1, 0));
				state.vehicles.put(p+"-"+7,new Vehicle(p,vehicleType.SevenCar,0,0,order[6]+1, 0));
				state.vehicles.put(p+"-"+8,new Vehicle(p,vehicleType.EightCar,0,0,order[7]+1, 0));
				state.vehicles.put(p+"-"+9,new Vehicle(p,vehicleType.NineCar,0,0,order[8]+1, 0));
				state.vehicles.put(p+"-"+10,new Vehicle(p,vehicleType.TenCar, 0,0,order[9]+1, 0));

		}
		
		
	}
	
	

	
	private Integer[] randomizeVector(int vecLength)
	{
		
		
		
		Integer[] order=new Integer[vecLength];
		
		for(int j = 0; j < vecLength; j++) {	
			order[j]=j;
			}

		System.out.println(order);
		
		Integer[] ids =order;
		List<Integer> idList = Arrays.asList(ids);
		Collections.shuffle(idList);
		ids = idList.toArray(ids);
		
		
		return ids;
	}

}
