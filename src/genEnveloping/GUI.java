package genEnveloping;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {

	JFrame jFrame = new JFrame("Enveloping");

	JButton openButton;
	JButton exitButton;
	JButton extractButton;
	Container container = jFrame.getContentPane();

	JPanel buttonPanel;

	public GUI() {
		// super("XML Digital Signautre <Enveloping>");
		openButton = new JButton("OPEN"); // 버튼 객체 생성
		extractButton = new JButton("EXTRACT");
		exitButton = new JButton("EXIT");
		buttonPanel = new JPanel(); // 판넬 객체 생성

		buttonPanel.add(openButton);
		buttonPanel.add(exitButton);
		buttonPanel.add(extractButton);

		container.add(openButton, BorderLayout.NORTH);
		container.add(extractButton, BorderLayout.CENTER);
		container.add(exitButton, BorderLayout.SOUTH);

		jFrame.setSize(400, 200);
		jFrame.setVisible(true);

		openButton.addActionListener(this);
		exitButton.addActionListener(this);
		extractButton.addActionListener(this);

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:\\eclipse\\eclipse\\signature_work"));
		
		if (e.getSource() == openButton) {
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					GenEnveloping.main(chooser.getSelectedFile().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Signed Successfully");
			}
		} else if (e.getSource() == extractButton) {
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					ExtractEnveloping.main(chooser.getSelectedFile().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Extract Successfully");
			}
		} else if (e.getSource() == exitButton) {
			JOptionPane.showMessageDialog(null, "종료합니다.");
			System.exit(0);
		} else {
			// JOptionPane.showMessageDialog(null, "기능의 밖을 선택하셨습니다.",
			// JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void main(String[] args) {
		new GUI();
	}
}
