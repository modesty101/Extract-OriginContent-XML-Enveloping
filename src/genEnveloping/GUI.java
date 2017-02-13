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
	JButton verifyButton;
	JButton prepareVfyButton;
	Container container = jFrame.getContentPane();

	JPanel buttonPanel;

	public GUI() {
		openButton = new JButton("OPEN"); // 버튼 객체 생성
		extractButton = new JButton("EXTRACT");
		verifyButton = new JButton("VERIFY");
		prepareVfyButton = new JButton("PREPARE");
		exitButton = new JButton("EXIT");
		buttonPanel = new JPanel(); // 판넬 객체 생성

		buttonPanel.add(openButton);
		buttonPanel.add(exitButton);
		buttonPanel.add(extractButton);
		buttonPanel.add(verifyButton);
		buttonPanel.add(prepareVfyButton);

		container.add(openButton, BorderLayout.NORTH);
		container.add(extractButton, BorderLayout.CENTER);
		container.add(verifyButton, BorderLayout.WEST);
		container.add(exitButton, BorderLayout.SOUTH);
		container.add(prepareVfyButton, BorderLayout.EAST);

		jFrame.setSize(400, 200);
		jFrame.setVisible(true);

		openButton.addActionListener(this);
		exitButton.addActionListener(this);
		extractButton.addActionListener(this);
		verifyButton.addActionListener(this);
		prepareVfyButton.addActionListener(this);

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:\\eclipse\\eclipse\\signature_work"));
		chooser.setMultiSelectionEnabled(true);

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
		} else if (e.getSource() == verifyButton) {
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				boolean flag = false;
				File selected[] = chooser.getSelectedFiles();
				try {
					flag = VerifyEnveloping.main(selected[0].getName(),selected[1].getName());
					if (flag == true) {
						JOptionPane.showMessageDialog(null, "Verify OK");
					} else {
						JOptionPane.showMessageDialog(null, "Verify NOT OK!!");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		} else if (e.getSource() == prepareVfyButton) {
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				boolean flag = false;
				try {
					flag = AppendStr.main(chooser.getSelectedFile().getName());

					if (flag == true) {
						JOptionPane.showMessageDialog(null, "Prepared. Ready!!");
					} else {
						JOptionPane.showMessageDialog(null, "NOT PREPARED..");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else {
			// JOptionPane.showMessageDialog(null, "기능의 밖을 선택하셨습니다.",
			// JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void main(String[] args) {
		new GUI();
	}
}
