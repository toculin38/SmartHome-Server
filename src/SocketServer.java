import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.*;

public class SocketServer extends java.lang.Thread {
	
	private DataInputStream in;
	private DataOutputStream out;
	private String data = null;
	private String role;
	private Communicator communicator;
	private ServerSocket server;
	private HistoryPanel historylog;
	
	public SocketServer(int port, String role) {
		historylog = new HistoryPanel(role);
		try {
			this.role = role;
			server = new ServerSocket(port);

		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null, "Socket�s�u�����D ! ���w��Port�i��w�g�Q�ϥΤ�!" + "\n" + "IOException :" + e.toString() + "\n");
			System.exit(0);
		}
	}

	public void run() {
		Socket socket;
		updateLog("���A���w�Ұ� !");
		while (true) {
			socket = null;
			try {
				synchronized (server) {
					updateLog("���ݳs�u port : " + server.getLocalPort());
					socket = server.accept();
				}
				updateLog("���o�s�u : InetAddress = " + socket.getInetAddress(),true);
				socket.setSoTimeout(15000);
				in = new java.io.DataInputStream(socket.getInputStream());
				out = new java.io.DataOutputStream(socket.getOutputStream());
				switch(role){
					case "Sender":
						send();
						break;
					case "Receiver":
						receive();
						break;
				}
				
			} catch (java.net.SocketException e) {
				updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...",false);
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket�s�u�����D !" + "\n" + "IOException :" + e.toString() + "\n");
				System.exit(0);
			}

		}
	}
	
	private void receive() throws IOException{
		while (true)
		{
			try {
				data = in.readUTF();
				updateLog("Server���o����:" + data);
				communicator.setData(data);
			} catch (java.net.SocketTimeoutException e) {
				// do nothing keep going
			}
		}
	}
	private void send() throws IOException,SocketException{
		while (true)
		{
			try {
				data = communicator.getData();
				if(data != null){
					data = communicator.getData();
					out.writeUTF(data);
					out.flush();
					updateLog("Server�e�X����:" + data);				
					data = null;
					communicator.setData(data);
				}
				Thread.sleep(100); //if loop speed is too fast the message can not send correctly
			} catch (java.net.SocketTimeoutException e) {
				// do nothing keep going
			} catch (InterruptedException e) {
				updateLog("Sleep���w�������_");
			}
		}
	}

	public void setCommunicator(Communicator communicator){
		this.communicator = communicator;
	}
	
	public HistoryPanel getHistoryPanel(){
		return this.historylog;
	}
	
	private void updateLog(String message){
		historylog.addMessage(message);
	}
	
	private void updateLog(String message,boolean connection){
		historylog.addMessage(message);
		historylog.changeStateColor(connection);
	}
}