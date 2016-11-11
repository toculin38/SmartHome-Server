import org.json.JSONObject;

public class LightState{
	
	private static int varSize = 2;
	private boolean enable;
	private int type;
	
	public LightState(){
		enable = false;
		type = 0;
	}
	
	public LightState(JSONObject json){
		enable = json.getBoolean("enable");
		type = json.getInt("type");
	}
	
	public void change(String command){
		switch(command){
			case "type0":
				setType(0);
				break;
			case "type1":
				setType(1);
				break;
			case "type2":
				setType(2);
				break;
		}
	}
	
	
	public String[] states(){
		String[] s = new String[varSize];
		s[0] = String.valueOf(getEnable());
		s[1] = String.valueOf(getType());
		return s;
	}
	
	public boolean getEnable(){
		return enable;
	}
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	
	public int getType(){
		return type;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
}