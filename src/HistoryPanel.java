import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel{
	
	private JTextArea area = new JTextArea(20,20);
	private JLabel nameLabel = new JLabel();
	
	private JPanel statePanel = new JPanel();
	private JPanel colorPanel = new JPanel();
	private JLabel stateLabel = new JLabel();
	
	private String history = "";
	public HistoryPanel(String panelName){
		super();
		//set up state Panel
		statePanel.setBackground(Color.WHITE);
		statePanel.setLayout(new GridLayout(1,2));
		
		colorPanel.setBackground(Color.RED);
		
		stateLabel.setText(panelName);
		
		statePanel.add(colorPanel);
		statePanel.add(stateLabel);
		
		//set up this panel
		this.setBackground(Color.WHITE);
		nameLabel.setText(panelName); 
		area.setEditable(false);
		this.add(new JScrollPane(area));
		this.add(nameLabel);
		
	}
	public void addMessage(String message){
		history += message + "\n";
		area.setText(history);
	}
	public void changeStateColor(boolean flag){
		if(flag)
			colorPanel.setBackground(Color.GREEN);
		else
			colorPanel.setBackground(Color.RED);
	}
	public JPanel getStatePanel() {
		return statePanel;
	}
}
