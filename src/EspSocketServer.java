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
					"Socket連線有問題 ! 指定的Port可能已經被使用中!" + "\n" + "IOException :" + e.toString() + "\n");
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
					updateLog("等待ESP連線 port : " + server.getLocalPort());
					socket = server.accept();
					updateLog("取得連線 : InetAddress = " + socket.getInetAddress(), true);
					socket.setSoTimeout(15000);					
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					connecting = true;
				}								//要用BUFFER寫
				
				new Receiver().start();
				while(connecting){
					Thread.sleep(100);
				}
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket連線有問題 !" + "\n" + "IOException :" + e.toString() + "\n");
				System.exit(0);
			} catch (InterruptedException e) {
				updateLog("Sleep未預期的中斷");
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
						updateLog("從ESP取得的值:" + data);
					}
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (IOException e) {
					connecting = false;
					updateLog("失去連線 port : " + server.getLocalPort() + " 等待重新連線...", connecting);
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