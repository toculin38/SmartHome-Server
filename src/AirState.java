import org.json.JSONObject;

public class AirState{
	
	private static int varSize = 4;
	private boolean enable;
	private boolean power;
	private int temperature;
	private int wind;
	
	public AirState(){
		enable = false;
		power = false;
		temperature = 0;
	}
	
	public AirState(JSONObject json){
		enable = json.getBoolean("enable");
		power = json.getBoolean("power");
		temperature = json.getInt("temperature");
		wind = json.getInt("wind");
	}
	
	public void change(String command){
		switch(command){
			case "AirOn":
				turnPower(true);
				break;
			case "AirOff":
				turnPower(false);
				break;
		}
	}
	
	public void turnPower(boolean value){
		power = value;
	}
	
	public String[] states(){
		String[] s = new String[varSize];
		s[0] = String.valueOf(getEnable());
		s[1] = String.valueOf(getPower());
		s[2] = String.valueOf(getTemperature());
		s[3] = String.valueOf(getWind());
		return s;
	}
	
	public boolean getEnable(){
		return enable;
	}
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	public boolean getPower(){
		return power;
	}
	
	public void setPower(boolean power){
		this.power = power;
	}
	
	public int getTemperature(){
		return temperature;
	}
	
	public void setTemperature(int temperature){
		this.temperature = temperature;
	}
	
	public int getWind(){
		return wind;
	}
	
	public void setWind(int wind){
		this.wind = wind;
	}
	
	
}