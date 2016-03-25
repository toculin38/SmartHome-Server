import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;

public class EspSocketServer extends SocketServer {
	
	BufferedReader br;
	
	public EspSocketServer(int port, String role,HistoryPanel hp) {
		super(port,role);
		this.historylog = hp;
	}

	public void run() {
		
		updateLog("���A���w�Ұ� !");
		while (!connecting) {
			try{
				updateLog("����ESP�s�u port : " + server.getLocalPort());
				updateLog("���o�s�u : InetAddress = " + startConnect(), true);
				new Receiver().start();
				watchConnecting();
			}catch (InterruptedException e) {
				updateLog("�ʵ��s�u�ɡA���w�����_");
			}catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket�s�u�����D !\n" + e.toString() + "\n");
				System.exit(0);
			}
		}
	}
	
	@Override
	InetAddress startConnect() throws java.io.IOException{
		Socket socket = null;
		synchronized (server) {
			socket = server.accept();
			socket.setSoTimeout(15000);					
			out = new java.io.DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Cause Arduino need to use buffer
			connecting = true;
		}								
		return socket.getInetAddress();
	}
	
	
	class Receiver extends SocketServer.Receiver{
		@Override
		public void run(){
			String data="";
			while (connecting == true) {
				try {
					data = br.readLine();
					if(!data.isEmpty())
					{
						updateLog("�q" + role + "���o����:" + data);
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
	

}