
public class DataManager {
	private TVState TV;
	
	public DataManager(){
		TV = new TVState();
	}
	
	public void changeState(String command){
		switch(command){
			case "power":
				TV.turnPower();
				break;
		}
	}
	
	public String[] getStates(String type){
		switch(type){
			case "TV":
				return TV.getStates();
			default:
				return null;
		}
		
	}
}
