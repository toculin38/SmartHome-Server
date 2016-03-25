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
		
		updateLog("伺服器已啟動 !");
		while (!connecting) {
			try{
				updateLog("等待ESP連線 port : " + server.getLocalPort());
				updateLog("取得連線 : InetAddress = " + startConnect(), true);
				new Receiver().start();
				watchConnecting();
			}catch (InterruptedException e) {
				updateLog("監視連線時，未預期中斷");
			}catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket連線有問題 !\n" + e.toString() + "\n");
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
						updateLog("從" + role + "取得的值:" + data);
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
	

}