import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class DataManager {

	private static final String FILE_NAME_TV = "TV.json";
	private static final String FILE_NAME_FAN = "Fan.json";
	private static final String FILE_NAME_AIR = "Air.json";
	private static final String FILE_NAME_LIGHT = "Light.json";
	
	private TVState TV;
	private AirState Air;
	private LightState Light;
	private FanState Fan;
	
	public DataManager() {
		TV = new TVState(readJsonFile(FILE_NAME_TV));
		Fan = new FanState(readJsonFile(FILE_NAME_FAN));
		Air = new AirState(readJsonFile(FILE_NAME_AIR));
		Light = new LightState(readJsonFile(FILE_NAME_LIGHT));

	}

	public void changeState(String appliance, String command) {
		switch (appliance) {
			case "TV":
				TV.change(command);
				writeJsonFile(new JSONObject(TV), FILE_NAME_TV);
				break;
			case "Air":
				Air.change(command);
				writeJsonFile(new JSONObject(Air), FILE_NAME_AIR);
				break;
			case "Light":
				Light.change(command);
				writeJsonFile(new JSONObject(Light), FILE_NAME_AIR);
				break;
			case "Fan":
				Fan.change(command);
				writeJsonFile(new JSONObject(Light), FILE_NAME_FAN);
				break;
			default:
		}
	}

	public String[] getStates(String type) {
		switch (type) {
		case "TV":
			return TV.states();
		case "Air":
			return Air.states();
		case "Light":
			return Light.states();
		case "Fan":
			return Fan.states();
		default:
			return null;
		}

	}

	private JSONObject readJsonFile(String fileName) {

		StringBuffer txt = new StringBuffer();

		FileReader fr;
		
		try {
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				txt.append(line + "\r\n");
			}
			fr.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "系統找不到" + fileName + "檔案 !\n" + e.toString() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new JSONObject(txt.toString());
	}

	private void writeJsonFile(JSONObject json, String fileName) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			fw.write(json.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
