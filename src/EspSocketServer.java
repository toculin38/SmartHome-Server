import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class EspSocketServer extends java.lang.Thread {

	private DataManager dataManager;
	private BufferedReader br ;
	private ServerSocket server;
	private HistoryPanel historylog;
	private boolean connecting = false;
	private String role;
	public EspSocketServer(int port, String role,HistoryPanel hp) {
		this.role = role;
		this.historylog = hp;
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
					updateLog("����ESP�s�u port : " + server.getLocalPort());
					socket = server.accept();
					updateLog("���o�s�u : InetAddress = " + socket.getInetAddress(), true);
					socket.setSoTimeout(15000);					
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					connecting = true;
				}								//�n��BUFFER�g
				
				new Receiver().start();
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

	private class Receiver extends java.lang.Thread
	{
		public void run()
		{
			String data="";
			while (connecting == true) {
				try {
					data = br.readLine();
					if(!data.isEmpty())
					{
						updateLog("�qESP���o����:" + data);
					}
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (IOException e) {
					connecting = false;
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
				}
			}
		}
	}
	
	public void setDataManager(DataManager dataManager){
		this.dataManager = dataManager;
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