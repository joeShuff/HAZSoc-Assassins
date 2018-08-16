package me.JoeShuff.Assassins;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import me.JoeShuff.Assassins.Objects.Mail;
import me.JoeShuff.Assassins.Objects.Player;
import javax.swing.JScrollPane;

public class SendEmail extends JFrame {

	private JPanel panel;
	private JTextField subjectText;
	private JTextArea bodyText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendEmail window = new SendEmail();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SendEmail() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddPlayer.class.getResource("/biohazard.png")));
		getContentPane().setBackground(Color.BLACK);
		setBounds(100, 100, 528, 342);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setUndecorated(true);
		setVisible(true);
		setResizable(false);
		
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(10, 32, 56, 25);
		panel.add(lblSubject);
		
		subjectText = new JTextField();
		subjectText.setBounds(65, 35, 447, 20);
		panel.add(subjectText);
		subjectText.setColumns(10);
		
		JLabel lblBody = new JLabel("Body");
		lblBody.setForeground(Color.WHITE);
		lblBody.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBody.setBounds(10, 64, 56, 25);
		panel.add(lblBody);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String subject = subjectText.getText();
				String body = bodyText.getText();
				
				for (Player p : MainWindow.me.PLAYERS)
				{
					Mail.sendMail(p.getEmail(), subject, body, null, null);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				setVisible(false);
				dispose();
			}
		});
		btnSend.setBounds(65, 282, 447, 23);
		panel.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(65, 68, 447, 207);
		panel.add(scrollPane);
		
		bodyText = new JTextArea();
		scrollPane.setViewportView(bodyText);
		bodyText.setLineWrap(true);
	}
}
