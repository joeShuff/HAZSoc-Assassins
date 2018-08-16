package me.JoeShuff.Assassins;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.JoeShuff.Assassins.FTP.FTPManager;
import me.JoeShuff.Assassins.Objects.Contract;
import me.JoeShuff.Assassins.Objects.Mail;
import me.JoeShuff.Assassins.Objects.Player;
import me.JoeShuff.Assassins.Objects.TextScroller;
import me.JoeShuff.Assassins.SQL.MySQL;
import me.JoeShuff.Assassins.SQL.SQLManager;

public class MainWindow extends JFrame {
	
	public HashMap<String, Integer> killTypes = new HashMap<String, Integer>();
	public List<String> KILL_TYPES = Arrays.asList("Small Melee","Killer Animal","Nerf Gun","Large Melee","Poisoning","Shadow Game","Costume Kill");
	
	public static JPanel contentPane;
	
	public static MainWindow me;
	
	public static FTPManager ftpManager;
	
	public static TextScroller notifScroller;
	
	public ProgressScroller progressScroller;
	
	public Player CURRENT_PLAYER;
	
	public String CURRENT_DOUBLE_POINTS = "Small Melee";

	private static final String FTP_HOST = "";
	private static final String FTP_USERNAME = "";
	private static final String FTP_PASSWORD = "";
	
	public String currentContract = "";
	
	public List<Player> PLAYERS = new ArrayList<Player>();
	public List<Contract> CONTRACTS = new ArrayList<Contract>();
	
	public String currentImagePath = "";
	
	public JTextField nameTxt;
	public JTextField emailTxt;
	public JTextField collegeTxt;
	public JTextField courseTxt;
	public JTextField yearTxt;
	public JTextField accommTxt;
	public JTextField SocietiesTxt;
	public JCheckBox chckbxAssignContracts;
	public JLabel profileImage;
	public JList contractList;
	public JList RocketList;
	public JList SpartaList;
	public JList DragonList;
	public JButton btnSave;
	public JButton btnChangeProfileImage;
	public JButton btnCompleteContract;
	public JLabel activeContractsCount;
	public JLabel targettedByCount;
	public JLabel completedContractsCount;
	public JLabel pointsCount;
	public JLabel doublePointsKillMethod;
	public JCheckBox chckbxUpdateMethod;
	public JCheckBox chckbxShowActiveContracts;
	public JProgressBar progressBar;
	public JLabel lblProgressBar;
	public JButton buttonRefresh;
	public JButton btnAddToRocket;
	public JButton btnAddToSparta;
	public JButton btnAddToDragon;
	public JButton btnForceNewContracts;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					me = window;
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setIconImage(new ImageIcon(this.getClass().getResource("/biohazard.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(100, 100, 1018, 696);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		killTypes.put("Nerf Gun", 2);
		killTypes.put("Large Melee", 2);
		killTypes.put("Small Melee", 1);
		killTypes.put("Killer Animal", 2);
		killTypes.put("Poisoning", 3);
		killTypes.put("Shadow Game - Assassin Win", 3);
		killTypes.put("Shadow Game - Target Win", 2);
		killTypes.put("Costume Kill", 3);
		
		JLabel titleLogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/logo.png")).getImage();
		titleLogo.setIcon(new ImageIcon(img));
		titleLogo.setBounds(10, 0, 231, 76);
		contentPane.add(titleLogo);
		
		JScrollPane RocketScrollPane = new JScrollPane();
		RocketScrollPane.setBounds(25, 285, 200, 249);
		contentPane.add(RocketScrollPane);
		
		RocketList = new JList();
		RocketList.setBackground(Color.DARK_GRAY);
		RocketList.setForeground(Color.WHITE);
		RocketList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				SQLManager.setActivePlayer((String)list.getSelectedValue());
				SpartaList.clearSelection();
				DragonList.clearSelection();
			}
		});
		RocketScrollPane.setViewportView(RocketList);
		
		JScrollPane SpartaScrollPane = new JScrollPane();
		SpartaScrollPane.setBounds(248, 285, 200, 249);
		contentPane.add(SpartaScrollPane);
		
		SpartaList = new JList();
		SpartaList.setBackground(Color.DARK_GRAY);
		SpartaList.setForeground(Color.WHITE);
		SpartaList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				SQLManager.setActivePlayer((String)list.getSelectedValue());
				RocketList.clearSelection();
				DragonList.clearSelection();
			}
		});
		SpartaScrollPane.setViewportView(SpartaList);
		
		JScrollPane DragonScrollPane = new JScrollPane();
		DragonScrollPane.setBounds(475, 285, 200, 249);
		contentPane.add(DragonScrollPane);
		
		DragonList = new JList();
		DragonList.setBackground(Color.DARK_GRAY);
		DragonList.setForeground(Color.WHITE);
		DragonList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				SQLManager.setActivePlayer((String)list.getSelectedValue());
				RocketList.clearSelection();
				SpartaList.clearSelection();
			}
		});
		DragonScrollPane.setViewportView(DragonList);
		
		JLabel rocketLogo = new JLabel("");
		rocketLogo.setBounds(25, 236, 50, 49);
		Image rocketImg = new ImageIcon(this.getClass().getResource("/rocket.png")).getImage();
		rocketLogo.setIcon(new ImageIcon(rocketImg));
		contentPane.add(rocketLogo);
		
		JLabel spartaLogo = new JLabel("");
		spartaLogo.setBounds(248, 236, 50, 49);
		Image spartaImg = new ImageIcon(this.getClass().getResource("/sparta.png")).getImage();
		spartaLogo.setIcon(new ImageIcon(spartaImg));
		contentPane.add(spartaLogo);
		
		JLabel dragonLogo = new JLabel("");
		dragonLogo.setBounds(473, 236, 50, 49);
		Image dragonImg = new ImageIcon(this.getClass().getResource("/dragon.png")).getImage();
		dragonLogo.setIcon(new ImageIcon(dragonImg));
		contentPane.add(dragonLogo);
		
		btnAddToRocket = new JButton("Add to Rocket");
		btnAddToRocket.setForeground(Color.WHITE);
		btnAddToRocket.setBackground(Color.DARK_GRAY);
		btnAddToRocket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						AddPlayer.main(null, "Rocket", "/rocket.png");	
						
					}
				}).start();
							
			}
		});
		btnAddToRocket.setBounds(25, 545, 200, 23);
		contentPane.add(btnAddToRocket);
		
		btnAddToSparta = new JButton("Add to Sparta");
		btnAddToSparta.setForeground(Color.WHITE);
		btnAddToSparta.setBackground(Color.DARK_GRAY);
		btnAddToSparta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						AddPlayer.main(null, "Sparta", "/sparta.png");	
						
					}
				}).start();
			}
		});
		btnAddToSparta.setBounds(248, 545, 200, 23);
		contentPane.add(btnAddToSparta);
		
		btnAddToDragon = new JButton("Add to Dragon Army");
		btnAddToDragon.setForeground(Color.WHITE);
		btnAddToDragon.setBackground(Color.DARK_GRAY);
		btnAddToDragon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						AddPlayer.main(null, "Dragon Army", "/dragon.png");	
						
					}
				}).start();
			}
		});
		btnAddToDragon.setBounds(475, 545, 200, 23);
		contentPane.add(btnAddToDragon);
		
		JLabel lblRocket = new JLabel("ROCKET");
		lblRocket.setForeground(new Color(30, 144, 255));
		lblRocket.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRocket.setBounds(87, 248, 95, 26);
		contentPane.add(lblRocket);
		
		JLabel lblSparta = new JLabel("SPARTA");
		lblSparta.setForeground(new Color(255, 0, 0));
		lblSparta.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSparta.setBounds(308, 247, 95, 26);
		contentPane.add(lblSparta);
		
		JLabel lblDragonArmy = new JLabel("DRAGON ARMY");
		lblDragonArmy.setForeground(new Color(0, 128, 0));
		lblDragonArmy.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDragonArmy.setBounds(533, 247, 142, 26);
		contentPane.add(lblDragonArmy);
		
		JLabel lblTime = new JLabel("TIME");
		lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTime.setFont(new Font("Prestige Elite Std", Font.PLAIN, 20));
		lblTime.setForeground(new Color(255, 255, 255));
		lblTime.setBounds(328, 36, 674, 26);
		contentPane.add(lblTime);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.WHITE);
		separator.setBounds(10, 223, 740, 1);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.WHITE);
		separator_1.setBounds(10, 76, 992, 1);
		contentPane.add(separator_1);
		
		JLabel lblCurrentUser = new JLabel("Selected Player");
		lblCurrentUser.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCurrentUser.setForeground(Color.WHITE);
		lblCurrentUser.setBounds(10, 76, 142, 23);
		contentPane.add(lblCurrentUser);
		
		nameTxt = new JTextField();
		nameTxt.setForeground(Color.WHITE);
		nameTxt.setBackground(Color.BLACK);
		nameTxt.setEnabled(false);
		nameTxt.setBounds(47, 113, 231, 20);
		contentPane.add(nameTxt);
		nameTxt.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setForeground(Color.WHITE);
		lblName.setBounds(10, 116, 46, 14);
		contentPane.add(lblName);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setBounds(10, 147, 46, 14);
		contentPane.add(lblEmail);
		
		emailTxt = new JTextField();
		emailTxt.setBackground(Color.BLACK);
		emailTxt.setForeground(Color.WHITE);
		emailTxt.setEnabled(false);
		emailTxt.setColumns(10);
		emailTxt.setBounds(47, 144, 231, 20);
		contentPane.add(emailTxt);
		
		JLabel lblCollege = new JLabel("College:");
		lblCollege.setForeground(Color.WHITE);
		lblCollege.setBounds(10, 178, 46, 14);
		contentPane.add(lblCollege);
		
		collegeTxt = new JTextField();
		collegeTxt.setForeground(Color.WHITE);
		collegeTxt.setBackground(Color.BLACK);
		collegeTxt.setEnabled(false);
		collegeTxt.setColumns(10);
		collegeTxt.setBounds(78, 175, 200, 20);
		contentPane.add(collegeTxt);
		
		JLabel lblCourse = new JLabel("Course:");
		lblCourse.setForeground(Color.WHITE);
		lblCourse.setBounds(339, 89, 46, 14);
		contentPane.add(lblCourse);
		
		courseTxt = new JTextField();
		courseTxt.setBackground(Color.BLACK);
		courseTxt.setForeground(Color.WHITE);
		courseTxt.setEnabled(false);
		courseTxt.setColumns(10);
		courseTxt.setBounds(385, 86, 200, 20);
		contentPane.add(courseTxt);
		
		JLabel lblYearOfStudy = new JLabel("Year of Study:");
		lblYearOfStudy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblYearOfStudy.setForeground(Color.WHITE);
		lblYearOfStudy.setBounds(293, 114, 87, 14);
		contentPane.add(lblYearOfStudy);
		
		yearTxt = new JTextField();
		yearTxt.setForeground(Color.WHITE);
		yearTxt.setBackground(Color.BLACK);
		yearTxt.setEnabled(false);
		yearTxt.setColumns(10);
		yearTxt.setBounds(385, 111, 200, 20);
		contentPane.add(yearTxt);
		
		JLabel lblAccommodation = new JLabel("Accommodation:");
		lblAccommodation.setForeground(Color.WHITE);
		lblAccommodation.setBounds(298, 139, 87, 14);
		contentPane.add(lblAccommodation);
		
		accommTxt = new JTextField();
		accommTxt.setBackground(Color.BLACK);
		accommTxt.setForeground(Color.WHITE);
		accommTxt.setEnabled(false);
		accommTxt.setColumns(10);
		accommTxt.setBounds(385, 136, 200, 20);
		contentPane.add(accommTxt);
		
		JLabel lblSocieties = new JLabel("Other Info:");
		lblSocieties.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSocieties.setForeground(Color.WHITE);
		lblSocieties.setBounds(308, 164, 68, 14);
		contentPane.add(lblSocieties);
		
		SocietiesTxt = new JTextField();
		SocietiesTxt.setForeground(Color.WHITE);
		SocietiesTxt.setBackground(Color.BLACK);
		SocietiesTxt.setEnabled(false);
		SocietiesTxt.setColumns(10);
		SocietiesTxt.setBounds(385, 161, 200, 20);
		contentPane.add(SocietiesTxt);
		
		btnSave = new JButton("Save");
		btnSave.setBackground(Color.DARK_GRAY);
		btnSave.setForeground(Color.WHITE);
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String name = nameTxt.getText();
				String email = emailTxt.getText();
				String college = collegeTxt.getText();
				String course = courseTxt.getText();
				String year = yearTxt.getText();
				
				String accom = accommTxt.getText();
				String societies = SocietiesTxt.getText();
				
				nameTxt.setBackground(Color.BLACK);
				emailTxt.setBackground(Color.BLACK);
				collegeTxt.setBackground(Color.BLACK);
				courseTxt.setBackground(Color.BLACK);
				yearTxt.setBackground(Color.BLACK);
				
				accommTxt.setBackground(Color.BLACK);
				SocietiesTxt.setBackground(Color.BLACK);
				
				boolean valid = true;
				
				String message = "";
				
				if (name.equals("") || name.length() > 60)
				{
					valid = false;
					nameTxt.setBackground(Color.RED);
				}
				
				if (email.equals("") || !email.contains("@") || email.length() > 100 || !SQLManager.isValidEmail(email, true))
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
					SocietiesTxt.setBackground(Color.RED);
				}
				
				if (profileImage.getIcon() == null)
				{
					valid = false;
					btnChangeProfileImage.setBackground(Color.RED);
				}
				
				if (!valid)
				{
					JOptionPane.showMessageDialog(null, "Invalid player information!");
				}
				else
				{
					File destination = new File(currentImagePath); 
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
						
						String addPlayer = "UPDATE `HAZ_Players` SET `name`='NAME', `faction`='" + CURRENT_PLAYER.getFaction() + "', `email`='EMAIL', `college`='COLLEGE', `course`='COURSE', `year`='YEAR', `accommodation`='ACC', `other`='SOC' WHERE `id`='" + CURRENT_PLAYER.getID() + "'";
						
						addPlayer = addPlayer.replace("NAME", name).replace("EMAIL", email).replace("COLLEGE", college).replace("COURSE", course).replace("YEAR", year).replace("ACC", accom).replace("SOC", societies);
						
						s.executeUpdate(addPlayer);
						
						ResultSet rs = s.executeQuery("SELECT `id` FROM `HAZ_Players` WHERE `email`='" + email + "'");
						
						if (rs.next())
						{						
							id = rs.getInt("id");
						}
						
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
						return;
					}
					
					MainWindow.ftpManager.client.uploadBytes(destination.toPath().toString(), "/Assassins/" + id + ".png");
					
					File toRemove = new File(System.getProperty("user.dir") + "/Images/" + id + ".png");
					
					if (toRemove.exists())
					{
						toRemove.delete();
					}
					
					MainWindow.notifScroller.setString("UPDATED PLAYER");

					SQLManager.refreshAll();
				}				
			}
		});
		btnSave.setBounds(385, 189, 200, 23);
		contentPane.add(btnSave);
		
		btnChangeProfileImage = new JButton("Change Image");
		btnChangeProfileImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new ImageFilter());
				
				int returnval = chooser.showDialog(null, "Choose Player Profile Image");
				
				if (returnval == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();

					currentImagePath = file.getPath();
					
					BufferedImage image = null;
				    try 
				    {
				        image = ImageIO.read(file);

				    } catch (Exception ex) 
				    {
				        ex.printStackTrace();
				    }
					
					ImageIcon imageIcon = new ImageIcon(AddPlayer.fitimage(image, profileImage.getWidth(), profileImage.getHeight()));
				    profileImage.setIcon(imageIcon);
				}
			}
		});
		btnChangeProfileImage.setForeground(Color.WHITE);
		btnChangeProfileImage.setBackground(Color.DARK_GRAY);
		btnChangeProfileImage.setEnabled(false);
		btnChangeProfileImage.setBounds(595, 189, 137, 23);
		contentPane.add(btnChangeProfileImage);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.WHITE);
		separator_2.setBounds(748, 77, 2, 590);
		contentPane.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.WHITE);
		separator_3.setBounds(748, 457, 264, 1);
		contentPane.add(separator_3);
		
		JLabel lblNotifications = new JLabel("");
		lblNotifications.setForeground(Color.WHITE);
		lblNotifications.setFont(new Font("Prestige Elite Std", Font.PLAIN, 19));
		lblNotifications.setBounds(228, 42, 486, 34);
		contentPane.add(lblNotifications);
		
		notifScroller = new TextScroller(lblNotifications);
		
		chckbxAssignContracts = new JCheckBox("Assign Contracts at Midnight");
		chckbxAssignContracts.setForeground(Color.WHITE);
		chckbxAssignContracts.setBackground(Color.BLACK);
		chckbxAssignContracts.setFont(new Font("Prestige Elite Std", Font.PLAIN, 13));
		chckbxAssignContracts.setBounds(228, 12, 447, 23);
		contentPane.add(chckbxAssignContracts);
		
		profileImage = new JLabel("");
		profileImage.setBounds(595, 88, 137, 90);
		contentPane.add(profileImage);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(760, 88, 242, 283);
		contentPane.add(scrollPane);
		
		contractList = new JList();
		contractList.setForeground(Color.WHITE);
		contractList.setBackground(Color.DARK_GRAY);
		contractList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				btnCompleteContract.setEnabled(true);
							
				currentContract = (String) list.getSelectedValue();
				
				try {
					if (currentContract.contains("[COMPLETE]"))
					{
						btnCompleteContract.setText("Undo Contract");
					}
					else
					{
						btnCompleteContract.setText("Complete Contract");
					}
				} catch(Exception ex){}
			}
		});
		
		
		scrollPane.setViewportView(contractList);
		
		btnCompleteContract = new JButton("Complete Contract");
		btnCompleteContract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] s = new String[killTypes.size()];
				
				int i = 0;
				for (String key : killTypes.keySet())
				{
					s[i] = key;
					
					i ++;
				}
				
				new CompleteContract(currentContract);
				try {CompleteContract.comboBox.setModel(new DefaultComboBoxModel(s));} catch(Exception e){}
			}
		});
		btnCompleteContract.setBackground(Color.DARK_GRAY);
		btnCompleteContract.setForeground(Color.WHITE);
		btnCompleteContract.setEnabled(false);
		btnCompleteContract.setBounds(760, 382, 242, 23);
		contentPane.add(btnCompleteContract);
		
		JLabel lblActiveContracts = new JLabel("Active Contracts:");
		lblActiveContracts.setForeground(Color.WHITE);
		lblActiveContracts.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblActiveContracts.setBounds(758, 469, 147, 14);
		contentPane.add(lblActiveContracts);
		
		JLabel lblTargetedBy = new JLabel("Targeted by:");
		lblTargetedBy.setForeground(Color.WHITE);
		lblTargetedBy.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTargetedBy.setBounds(758, 489, 147, 14);
		contentPane.add(lblTargetedBy);
		
		JLabel lblCompletedContracts = new JLabel("Completed Contracts:");
		lblCompletedContracts.setForeground(Color.WHITE);
		lblCompletedContracts.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCompletedContracts.setBounds(758, 514, 147, 14);
		contentPane.add(lblCompletedContracts);
		
		activeContractsCount = new JLabel("0");
		activeContractsCount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		activeContractsCount.setForeground(Color.WHITE);
		activeContractsCount.setBounds(915, 469, 46, 14);
		contentPane.add(activeContractsCount);
		
		targettedByCount = new JLabel("0");
		targettedByCount.setForeground(Color.WHITE);
		targettedByCount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		targettedByCount.setBounds(915, 490, 46, 14);
		contentPane.add(targettedByCount);
		
		completedContractsCount = new JLabel("0");
		completedContractsCount.setForeground(Color.WHITE);
		completedContractsCount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		completedContractsCount.setBounds(915, 515, 46, 14);
		contentPane.add(completedContractsCount);
		
		JLabel lblPoints = new JLabel("Points:");
		lblPoints.setForeground(Color.WHITE);
		lblPoints.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPoints.setBounds(760, 551, 147, 14);
		contentPane.add(lblPoints);
		
		pointsCount = new JLabel("0");
		pointsCount.setForeground(Color.WHITE);
		pointsCount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pointsCount.setBounds(917, 552, 46, 14);
		contentPane.add(pointsCount);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.WHITE);
		separator_4.setBounds(748, 576, 264, 1);
		contentPane.add(separator_4);
		
		JLabel lblTodaysDoublePoints = new JLabel("Today's Double Points Kill Method");
		lblTodaysDoublePoints.setForeground(Color.WHITE);
		lblTodaysDoublePoints.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTodaysDoublePoints.setBounds(758, 588, 244, 23);
		contentPane.add(lblTodaysDoublePoints);
		
		doublePointsKillMethod = new JLabel("Long Melee");
		doublePointsKillMethod.setHorizontalAlignment(SwingConstants.CENTER);
		doublePointsKillMethod.setForeground(Color.RED);
		doublePointsKillMethod.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doublePointsKillMethod.setBounds(785, 612, 176, 14);
		contentPane.add(doublePointsKillMethod);
		
		chckbxUpdateMethod = new JCheckBox("Update Method at Midnight");
		chckbxUpdateMethod.setForeground(Color.WHITE);
		chckbxUpdateMethod.setFont(new Font("Tahoma", Font.PLAIN, 11));
		chckbxUpdateMethod.setBackground(Color.BLACK);
		chckbxUpdateMethod.setBounds(757, 633, 245, 23);
		contentPane.add(chckbxUpdateMethod);
		
		buttonRefresh = new JButton("Refresh");
		buttonRefresh.setForeground(Color.WHITE);
		buttonRefresh.setBackground(Color.DARK_GRAY);
		buttonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SQLManager.refreshAll();
			}
			
		});
		buttonRefresh.setBounds(25, 575, 650, 23);
		contentPane.add(buttonRefresh);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(25, 619, 515, 23);
		contentPane.add(progressBar);
		
		lblProgressBar = new JLabel("");
		lblProgressBar.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgressBar.setFont(new Font("Prestige Elite Std", Font.PLAIN, 15));
		lblProgressBar.setForeground(Color.WHITE);
		lblProgressBar.setBounds(25, 642, 515, 14);
		contentPane.add(lblProgressBar);
		
		progressScroller = new ProgressScroller(lblProgressBar);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.WHITE);
		separator_6.setBounds(10, 607, 740, 1);
		contentPane.add(separator_6);
		
		chckbxShowActiveContracts = new JCheckBox("Show Active Contracts Only");
		chckbxShowActiveContracts.setSelected(true);
		chckbxShowActiveContracts.setForeground(Color.WHITE);
		chckbxShowActiveContracts.setBackground(Color.BLACK);
		chckbxShowActiveContracts.setBounds(756, 406, 246, 23);
		contentPane.add(chckbxShowActiveContracts);
		
		btnForceNewContracts = new JButton("Force New Contracts");
		btnForceNewContracts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String pass = JOptionPane.showInputDialog("Please input your master password to force contracts");
				
				if (pass.equals("HailFire"))
				{
					generateNextContracts();
				}
				
			}
		});
		btnForceNewContracts.setBounds(550, 619, 185, 23);
		contentPane.add(btnForceNewContracts);
		
		JButton btnSendMassEmail = new JButton("Send Mass Email");
		btnSendMassEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new SendEmail();
			}
		});
		btnSendMassEmail.setForeground(Color.WHITE);
		btnSendMassEmail.setBackground(Color.DARK_GRAY);
		btnSendMassEmail.setBounds(760, 430, 242, 23);
		contentPane.add(btnSendMassEmail);
		
		JButton btnNewButton = new JButton("POINTS");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.DARK_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new PointsWindow();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton.setBounds(932, 545, 75, 26);
		contentPane.add(btnNewButton);
		
		new Thread(new Runnable() {
			
			public void run() {
				MySQL.initialiseSQL();				
				
				SQLManager.bootup();
				
				ftpManager = new FTPManager(MainWindow.me, FTP_HOST, FTP_USERNAME, FTP_PASSWORD);
				
				SQLManager.refreshAll();
			};
		}).start();
		
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						while(true)
						{
							SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss    dd/MM/YYYY");
							String date = format.format(new Date());
							
							SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
							String date2 = format2.format(new Date());
							
							if (date2.equals("00:00:00"))
							{
								midnight();
							}
							
							lblTime.setText(date);
							
							try{Thread.sleep(1000);} catch(Exception e){}
						}
					}
				}).start();
	}
	
	public void midnight()
	{
		System.out.println("Midnight");
		
		SQLManager.refreshAll();
		
		if (chckbxUpdateMethod.isSelected())
		{
			generateNewMethod();
		}
		
		if (chckbxAssignContracts.isSelected())
		{
			generateNextContracts();
		}
	}
	
	public static void generateNextContracts()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				disableAll();
				
				MainWindow.me.progressBar.setMinimum(0);
				MainWindow.me.progressBar.setMaximum(MainWindow.me.PLAYERS.size());
				MainWindow.me.progressScroller.setString("GENERATING CONTRACTS");
				
				for (Player p : MainWindow.me.PLAYERS)
				{					
					List<Player> otherTeam1 = new ArrayList<Player>();
					List<Player> otherTeam2 = new ArrayList<Player>();
					
					List<String> teams = new ArrayList<String>();
					teams.add("Dragon Army");
					teams.add("Rocket");
					teams.add("Sparta");
					
					teams.remove(p.getFaction());
					
					for (Player p2 : MainWindow.me.PLAYERS)
					{
						if (p2.getFaction().equals(teams.get(0)))
						{
							otherTeam1.add(p2);
						}
						else if (p2.getFaction().equals(teams.get(1)))
						{
							otherTeam2.add(p2);
						}
					}
					
					List<Contract> activeContracts = new ArrayList<Contract>();
					
					for (Contract c : MainWindow.me.CONTRACTS)
					{
						//if (c.isComplete()) {continue;}
						
						if (c.getAssassin() == p.getID())
						{
							if (!c.isComplete()) {activeContracts.add(c);}
							
							List<Player> toRemove1 = new ArrayList<Player>();
							for (Player removeC : otherTeam1)
							{
								if (removeC.getID() == c.getTarget())
								{
									toRemove1.add(removeC);
								}
							}
							
							for (Player getRid : toRemove1)
							{
								otherTeam1.remove(getRid);
							}
							
							List<Player> toRemove2 = new ArrayList<Player>();
							for (Player removeC : otherTeam2)
							{
								if (removeC.getID() == c.getTarget())
								{
									toRemove2.add(removeC);
								}
							}
							
							for (Player getRid : toRemove2)
							{
								otherTeam2.remove(getRid);
							}
						}
					}
					
					Player selected1 = null;
					
					if (otherTeam1.size() > 0)
					{
						selected1 = otherTeam1.get(new Random().nextInt(otherTeam1.size()));
					}
					
					Player selected2 = null;
					
					if (otherTeam2.size() > 0)
					{
						selected2 = otherTeam2.get(new Random().nextInt(otherTeam2.size()));
					}
					
					String newContract = "Greetings Assassin!\n \n" + 
											"A new dawn, a new day, which brings new contracts!\n \n" + "Today's DOUBLE POINTS method is " + MainWindow.me.doublePointsKillMethod.getText() + "\n \n";
					
					String link1 = null;
					String name1 = null;
					
					if (selected1 != null)
					{
						newContract = newContract + "Contract #1 from " + selected1.getFaction() + "\n" + 
													"Target: " + selected1.getName() + "\n" +
													"College: " + selected1.getCollege() + "\n" + 
													"Course: " + selected1.getCourse() + "\n" +
													"Year: " + selected1.getYear() + "\n" + 
													"Accommodation: " + selected1.getAccommodation() + "\n" + 
													"Other Info: " + selected1.getOtherInfo() + "\n" + 
													"Profile Image is attached\n \n";
						
						
						link1 = System.getProperty("user.dir") + "/Images/" + selected1.getID() + ".png";
						name1 = selected1.getName();
						
						SQLManager.downloadPlayerImage(selected1.getID());
						
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
						String date = df.format(new Date()); 
						MySQL.executeUpdate("INSERT INTO `HAZ_Contracts` (`dateSet`,`assassin`,`target`) VALUES ('" + date + "','" + p.getID() + "','" + selected1.getID() + "')");
					}
					else
					{
						newContract = newContract + "There is no contract available for Faction " + teams.get(0) + "! Get some kills to open up contracts!\n \n";
					}
					
					String link2 = null;
					String name2 = null;
					
					if (selected2 != null)
					{
						newContract = newContract + "Contract #2 from " + selected2.getFaction() + "\n" + 
								"Target: " + selected2.getName() + "\n" +
								"College: " + selected2.getCollege() + "\n" + 
								"Course: " + selected2.getCourse() + "\n" +
								"Year: " + selected2.getYear() + "\n" + 
								"Accommodation: " + selected2.getAccommodation() + "\n" + 
								"Other Info: " + selected2.getOtherInfo() + "\n" + 
								"Profile Image is attached\n \n";
						
						link2 = System.getProperty("user.dir") + "/Images/" + selected2.getID() + ".png";
						name2 = selected2.getName();
						
						SQLManager.downloadPlayerImage(selected2.getID());
						
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
						String date = df.format(new Date()); 
						MySQL.executeUpdate("INSERT INTO `HAZ_Contracts` (`dateSet`,`assassin`,`target`) VALUES ('" + date + "','" + p.getID() + "','" + selected2.getID() + "')");
					}
					else
					{
						newContract = newContract + "There is no contract available for Faction " + teams.get(1) + "! Get some kills to open up contracts!\n \n";
					}
					
					if (activeContracts.size() > 0)
					{
						newContract = newContract + "As a little reminder, here are your active contracts: \n";
						
						for (Contract c : activeContracts)
						{
							Player target = null;
							
							for (Player pl : MainWindow.me.PLAYERS)
							{
								if (pl.getID() == c.getTarget())
								{
									target = pl;
									break;
								}
							}
							
							newContract = newContract + "Target: " + target.getName() + " set " + c.getDateSet() + "\n";
						}
						
						newContract = newContract + "\n";
					}
					
					newContract = newContract + "Remember to report ALL your kills to this email!\n \n" + "Regards\nThe Machine";
				
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
					String date = df.format(new Date()); 
					
					String final1 = "";
					String final2 = "";
					
					try {
						final1 = new File(link1).getPath();
						final2 = new File(link2).getPath();
					} catch (Exception e){}
					
					Mail.sendMail(p.getEmail(), "New Contracts " + date, newContract, Arrays.asList(final1, final2), Arrays.asList(name1, name2));
					
					MainWindow.me.progressBar.setValue(MainWindow.me.progressBar.getValue() + 1);
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}

				MainWindow.me.progressScroller.setString("");
				MainWindow.me.progressBar.setValue(0);
				MainWindow.notifScroller.setString("CONTRACTS ASSIGNED");
				
				enableAll();
				
				SQLManager.refreshAll();
			}
		}).start();
		
	}
	
	public static void generateNewMethod()
	{
		for (int i = 0 ; i < MainWindow.me.KILL_TYPES.size(); i++)
		{
			if (MainWindow.me.CURRENT_DOUBLE_POINTS.equals(MainWindow.me.KILL_TYPES.get(i)))
			{
				String newMethod = "";
				
				if (i == 6)
				{
					newMethod = MainWindow.me.KILL_TYPES.get(0);
				}
				else
				{
					newMethod = MainWindow.me.KILL_TYPES.get(i + 1);
				}
				
				MySQL.executeUpdate("UPDATE `HAZ_Assassins` SET `value`='" + newMethod + "' WHERE `type`='method'");
				
				SQLManager.refreshAll();
				
				return;
			}
		}
	}
	
	public class ProgressScroller extends TextScroller
	{
		public ProgressScroller(JLabel label)
		{
			super(label);
		}
		
		@Override
		public void run() 
		{
			List<Character> previous = new ArrayList<Character>();
			  
			while(true)
			{
				char c = str.charAt(0);
				String rest = str.substring(1);
				  
				str = rest + c;
				  
				label.setText(str);
				  
				try{
					Thread.sleep(200);
				}catch(InterruptedException e){}
			}
		}
	}
	
	public static void disableAll()
	{
		MainWindow.me.nameTxt.setEnabled(false);
		MainWindow.me.emailTxt.setEnabled(false);
		MainWindow.me.collegeTxt.setEnabled(false);
		MainWindow.me.courseTxt.setEnabled(false);
		MainWindow.me.yearTxt.setEnabled(false);
		MainWindow.me.accommTxt.setEnabled(false);
		MainWindow.me.SocietiesTxt.setEnabled(false);
		MainWindow.me.chckbxAssignContracts.setEnabled(false);
		MainWindow.me.contractList.setEnabled(false);
		MainWindow.me.RocketList.setEnabled(false);
		MainWindow.me.SpartaList.setEnabled(false);
		MainWindow.me.DragonList.setEnabled(false);
		MainWindow.me.btnSave.setEnabled(false);
		MainWindow.me.btnChangeProfileImage.setEnabled(false);
		MainWindow.me.btnCompleteContract.setEnabled(false);
		MainWindow.me.chckbxUpdateMethod.setEnabled(false);
		MainWindow.me.chckbxShowActiveContracts.setEnabled(false);
		MainWindow.me.buttonRefresh.setEnabled(false);
		MainWindow.me.btnAddToRocket.setEnabled(false);
		MainWindow.me.btnAddToSparta.setEnabled(false);
		MainWindow.me.btnAddToDragon.setEnabled(false);
		MainWindow.me.btnForceNewContracts.setEnabled(false);
	}
	
	public static void enableAll()
	{
		MainWindow.me.nameTxt.setEnabled(true);
		MainWindow.me.emailTxt.setEnabled(true);
		MainWindow.me.collegeTxt.setEnabled(true);
		MainWindow.me.courseTxt.setEnabled(true);
		MainWindow.me.yearTxt.setEnabled(true);
		MainWindow.me.accommTxt.setEnabled(true);
		MainWindow.me.SocietiesTxt.setEnabled(true);
		MainWindow.me.chckbxAssignContracts.setEnabled(true);
		MainWindow.me.contractList.setEnabled(true);
		MainWindow.me.RocketList.setEnabled(true);
		MainWindow.me.SpartaList.setEnabled(true);
		MainWindow.me.DragonList.setEnabled(true);
		MainWindow.me.btnSave.setEnabled(true);
		MainWindow.me.btnChangeProfileImage.setEnabled(true);
		MainWindow.me.btnCompleteContract.setEnabled(true);
		MainWindow.me.chckbxUpdateMethod.setEnabled(true);
		MainWindow.me.chckbxShowActiveContracts.setEnabled(true);
		MainWindow.me.buttonRefresh.setEnabled(true);
		MainWindow.me.btnAddToRocket.setEnabled(true);
		MainWindow.me.btnAddToSparta.setEnabled(true);
		MainWindow.me.btnAddToDragon.setEnabled(true);
		MainWindow.me.btnForceNewContracts.setEnabled(true);
	}
}

