import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainUI extends JFrame{
	
	private static int WIDTH = 640;
	private static int HEIGHT = 480;
	
	private JPanel ServerStatusPanel = new JPanel(); //伺服器狀態Panel
	private JPanel MessagePanel = new JPanel(); //訊息面板(整體下半)
	private JPanel BigPanel = new JPanel(); //上半部Panel
	
	public MainUI(HistoryPanel LPanel,HistoryPanel RPanel){
		super("Server");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		//基本UI設定
		BigPanel.setBackground(Color.WHITE); //上半部Panel背景色為白
		this.add(BigPanel); //加入上半部Panel
		
		BigPanel.add(ServerStatusPanel, BorderLayout.NORTH); //將伺服器狀態Panel加入上半部
		ServerStatusPanel.setLayout(new BorderLayout()); //伺服器狀態Panel設置BorderLayout
	
		ServerStatusPanel.add(LPanel.getStatePanel(), BorderLayout.NORTH); //獎Sender面板放入伺服器狀態Panel北邊
		ServerStatusPanel.add(RPanel.getStatePanel(), BorderLayout.SOUTH); //獎Receiver面板放入伺服器狀態Panel南邊
		
		MessagePanel.setBackground(Color.WHITE); //訊息面板設為白色
		MessagePanel.setLayout(new BorderLayout()); //訊息面板設置BorderLayout()
		MessagePanel.add(LPanel, BorderLayout.WEST); //將左邊面板(sender)放到訊息面板
		MessagePanel.add(RPanel, BorderLayout.EAST); //將右邊面板(receiver)放到訊息面板
		this.add(MessagePanel, BorderLayout.SOUTH); //把訊息面板放到下半部
	}

}
