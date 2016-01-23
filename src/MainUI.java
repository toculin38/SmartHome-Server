import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainUI extends JFrame{
	
	private static int WIDTH = 640;
	private static int HEIGHT = 480;
	
	private JPanel ServerStatusPanel = new JPanel(); //���A�����APanel
	private JPanel MessagePanel = new JPanel(); //�T�����O(����U�b)
	private JPanel BigPanel = new JPanel(); //�W�b��Panel
	
	public MainUI(HistoryPanel LPanel,HistoryPanel RPanel){
		super("Server");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		//��UI�]�w
		BigPanel.setBackground(Color.WHITE); //�W�b��Panel�I���⬰��
		this.add(BigPanel); //�[�J�W�b��Panel
		
		BigPanel.add(ServerStatusPanel, BorderLayout.NORTH); //�N���A�����APanel�[�J�W�b��
		ServerStatusPanel.setLayout(new BorderLayout()); //���A�����APanel�]�mBorderLayout
	
		ServerStatusPanel.add(LPanel.getStatePanel(), BorderLayout.NORTH); //��Sender���O��J���A�����APanel�_��
		ServerStatusPanel.add(RPanel.getStatePanel(), BorderLayout.SOUTH); //��Receiver���O��J���A�����APanel�n��
		
		MessagePanel.setBackground(Color.WHITE); //�T�����O�]���զ�
		MessagePanel.setLayout(new BorderLayout()); //�T�����O�]�mBorderLayout()
		MessagePanel.add(LPanel, BorderLayout.WEST); //�N���䭱�O(sender)���T�����O
		MessagePanel.add(RPanel, BorderLayout.EAST); //�N�k�䭱�O(receiver)���T�����O
		this.add(MessagePanel, BorderLayout.SOUTH); //��T�����O���U�b��
	}

}
