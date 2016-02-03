import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class SocketServer extends java.lang.Thread {

	private DataInputStream in;
	private DataOutputStream out;
	private String data = null;
	private Communicator geter;
	private Communicator seter;
	private ServerSocket server;
	private HistoryPanel historylog;
	private boolean connecting = false;

	public SocketServer(int port, String role) {
		historylog = new HistoryPanel(role);
		try {
			server = new ServerSocket(port);

		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null,
					"Socket�s�u�����D ! ���w��Port�i��w�g�Q�ϥΤ�!" + "\n" + "IOException :" + e.toString() + "\n");
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
					updateLog("���o�s�u : InetAddress = " + socket.getInetAddress(), true);
					socket.setSoTimeout(15000);
					in = new java.io.DataInputStream(socket.getInputStream());
					out = new java.io.DataOutputStream(socket.getOutputStream());
					connecting = true;
				}
				new Receiver().start();
				new Sender().start();
				while(connecting){
					Thread.sleep(100);
				}
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket�s�u�����D !" + "\n" + "IOException :" + e.toString() + "\n");
				System.exit(0);
			} catch (InterruptedException e) {
				updateLog("Sleep���w�������_");
			}
		}
	}

	private class Receiver extends java.lang.Thread {
		public void run() {
			while (connecting == true) {
				try {
					data = in.readUTF();
					updateLog("Server���o����:" + data);
					seter.setData(data);
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (IOException e) {
					connecting = false;
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
				}
			}
		}
	}

	private class Sender extends java.lang.Thread {
		public void run() {
			while (connecting == true) {
				try {
					data = geter.getData();
					if (data != null) {
						out.writeUTF(data);
						out.flush();
						updateLog("Server�e�X����:" + data);
						data = null;
						geter.clear();
					}
					Thread.sleep(100); // if loop speed is too fast the message
										// can not send correctly
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (InterruptedException e) {
					updateLog("Sleep���w�������_");
				} catch (IOException e) {
					connecting = false;
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
					e.printStackTrace();
				}
			}
		}
	}

	public void setCommunicator(Communicator geter,Communicator seter) {
		this.geter = geter;
		this.seter = seter;
	}

	public HistoryPanel getHistoryPanel() {
		return this.historylog;
	}

	private void updateLog(String message) {
		historylog.addMessage(message);
	}

	private void updateLog(String message, boolean connection) {
		historylog.addMessage(message);
		historylog.changeStateColor(connection);
	}
}