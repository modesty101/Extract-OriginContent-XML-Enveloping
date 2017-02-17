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

/**
 * GUI 구현
 * 
 * @author 김동규
 * @since 2017
 */
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
		extractButton = new JButton("EXTRACT"); // 값 추출
		verifyButton = new JButton("VERIFY"); // 다이제스트 값 검증
		prepareVfyButton = new JButton("PREPARE"); // 검증 준비
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
			/* 파일을 Enveloping 서명한다. */
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
			/* 서명된 파일에서 원본 파일을 추출한다 */
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					ExtractEnveloping.main(chooser.getSelectedFile().toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Extract Successfully");
			}
		} else if (e.getSource() == exitButton) {
			JOptionPane.showMessageDialog(null, "종료합니다.");
			System.exit(0);
		} else if (e.getSource() == verifyButton) {
			/* 다이제스트 값을 검증한다 */
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				boolean flag = false;
				try {
					flag = VerifyEnveloping.main(chooser.getSelectedFile().getName());
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
			/* 검증을 위해 특정 문자열을 특정위치에 삽입한다 */
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
