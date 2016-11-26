import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;

public class ArduinoSocketServer extends SocketServer {

	private static int TV_EspPort = 8088;

	EspSocketServer TV;
	BufferedReader br;

	public ArduinoSocketServer(int port, String role) {
		super(port, role);
		TV = new EspSocketServer(TV_EspPort, "ESP_TV", this.getHistoryPanel());
		TV.setDataManager(dataManager);
	}

	@Override
	public void run() {
		updateLog("���A���w�Ұ� !");
		while (!connecting) {
			try {
				updateLog("���ݳs�u port : " + server.getLocalPort());
				updateLog("���o�s�u : InetAddress = " + startConnect(), true);
				TV.start();
				(new Sender()).start();
				(new Receiver()).start();
				watchConnecting();

			} catch (InterruptedException e) {
				updateLog("�ʵ��s�u�ɡA���w�����_");
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "Socket�s�u�����D !\n" + e.toString() + "\n");
				System.exit(0);
			}
		}
	}

	@Override
	InetAddress startConnect() throws java.io.IOException {
		Socket socket = null;
		synchronized (server) {
			socket = server.accept();
			socket.setSoTimeout(15000);
			out = new java.io.DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// Cause
																					// Arduino
																					// need
																					// to
																					// use
																					// buffer
			connecting = true;
		}
		return socket.getInetAddress();
	}

	class Receiver extends SocketServer.Receiver {
		@Override
		public void run() {
			while (connecting == true) {
				try {
					receive();
				} catch (java.net.SocketTimeoutException e) {
					// do nothing keep going
				} catch (IOException e) {
					connecting = false;
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
				}
			}
		}

		@Override
		protected void receive() throws IOException {
			String data = "";
			data = br.readLine();
			updateLog("�q" + role + "���o����:" + data);

			if (isCommand(data)) {
				handleCommand(data);
			} else if (!data.isEmpty()) {
				seter.setData(data);
			}
		}
	}

	class Sender extends SocketServer.Sender {
		@Override
		public void run() {
			while (connecting == true) {
				try {
					send();
					Thread.sleep(100); // if loop speed is too fast the message
										// can not send correctly
				} catch (java.net.SocketTimeoutException e) {
					continue;
				} catch (InterruptedException e) {
					updateLog("Sleep���w�������_");
				} catch (IOException e) {
					connecting = false;
					updateLog("���h�s�u port : " + server.getLocalPort() + " ���ݭ��s�s�u...", connecting);
					e.printStackTrace();
				}
			}
		}

		private void send() throws IOException {
			String data = geter.getData();
			if (data != null) {
				out.writeUTF(data);
				updateLog("Server�e�X����:" + data);
				data = null;
				geter.clear();
			}
		}
	}
	
	@Override
	protected String[] handleCommand(String command) {
		String[] s = null;
		switch (command) {
		case "TVOn":
			dataManager.changeState("TV", command);
			break;
		case "TVOff":
			dataManager.changeState("TV", command);
			break;
		default:
		}
		return s;
	}

	@Override
	protected boolean isCommand(String command) {
		switch (command) {
		/*request from arduino */
			case "TVOn":
			case "TVOff":
				return true;
			default:
		}
		return false;
	}

}