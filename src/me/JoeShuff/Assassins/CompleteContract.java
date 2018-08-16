package me.JoeShuff.Assassins;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import me.JoeShuff.Assassins.Objects.Contract;
import me.JoeShuff.Assassins.Objects.Mail;
import me.JoeShuff.Assassins.Objects.Player;
import me.JoeShuff.Assassins.SQL.MySQL;
import me.JoeShuff.Assassins.SQL.SQLManager;

public class CompleteContract extends JFrame {

	private JFrame frame;

	private JPanel contentFrame;
	private JTextField textField;
	
	private Contract currentContract = null;
	public static JComboBox comboBox;
	private JLabel lblDoublePoints;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompleteContract window = new CompleteContract("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CompleteContract(String contract) {
		initialize(contract);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String contract) {
		
		int id = -2;
		if (!contract.equals("") && !contract.contains("[COMPLETE]"))
		{
			id = Integer.valueOf(contract.split(" ")[0].trim());
		}
		
		if (contract.contains("[COMPLETE]"))
		{
			id = Integer.valueOf(contract.replace("[COMPLETE] ", "").split(" ")[0].trim());
			
			for (Contract c : MainWindow.me.CONTRACTS)
			{
				if (c.getID() == id)
				{
					currentContract = c;
					break;
				}
			}
			
			try
			{
				Connection conn = MySQL.getConnection();
				Statement s = conn.createStatement();
				
				ResultSet rs = null;
				
				if (currentContract.getMethod().contains("Target"))
				{
					rs = s.executeQuery("SELECT `points` FROM `HAZ_Players` WHERE `id`='" + currentContract.getTarget() + "'");
				}
				else
				{
					rs = s.executeQuery("SELECT `points` FROM `HAZ_Players` WHERE `id`='" + currentContract.getAssassin() + "'");
				}

				int points = 0;
				if (rs.next())
				{
					points = rs.getInt("points");
				}
				
				points = points - currentContract.getPointsEarned();
				
				if (currentContract.getMethod().contains("Target"))
				{
					s.executeUpdate("UPDATE `HAZ_Players` SET `points`='" + points + "' WHERE `id`='" + currentContract.getTarget() + "'");
				}
				else
				{
					s.executeUpdate("UPDATE `HAZ_Players` SET `points`='" + points + "' WHERE `id`='" + currentContract.getAssassin() + "'");
				}
				
				s.executeUpdate("UPDATE `HAZ_Contracts` SET `complete`='false', `pointsEarned`='0', `method`='None' WHERE `id`='" + currentContract.getID() + "'");
				
				if (currentContract.getTarget() == -1)
				{
					s.executeUpdate("DELETE FROM `HAZ_Contracts` WHERE `id`='" + currentContract.getID() + "'");
				}
				
				//Send confirmation email
				Player assassin = null;
				Player target = null;
				
				for (Player p : MainWindow.me.PLAYERS)
				{
					if (p.getID() == currentContract.getAssassin())
					{
						assassin = p;
					}
					
					if (p.getID() == currentContract.getTarget())
					{
						target = p;
					}
					
					if (currentContract.getTarget() == -1)
					{
						target = new Player(-1, "The Gods", "Popup Target", 0, "null@", "", "", "", "", "");
					}
				}
				
				String assassinConfirmation = "Greetings Assassin,\n \n" + 
												"According to our records, the following contract has been nullified.\n \n" +
												"Target: " + target.getName() + "\n" +
												"Kill Method: " + currentContract.getMethod() + "\n \n" +
												"If this has been made in error, please let us know! \n \n" + 
												"Regards\n" + "The Machine";
				
				Mail.sendMail(assassin.getEmail(), "Contract Nullified on " + target.getName().split(" ")[0], assassinConfirmation, null, null);
				
				String targetConfirmation = "Greetings Assassin, \n \n" + 
											"According to our records, the contract " + assassin.getName() + " has on you has been nullified!\n \n" +
											"If this is not correct then please let us know! \n \n" +
											"Regards\n" + "The Machine";
				
				Mail.sendMail(target.getEmail(), "Contract Nullified from " + assassin.getName().split(" ")[0], targetConfirmation, null, null);
				
				setVisible(false);
				dispose();
				
				SQLManager.refreshAll();
				
				MainWindow.notifScroller.setString("CONTRACT UNDONE");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return;
		}
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddPlayer.class.getResource("/biohazard.png")));
		getContentPane().setBackground(Color.BLACK);
		setBounds(100, 100, 358, 195);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		contentFrame = new JPanel();
		contentFrame.setBackground(Color.BLACK);
		setContentPane(contentFrame);
		contentFrame.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Choose a Kill Method");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(10, 54, 122, 14);
		contentFrame.add(lblNewLabel);
		
		JLabel lblContractInfo = new JLabel("CONTRACT INFO");
		lblContractInfo.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblContractInfo.setForeground(Color.WHITE);
		lblContractInfo.setBounds(10, 11, 476, 26);
		contentFrame.add(lblContractInfo);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Nerf Gun", "Small Melee", "Large Melee", "Poison", "Shadow Game - Assassin", "Shadow Game - Target"}));
		comboBox.setForeground(Color.WHITE);
		comboBox.setBackground(Color.BLACK);
		comboBox.setBounds(153, 51, 152, 20);
		comboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        
		    	lblDoublePoints.setVisible(false);
		    	
		    	String selected = (String) ((JComboBox) e.getSource()).getSelectedItem();
		    	textField.setText("" + MainWindow.me.killTypes.get(selected));
		    	
		    	lblDoublePoints.setText("x2");
		    	
		    	if (selected.contains(MainWindow.me.CURRENT_DOUBLE_POINTS))
		    	{
		    		textField.setText("" + (2 * MainWindow.me.killTypes.get(selected)));
		    		lblDoublePoints.setVisible(true);
		    	}
		    	
		    	if (currentContract.getID() == -1)
		    	{
		    		if (selected.contains(MainWindow.me.CURRENT_DOUBLE_POINTS))
		    		{
		    			textField.setText("" + (4 * MainWindow.me.killTypes.get(selected)));
		    			lblDoublePoints.setText("x4");
			    		lblDoublePoints.setVisible(true);
		    		}
		    		else
		    		{
		    			textField.setText("" + (2 * MainWindow.me.killTypes.get(selected)));
			    		lblDoublePoints.setVisible(true);
		    		}
		    	}
		    	
		    }
		});
		contentFrame.add(comboBox);
		
		textField = new JTextField();
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.WHITE);
		textField.setBounds(153, 82, 152, 20);
		contentFrame.add(textField);
		textField.setColumns(10);
		
		JLabel lblPointsForKill = new JLabel("Points for Kill");
		lblPointsForKill.setForeground(Color.WHITE);
		lblPointsForKill.setBounds(10, 85, 265, 14);
		contentFrame.add(lblPointsForKill);
		
		JLabel lblFeelFreeTo = new JLabel("(Feel free to change for custom points)");
		lblFeelFreeTo.setForeground(Color.WHITE);
		lblFeelFreeTo.setBounds(10, 105, 327, 14);
		contentFrame.add(lblFeelFreeTo);
		
		JButton btnNewButton = new JButton("Confirm Contract");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (!SQLManager.isValidNumber(textField.getText()) || comboBox.getSelectedItem() == null)
				{
					JOptionPane.showMessageDialog(null, "Invalid points amount");
					return;
				}
				
				try
				{
					Connection conn = MySQL.getConnection();
					Statement s = conn.createStatement();
					
					ResultSet rs = null;
					
					if (currentContract.getID() == -1)
					{
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
						String date = df.format(new Date()); 
						
						s.executeUpdate("INSERT INTO `HAZ_Contracts`(`dateSet`, `method`, `assassin`, `target`, `complete`, `pointsEarned`) VALUES ('" + date + "','" + comboBox.getSelectedItem() + "','" + currentContract.getAssassin() + "','-1','true','" + textField.getText() + "')");
					}
					else
					{
						s.executeUpdate("UPDATE `HAZ_Contracts` SET `complete`='true', `pointsEarned`='" + textField.getText() + "', `method`='" + comboBox.getSelectedItem() + "' WHERE `id`='" + currentContract.getID() + "'");
					}

					if (((String) comboBox.getSelectedItem()).contains("Target"))
					{
						rs = s.executeQuery("SELECT `points` FROM `HAZ_Players` WHERE `id`='" + currentContract.getTarget() + "'");
					}
					else
					{
						rs = s.executeQuery("SELECT `points` FROM `HAZ_Players` WHERE `id`='" + currentContract.getAssassin() + "'");
					}
					
					int points = 0;
					if (rs.next())
					{
						points = rs.getInt("points");
					}
					
					points = points + Integer.valueOf(textField.getText());
					
					if (((String) comboBox.getSelectedItem()).contains("Target"))
					{
						s.executeUpdate("UPDATE `HAZ_Players` SET `points`='" + points + "' WHERE `id`='" + currentContract.getTarget() + "'");
					}
					else
					{
						s.executeUpdate("UPDATE `HAZ_Players` SET `points`='" + points + "' WHERE `id`='" + currentContract.getAssassin() + "'");
					}
					
					
					//Send confirmation email
					Player assassin = null;
					Player target = null;
					
					for (Player p : MainWindow.me.PLAYERS)
					{
						if (p.getID() == currentContract.getAssassin())
						{
							assassin = p;
						}
						
						if (p.getID() == currentContract.getTarget())
						{
							target = p;
						}
						
						if (currentContract.getID() == -1)
						{
							target = new Player(-1, "", "Popup Target", 0, "null@", "", "", "", "", "");
						}
					}
					
					String assassinConfirmation = "Greetings Assassin,\n \n" + 
													"According to our records, the following contract has been completed.\n \n" +
													"Target: " + target.getName() + "\n" +
													"Kill Method: " + (String) comboBox.getSelectedItem() + "\n \n";
					
					if (!((String) comboBox.getSelectedItem()).contains("Target"))
					{
						assassinConfirmation = assassinConfirmation + "Points Earned: " + Integer.valueOf(textField.getText()) + "\n" + "Total Points: " + points + "\n \n";
					}
					
					assassinConfirmation = assassinConfirmation + "If this has been made in error, please let us know! \n \n" +	"Regards\n" + "The Machine";
					
					Mail.sendMail(assassin.getEmail(), "Contract Confirmation on " + target.getName().split(" ")[0], assassinConfirmation, null, null);
					
					String targetConfirmation = "Greetings Assassin, \n \n" + 
												"According to our records, " + assassin.getName() + " has claimed a contract on you! \n \n" +
												"Kill Method: " + (String) comboBox.getSelectedItem() + "\n \n";
					
					if (((String) comboBox.getSelectedItem()).contains("Target"))
					{
						targetConfirmation = targetConfirmation + "Points Earned: " + Integer.valueOf(textField.getText()) + "\n" + "Total Points: " + points + "\n \n";
					}
					
					targetConfirmation = targetConfirmation + "If this is not correct then please let us know! \n \n" +	"Regards\n" + "The Machine";
					
					Mail.sendMail(target.getEmail(), "Contract Claim from " + assassin.getName().split(" ")[0], targetConfirmation, null, null);
					
					setVisible(false);
					dispose();
					
					SQLManager.refreshAll();
					
					MainWindow.notifScroller.setString("CONTRACT COMPLETED");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.DARK_GRAY);
		btnNewButton.setBounds(10, 130, 327, 23);
		contentFrame.add(btnNewButton);
		
		if (id == -2)
		{
			return;
		}
		
		if (id == -1)
		{
			currentContract = new Contract(-1, MainWindow.me.CURRENT_PLAYER.getID(), -1, "", false, 0, "");
		}

		for (Contract c : MainWindow.me.CONTRACTS)
		{
			if (c.getID() == id)
			{
				currentContract = c;
				break;
			}
		}
		
		int assassin = currentContract.getAssassin();
		int target = currentContract.getTarget();
		
		String assassinName = "";
		String targetName = "";
		
		for (Player p : MainWindow.me.PLAYERS)
		{
			if (p.getID() == assassin)
			{
				assassinName = p.getName();
			}
			
			if (p.getID() == target)
			{
				targetName = p.getName();
			}
			
			if (target == -1)
			{
				targetName = "Popup Target";
			}
		}
		
		lblContractInfo.setText(currentContract.getID() + " | " + assassinName + " vs " + targetName);
		
		lblDoublePoints = new JLabel("x2");
		lblDoublePoints.setForeground(Color.RED);
		lblDoublePoints.setVisible(false);
		lblDoublePoints.setFont(new Font("Viner Hand ITC", Font.PLAIN, 27));
		lblDoublePoints.setBounds(312, 78, 46, 33);
		contentFrame.add(lblDoublePoints);
	}
}
