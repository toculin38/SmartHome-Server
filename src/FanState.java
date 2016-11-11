import org.json.JSONObject;

public class FanState{
	
	private static int varSize = 2;
	private boolean enable;
	private int speed;
	
	public FanState(){
		enable = false;
		speed = 0;
	}
	
	public FanState(JSONObject json){
		enable = json.getBoolean("enable");
		speed = json.getInt("speed");
	}
	
	public void change(String command){
		switch(command){
			case "speed0":
				setSpeed(0);
				break;
			case "speed1":
				setSpeed(1);
				break;
			case "speed2":
				setSpeed(2);
				break;
			case "speed3":
				setSpeed(3);
				break;
		}
	}
	
	
	public String[] states(){
		String[] s = new String[varSize];
		s[0] = String.valueOf(getEnable());
		s[1] = String.valueOf(getSpeed());
		return s;
	}
	
	public boolean getEnable(){
		return enable;
	}
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	
	public int getSpeed(){
		return speed;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
}