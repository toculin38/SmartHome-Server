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
			JOptionPane.showMessageDialog(null, "Socket連線有問題 ! 指定的Port可能已經被使用中!" + "\n" + "IOException :" + e.toString() + "\n");
			System.exit(0);
		}
	}

	public void run() {
		Socket socket;
		updateLog("伺服器已啟動 !");
		while (true) {
			socket = null;
			try {
				synchronized (server) {
					updateLog("等待連線 port : " + server.getLocalPort());
					socket = server.accept();
				}
				updateLog("取得連線 : InetAddress = " + socket.getInetAddress(),true);
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
				updateLog("失去連線 port : " + server.getLocalPort() + " 等待重新連線...",false);
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket連線有問題 !" + "\n" + "IOException :" + e.toString() + "\n");
				System.exit(0);
			}

		}
	}
	
	private void receive() throws IOException{
		while (true)
		{
			try {
				data = in.readUTF();
				updateLog("Server取得的值:" + data);
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
					updateLog("Server送出的值:" + data);				
					data = null;
					communicator.setData(data);
				}
				Thread.sleep(100); //if loop speed is too fast the message can not send correctly
			} catch (java.net.SocketTimeoutException e) {
				// do nothing keep going
			} catch (InterruptedException e) {
				updateLog("Sleep未預期的中斷");
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