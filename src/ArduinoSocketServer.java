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
					out = new java.io.DataOutputStream(socket.getOutputStream());
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					connecting = true;
				}								//�n��BUFFER�g
				
				new Sender().start();
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
					//if(in.available()>0)
					if(!data.isEmpty())
					{
						updateLog("Server���o����:" + data);
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
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
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
						updateLog("Server�e�X����:" + data2);
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