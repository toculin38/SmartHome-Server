public class Main{

	private static int ArduinoPort = 9999;
	private static int AndroidPort = 8880;

	public static void main(String[] args) {
		
		Communicator communicatorA = new Communicator();
		Communicator communicatorB = new Communicator();
		
		DataManager dataManager = new DataManager();
		
		
		ArduinoSocketServer arduino = new ArduinoSocketServer(ArduinoPort,"Arduino");
		AndroidSocketServer android = new AndroidSocketServer(AndroidPort,"Android");
		
		
		MainUI gui = new MainUI(arduino.getHistoryPanel(),android.getHistoryPanel());
		gui.setVisible(true);
		
		arduino.setCommunicator(communicatorA,communicatorB);
		android.setCommunicator(communicatorB,communicatorA);
		
		arduino.setDataManager(dataManager);
		android.setDataManager(dataManager);
		
		android.start();
		arduino.start();
		
	}
}
