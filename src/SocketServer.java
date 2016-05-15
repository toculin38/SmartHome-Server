import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class SocketServer extends java.lang.Thread {

	DataManager dataManager;
	DataInputStream in;
	DataOutputStream out;

	Communicator geter;
	Communicator seter;
	ServerSocket server;
	HistoryPanel historylog;
	boolean connecting = false;
	String role;

	public SocketServer(int port, String role) {
		this.role = role;
		historylog = new HistoryPanel(role);
		try {
			server = new ServerSocket(port);

		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null,
					role + "Socket連線有問題 ! 指定的Port可能已經被使用中!" + "\n" + "IOException :" + e.toString() + "\n");
			System.exit(0);
		}
	}

	public void run() {
		updateLog("伺服器已啟動 !");
		while (!connecting) {
			try {
				updateLog("等待連線 port : " + server.getLocalPort());
				updateLog("取得連線 : InetAddress = " + startConnect(), true);
				(new Sender()).start();
				(new Receiver()).start();
				watchConnecting();
			} catch (InterruptedException e) {
				updateLog("監視連線時，未預期中斷");
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket連線有問題 !\n" + e.toString() + "\n");
				System.exit(0);
			}
		}
	}

	public void setCommunicator(Communicator geter, Communicator seter) {
		this.geter = geter;
		this.seter = seter;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public HistoryPanel getHistoryPanel() {
		return this.historylog;
	}

	InetAddress startConnect() throws java.io.IOException {
		Socket socket = null;
		synchronized (server) {
			socket = server.accept();
			socket.setSoTimeout(15000);
			in = new java.io.DataInputStream(socket.getInputStream());
			out = new java.io.DataOutputStream(socket.getOutputStream());
			connecting = true;
		}
		return socket.getInetAddress();
	}

	void watchConnecting() throws InterruptedException {
		while (connecting) {
			Thread.sleep(100);
		}
	}

	class Receiver extends java.lang.Thread {
		public void run() {
			while (connecting == true) {
				try {
					receive();
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (IOException e) {
					connecting = false;
					updateLog("失去連線 port : " + server.getLocalPort() + " 等待重新連線...", connecting);
				}
			}
		}

		protected void receive() throws IOException {
			String data = in.readUTF();
			updateLog("從" + role + "取得的值:" + data);
			if(isCommand(data)){
				String[] s = handleCommand(data);
				if (s != null) {
					for (int i = 0; i < s.length; i++) {
						out.writeUTF(s[i]);
					}
				} 
			}else {
				seter.setData(data);
			}
			
		}

	}

	class Sender extends java.lang.Thread {
		public void run() {
			while (connecting == true) {
				try {
					send();
					Thread.sleep(100); // if loop speed is too fast the message
										// can not send correctly
				} catch (java.net.SocketTimeoutException e) {
					continue;
				} catch (InterruptedException e) {
					updateLog("Sleep未預期的中斷");
				} catch (IOException e) {
					connecting = false;
					updateLog("失去連線 port : " + server.getLocalPort() + " 等待重新連線...", connecting);
					e.printStackTrace();
				}
			}
		}

		private void send() throws IOException {
			String data = geter.getData();
			if (data != null) {
				out.writeUTF(data);
				updateLog("Server送出的值:" + data);
				data = null;
				geter.clear();
			}
		}
	}

	void updateLog(String message) {
		historylog.addMessage(message);
	}

	void updateLog(String message, boolean connection) {
		historylog.addMessage(message);
		historylog.changeStateColor(connection);
	}

	protected String[] handleCommand(String command) {
		String[] s = null;
		switch (command) {
			case "get TV state":
				s = dataManager.getStates("TV");
				break;
			case "PowerOn":
				dataManager.changeState("TV", command);
				break;
			case "PowerOff":
				dataManager.changeState("TV", command);
				break;
			default:
		}
		return s;
	}
	
	protected boolean isCommand(String command) {
		switch (command) {
			case "get TV state":
				return true;
			case "PowerOn":
				return true;
			case "PowerOff":
				return true;
			default:
		}
		return false;
	}
	

}