import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class ArduinoSocketServer extends java.lang.Thread {

	private DataManager dataManager;
	private DataOutputStream out;
	private BufferedReader br ;
	private Communicator geter;
	private Communicator seter;
	private ServerSocket server;
	private HistoryPanel historylog;
	private boolean connecting = false;
	private String role;
	
	public ArduinoSocketServer(int port, String role) {
		this.role = role;
		historylog = new HistoryPanel(role);
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
					updateLog("等待連線 port : " + server.getLocalPort());
					socket = server.accept();
					updateLog("取得連線 : InetAddress = " + socket.getInetAddress(), true);
					socket.setSoTimeout(15000);					
					out = new java.io.DataOutputStream(socket.getOutputStream());
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					connecting = true;
				}								//要用BUFFER寫
				
				new Sender().start();
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
					//if(in.available()>0)
					if(!data.isEmpty())
					{
						updateLog("Server取得的值:" + data);
						String[] s = analysisCommand(data);
						if(s != null){
							for(int i = 0 ; i < s.length; i++)
							{
								out.writeUTF(s[i]);
							}
					}
					else{
						seter.setData(data);
					}
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

	private class Sender extends java.lang.Thread 
	{
		public void run() 
		{
			String data2;
			while (connecting == true) {
				try {
					data2 = geter.getData();
					if (data2 != null) {
						out.writeUTF(data2);
						updateLog("Server送出的值:" + data2);
						if(role.equals("Arduino")){
							dataManager.handle(data2);
						}
						data2 = null;
						geter.clear();
					}
					Thread.sleep(100); // if loop speed is too fast the message
									   // can not send correctly
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (InterruptedException e) {
					updateLog("Sleep未預期的中斷");
				} catch (IOException e) {
					connecting = false;
					updateLog("失去連線 port : " + server.getLocalPort() + " 等待重新連線...", connecting);
					e.printStackTrace();
				}
			}
		}
	}

	public void setCommunicator(Communicator geter,Communicator seter) {
		this.geter = geter;
		this.seter = seter;
	}

	public void setDataManager(DataManager dataManager){
		this.dataManager = dataManager;
	}
	
	public HistoryPanel getHistoryPanel() {
		return this.historylog;
	}

	private String[] analysisCommand(String command){
		String[] s = null;
		switch(command){
			case "get TV state":
				s = dataManager.getStates("TV");
				break;
			default :

		}
		return s;
	}
	
	private void updateLog(String message) {
		historylog.addMessage(message);
	}

	private void updateLog(String message, boolean connection) {
		historylog.addMessage(message);
		historylog.changeStateColor(connection);
	}
	
}