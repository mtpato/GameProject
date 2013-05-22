package gameProject;



import gameProject.VehicleTypes.EightCar;
import gameProject.VehicleTypes.FiveCar;
import gameProject.VehicleTypes.FourCar;
import gameProject.VehicleTypes.NineCar;
import gameProject.VehicleTypes.OneCar;
import gameProject.VehicleTypes.SevenCar;
import gameProject.VehicleTypes.SixCar;
import gameProject.VehicleTypes.TenCar;
import gameProject.VehicleTypes.ThreeCar;
import gameProject.VehicleTypes.TwoCar;

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
		Float xlocation=Float.parseFloat(items.get(1));
		Float ylocation=Float.parseFloat(items.get(2));
		

		Vehicle moveVehicle = s.vehicles.get(moveID);
		
		moveVehicle.xLocation=xlocation;
		moveVehicle.yLocation=ylocation;
		moveVehicle.active=true;
	
		if(isOver(s)) s.over = true;
		
		return s;
	}

	
	
	
	
	
	
	
	@Override
	protected boolean isOver(GameState state) {
		
		CarWarsGameState s = (CarWarsGameState) state;
		
		
		for(int i = 0; i < s.players.size(); i++)
		{
			int counter=0;
			
				for(int j = 0; j < s.numVehicles; j++)
				{
				
					Vehicle currentVehicle=s.vehicles.get((i+1)+"-"+j);
					
					if(currentVehicle.active=true){counter=counter+1;};
					
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
		
		Iterator<Integer> it=s.players.iterator();
		int playerID = 0;
		
		
		for(int i = 0; i < s.players.size(); i++){
			
		playerID=it.next();
		
		cs=cs+"{"+playerID+"}"+":";
		
			for(int j = 0; j < s.numVehicles; j++) 
			{
			
				Vehicle currentVehicle=s.vehicles.get(playerID+"-"+j);
				
				cs=cs+currentVehicle.health+","+
						currentVehicle.speed+","+
						currentVehicle.xLocation+","+
						currentVehicle.yLocation+","+
						currentVehicle.orderInHand
						+"-";
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
				
				state.vehicles.put(p+"-"+1,new OneCar(p,0,0,order[0]+1, 0));
				state.vehicles.put(p+"-"+2,new TwoCar(p,0,0,order[1]+1, 0));
				state.vehicles.put(p+"-"+3,new ThreeCar(p,0,0,order[2]+1, 0));
				state.vehicles.put(p+"-"+4,new FourCar(p,0,0,order[3]+1, 0));
				state.vehicles.put(p+"-"+5,new FiveCar(p,0,0,order[4]+1, 0));
				state.vehicles.put(p+"-"+6,new SixCar(p,0,0,order[5]+1, 0));
				state.vehicles.put(p+"-"+7,new SevenCar(p,0,0,order[6]+1, 0));
				state.vehicles.put(p+"-"+8,new EightCar(p,0,0,order[7]+1, 0));
				state.vehicles.put(p+"-"+9,new NineCar(p,0,0,order[8]+1, 0));
				state.vehicles.put(p+"-"+10,new TenCar(p,0,0,order[9]+1, 0));

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