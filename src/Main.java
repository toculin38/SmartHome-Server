public class Main{
	private static int ArduinoPort = 8890;
	private static int AndroidPort = 8888;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Communicator communicatorA = new Communicator();
		Communicator communicatorB = new Communicator();
		SocketServer arduino = new SocketServer(ArduinoPort,"Arduino");
		SocketServer android = new SocketServer(AndroidPort,"Android");
		
		MainUI gui = new MainUI(arduino.getHistoryPanel(),android.getHistoryPanel());
		gui.setVisible(true);
		
		arduino.setCommunicator(communicatorA,communicatorB);
		android.setCommunicator(communicatorB,communicatorA);
		
		arduino.start();
		android.start();
	}
}
