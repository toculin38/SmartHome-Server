import org.json.JSONObject;

public class TVState{
	
	private static int varSize = 3;
	private boolean enable;
	private boolean power;
	private int channel;
	
	public TVState(){
		enable = false;
		power = false;
		channel = 0;
	}
	
	public TVState(JSONObject json){
		enable = json.getBoolean("enable");
		power = json.getBoolean("power");
		channel = json.getInt("channel");
	}
	
	public void change(String command){
		switch(command){
			case "PowerOn":
				turnPower(true);
				break;
			case "PowerOff":
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
		s[2] = String.valueOf(getChannel());
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
	
	public int getChannel(){
		return channel;
	}
	
	public void setChannel(int channel){
		this.channel = channel;
	}
	
}