public class Main{
	private static int ArduinoPort = 8890;
	private static int AndroidPort = 8888;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Communicator communicator = new Communicator();
		
		SocketServer sender = new SocketServer(ArduinoPort,"Sender");
		SocketServer receiver = new SocketServer(AndroidPort,"Receiver");
		MainUI gui = new MainUI(sender.getHistoryPanel(),receiver.getHistoryPanel());
		gui.setVisible(true);
		
		sender.setCommunicator(communicator);
		receiver.setCommunicator(communicator);
		sender.start();
		receiver.start();
	}
}
