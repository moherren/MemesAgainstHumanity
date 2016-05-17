package networking;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChoosePanel extends JPanel implements ActionListener{

	SetUp frame;
	JButton startButton;
	JComboBox<String> selector;
	
	public ChoosePanel(SetUp frame){
		this.frame=frame;
		setBackground(Color.WHITE);
//		setPreferredSize(new Dimension(500,500));
		selector=new JComboBox<String>(new String[]{"Host Game","Join Game"});
		startButton=new JButton("Go");
		startButton.addActionListener(this);
		add(selector);
		add(startButton);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(selector.getSelectedItem().equals("Host Game")){
			frame.changePanel(new HostPanel(frame));
		}
		else if(selector.getSelectedItem().equals("Join Game")){
			frame.changePanel(new JoinPanel(frame));
		}
	}
}

