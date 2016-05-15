import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class DataManager {

	private static final String FILE_NAME_TV = "TV.json";

	private TVState TV;

	public DataManager() {
		JSONObject json = readJsonFile(FILE_NAME_TV);
		TV = new TVState(json);
		
	}

	public void changeState(String appliance, String command) {
		switch (appliance) {
			case "TV":
				TV.change(command);
				writeJsonFile(new JSONObject(TV), FILE_NAME_TV);
				break;
			default:
		}
	}

	public String[] getStates(String type) {
		switch (type) {
		case "TV":
			return TV.states();
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
			JOptionPane.showMessageDialog(null, "系統找不到指定的檔案 !\n" + e.toString() + "\n");
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
