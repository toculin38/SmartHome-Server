
public class AndroidSocketServer extends SocketServer{

	public AndroidSocketServer(int port, String role) {
		super(port, role);
	}
	
	@Override
	protected String[] handleCommand(String command) {
		String[] s = null;
		switch (command) {
			case "get TV state":
				s = dataManager.getStates("TV");
				break;
			case "get Fan state":
				s = dataManager.getStates("Fan");
				break;
			case "get Air state":
				s = dataManager.getStates("Air");
				break;
			case "get Light state":
				s = dataManager.getStates("Light");
				break;
			default:
		}
		return s;
	}
	
	@Override
	protected boolean isCommand(String command) {
		switch (command) {
				/* request from android */
			case "get TV state":
			case "get Fan state":
			case "get Air state":
			case "get Light state":
				return true;
			default:
		}
		return false;
	}
	
	
}
