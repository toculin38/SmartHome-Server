
public class TVState{
	private boolean power;
	private int channel;
	public TVState(){
		power = false;
		channel = 12;
	}
	
	public void turnPower(){
		power = !power;
	}
	
	public String[] getStates(){
		String[] s = new String[2];
		s[0] = "" + power;
		s[1] = "" + channel;
		return s;
	}
}
