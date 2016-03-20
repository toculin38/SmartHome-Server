
public class TVState{
	private boolean enable;
	private boolean power;
	private int channel;
	public TVState(){
		enable = true;
		power = false;
		channel = 12;
	}
	
	public void turnPower(){
		power = !power;
	}
	
	public String[] getStates(){
		String[] s = new String[3];
		s[0] = "" + enable;
		s[1] = "" + power;
		s[2] = "" + channel;
		return s;
	}
}
