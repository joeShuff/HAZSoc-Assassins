package me.JoeShuff.Assassins;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import me.JoeShuff.Assassins.Objects.Mail;
import me.JoeShuff.Assassins.SQL.MySQL;
import me.JoeShuff.Assassins.SQL.SQLManager;

public class AddPlayer extends JFrame {

	private JPanel contentPane;
	
	private JTextField nameTxt;
	private JTextField emailTxt;
	private JTextField collegeTxt;
	private JTextField courseTxt;
	private JTextField yearTxt;
	private JTextField accommTxt;
	private JTextField socTxt;
	private JLabel profilePic;
	
	private String currentProfilePath = "";

	private String currentFaction = "";
	private JButton addProfileImgBtn;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args, String name, String path) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddPlayer window = new AddPlayer(name, path);
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
	public AddPlayer(String team, String path) {
		this.currentFaction = team;
		initialize(team, path);
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String team, String path) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddPlayer.class.getResource(path)));
		getContentPane().setBackground(Color.BLACK);
		setBounds(100, 100, 528, 335);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setUndecorated(true);
		setVisible(true);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(20, 20, 50, 49);
		Image dragonImg = new ImageIcon(this.getClass().getResource(path)).getImage();
		label.setIcon(new ImageIcon(dragonImg));
		getContentPane().add(label);
		
		JButton btnAddPlayer = new JButton("ADD PLAYER");
		btnAddPlayer.setBackground(Color.DARK_GRAY);
		btnAddPlayer.setForeground(Color.WHITE);
		btnAddPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String name = nameTxt.getText();
				String email = emailTxt.getText();
				String college = collegeTxt.getText();
				String course = courseTxt.getText();
				String year = yearTxt.getText();
				
				String accom = accommTxt.getText();
				String societies = socTxt.getText();
				
				nameTxt.setBackground(Color.BLACK);
				emailTxt.setBackground(Color.BLACK);
				collegeTxt.setBackground(Color.BLACK);
				courseTxt.setBackground(Color.BLACK);
				yearTxt.setBackground(Color.BLACK);
				
				accommTxt.setBackground(Color.BLACK);
				socTxt.setBackground(Color.BLACK);
				addProfileImgBtn.setBackground(Color.DARK_GRAY);
				
				boolean valid = true;
				
				if (name.equals("") || name.length() > 60)
				{
					valid = false;
					nameTxt.setBackground(Color.RED);
				}
				
				if (email.equals("") || !email.contains("@") || email.length() > 100 || !SQLManager.isValidEmail(email, false))
				{
					valid = false;
					emailTxt.setBackground(Color.RED);
				}
				
				if (college.equals("") || college.length() > 50)
				{
					valid = false;
					collegeTxt.setBackground(Color.RED);
				}
				
				if (course.equals("") || course.length() > 50)
				{
					valid = false;
					courseTxt.setBackground(Color.RED);
				}
				
				if (year.equals("") || year.length() > 100)
				{
					valid = false;
					yearTxt.setBackground(Color.RED);
				}
				
				if (accom.equals("") || accom.length() > 100)
				{
					valid = false;
					accommTxt.setBackground(Color.RED);
				}
				
				if (societies.equals("") || societies.length() > 1000)
				{
					valid = false;
					socTxt.setBackground(Color.RED);
				}
				
				if (profilePic.getIcon() == null)
				{
					valid = false;
					addProfileImgBtn.setBackground(Color.RED);
				}
				
				if (!valid)
				{
					JOptionPane.showMessageDialog(null, "Invalid player information!");
				}
				else
				{
					File destination = new File(currentProfilePath); 
					File to = new File(System.getProperty("user.dir") + "/saved.png");
					
					try {
						Files.copy(destination.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int id = -1;
					
					//Upload player data
					try
					{
						Connection conn = MySQL.getConnection();
						Statement s = conn.createStatement();
						
						String addPlayer = "INSERT INTO `HAZ_Players`(`name`, `faction`, `email`, `college`, `course`, `year`, `accommodation`, `other`) VALUES ('NAME','" + team + "', 'EMAIL','COLLEGE','COURSE','YEAR','ACC','SOC')";
						
						addPlayer = addPlayer.replace("NAME", name).replace("EMAIL", email).replace("COLLEGE", college).replace("COURSE", course).replace("YEAR", year).replace("ACC", accom).replace("SOC", societies);
						
						s.executeUpdate(addPlayer);
						
						ResultSet rs = s.executeQuery("SELECT `id` FROM `HAZ_Players` WHERE `email`='" + email + "'");
						
						if (rs.next())
						{						
							id = rs.getInt("id");
						}
						
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, "Error:\n" + e.getMessage());
						return;
					}
					
					MainWindow.ftpManager.client.uploadBytes(destination.toPath().toString(), "/Assassins/" + id + ".png");
					
					String addedPlayer = "Greetings Assassin, \n \n" +
											"You are receiving this email because you have been added to HAZSoc Assassins!\n" + 
											"You have been added to faction " + team + "\n \n" +
											"This is the information we have for you:\n" +
											"Name: " + name + "\n" +
											"Email: " + email + "\n" +
											"College: " + college + "\n" +
											"Course: " + course + "\n" + 
											"Year of Study: " + year + "\n" + 
											"Accommodation: " + accom + "\n" +
											"Other Info: " + societies + "\n" + 
											"Your profile image is also attached for approval!" + "\n \n" +
											"If any of this information is incorrect, OR if you are receiving this email in error, please let us know!\n \n" + 
											"Regards\n" + "The Machine";
					
					Mail.sendMail(email, "Welcome to Assassins!", addedPlayer, Arrays.asList(destination.toPath().toString()), Arrays.asList(name));
					
					MainWindow.notifScroller.setString("ADDED PLAYER");

					SQLManager.refreshAll();
					
					setVisible(false);
					dispose();
				}
			}
		});
		btnAddPlayer.setBounds(0, 282, 528, 23);
		contentPane.add(btnAddPlayer);
		
		JLabel label_1 = new JLabel("Name:");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(20, 89, 46, 14);
		contentPane.add(label_1);
		
		nameTxt = new JTextField();
		nameTxt.setForeground(Color.WHITE);
		nameTxt.setBackground(Color.BLACK);
		nameTxt.setColumns(10);
		nameTxt.setBounds(57, 86, 231, 20);
		contentPane.add(nameTxt);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setBounds(20, 117, 46, 14);
		contentPane.add(lblEmail);
		
		emailTxt = new JTextField();
		emailTxt.setForeground(Color.WHITE);
		emailTxt.setBackground(Color.BLACK);
		emailTxt.setColumns(10);
		emailTxt.setBounds(57, 114, 231, 20);
		contentPane.add(emailTxt);
		
		JLabel lblCollege = new JLabel("College:");
		lblCollege.setForeground(Color.WHITE);
		lblCollege.setBounds(12, 145, 46, 14);
		contentPane.add(lblCollege);
		
		collegeTxt = new JTextField();
		collegeTxt.setBackground(Color.BLACK);
		collegeTxt.setForeground(Color.WHITE);
		collegeTxt.setColumns(10);
		collegeTxt.setBounds(57, 142, 231, 20);
		contentPane.add(collegeTxt);
		
		JLabel lblCourse = new JLabel("Course:");
		lblCourse.setForeground(Color.WHITE);
		lblCourse.setBounds(15, 172, 46, 14);
		contentPane.add(lblCourse);
		
		courseTxt = new JTextField();
		courseTxt.setForeground(Color.WHITE);
		courseTxt.setBackground(Color.BLACK);
		courseTxt.setColumns(10);
		courseTxt.setBounds(57, 169, 231, 20);
		contentPane.add(courseTxt);
		
		JLabel lblYearOfStudy = new JLabel("Year of Study:");
		lblYearOfStudy.setForeground(Color.WHITE);
		lblYearOfStudy.setBounds(10, 201, 82, 14);
		contentPane.add(lblYearOfStudy);
		
		yearTxt = new JTextField();
		yearTxt.setBackground(Color.BLACK);
		yearTxt.setForeground(Color.WHITE);
		yearTxt.setColumns(10);
		yearTxt.setBounds(86, 198, 202, 20);
		contentPane.add(yearTxt);
		
		JLabel lblAccommodation = new JLabel("Accommodation:");
		lblAccommodation.setForeground(Color.WHITE);
		lblAccommodation.setBounds(20, 229, 94, 14);
		contentPane.add(lblAccommodation);
		
		accommTxt = new JTextField();
		accommTxt.setForeground(Color.WHITE);
		accommTxt.setBackground(Color.BLACK);
		accommTxt.setColumns(10);
		accommTxt.setBounds(108, 226, 180, 20);
		contentPane.add(accommTxt);
		
		JLabel lblSocieties = new JLabel("Other Info:");
		lblSocieties.setForeground(Color.WHITE);
		lblSocieties.setBounds(10, 257, 72, 14);
		contentPane.add(lblSocieties);
		
		socTxt = new JTextField();
		socTxt.setBackground(Color.BLACK);
		socTxt.setForeground(Color.WHITE);
		socTxt.setColumns(10);
		socTxt.setBounds(69, 254, 219, 20);
		contentPane.add(socTxt);
		
		addProfileImgBtn = new JButton("Add Profile Image");
		addProfileImgBtn.setForeground(Color.WHITE);
		addProfileImgBtn.setBackground(Color.DARK_GRAY);
		addProfileImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new ImageFilter());
				
				int returnval = chooser.showDialog(null, "Choose Player Profile Image");
				
				if (returnval == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();

					currentProfilePath = file.getPath();
					
					BufferedImage image = null;
				    try 
				    {
				        image = ImageIO.read(file);

				    } catch (Exception ex) 
				    {
				        ex.printStackTrace();
				    }
					
					ImageIcon imageIcon = new ImageIcon(fitimage(image, profilePic.getWidth(), profilePic.getHeight()));
				    profilePic.setIcon(imageIcon);
				}
			}
		});
		addProfileImgBtn.setBounds(316, 253, 202, 23);
		contentPane.add(addProfileImgBtn);
		
		profilePic = new JLabel("");
		profilePic.setForeground(Color.WHITE);
		profilePic.setBounds(316, 11, 202, 232);
		contentPane.add(profilePic);
	}

	public static Image fitimage(Image img , int w , int h)
	{
	    BufferedImage resizedimage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = resizedimage.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(img, 0, 0,w,h,null);
	    g2.dispose();
	    return resizedimage;
	} 
	
}
