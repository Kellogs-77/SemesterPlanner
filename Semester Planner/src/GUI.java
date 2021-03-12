import java.awt.BorderLayout;
import AssignmentTimeline.java;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI implements ActionListener{
	private int count = 0;
	private JLabel label;
	private JFrame frame;
	private JPanel panel;
	private JTextField userText;
	private ArrayList<String> finalList;
	private Scanner sc;
	private static JLabel success;
	
	public GUI() {
		
		frame = new JFrame();
		panel = new JPanel();
		frame.setSize(100,100);
		frame.add(panel);
		
		JLabel userLabel = new JLabel("Enter file name: ");
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);
		
		userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);
		frame.setVisible(true);
		
		success = new JLabel("");
		success.setBounds(100, 50, 165, 165);
		panel.add(success);
		
		JButton button = new JButton("Get schedule!");
		button.addActionListener(this);
		
		
		
		
		
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0, 1));
		panel.add(button);
		
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("OUR GUI");
		frame.pack();
		frame.setVisible(true);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GUI();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		LocalDate today = LocalDate.now();
		
		String currDate = today.toString();
		
		String temp = currDate;
		
		currDate = temp.substring(5, 7)+"/"+temp.substring(8)+"/"+temp.substring(0, 4);
		
		String fileName = userText.getText();
		
		try {
			 sc = new Scanner(new File(fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AssignmentTimeline at = new AssignmentTimeline();
		finalList = at.minHeapToUser(sc);
		String finalSchedule = "";
		for(String s : finalList) {
			finalSchedule = finalSchedule + s+ "\n";
		}
		
		success.setText(finalSchedule);
		
		
	}

}
