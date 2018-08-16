package me.JoeShuff.Assassins.SQL;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import me.JoeShuff.Assassins.AddPlayer;
import me.JoeShuff.Assassins.MainWindow;
import me.JoeShuff.Assassins.Objects.Contract;
import me.JoeShuff.Assassins.Objects.Player;

public class SQLManager {

	public static void bootup()
	{
		//Test if main table exists
		String playerTable = "CREATE TABLE `HAZ_Players` (`id` int(11) NOT NULL AUTO_INCREMENT,`active` char(5) NOT NULL DEFAULT 'true', `faction` char(25) NOT NULL, `points` int(10) NOT NULL DEFAULT '0', `name` varchar(60) NOT NULL DEFAULT 'Playername', `email` varchar(100) NOT NULL DEFAULT 'user@york.ac.uk', `college` varchar(50) NOT NULL, `course` varchar(50) NOT NULL, `year` varchar(100) NOT NULL DEFAULT '1', `accommodation` varchar(100) NOT NULL DEFAULT 'on campus', `other` varchar(1000) NOT NULL DEFAULT 'None', PRIMARY KEY (`id`));";
			
		if (!MySQL.tableExists("HAZ_Players"))
		{
			if (MySQL.executeUpdate(playerTable))
			{
				
			}
		}
		
		String contractTable = "CREATE TABLE `HAZ_Contracts` (`id` int(11) NOT NULL AUTO_INCREMENT, `dateSet` char(50) NOT NULL DEFAULT '00/00/0000', `method` char(100) NOT NULL DEFAULT 'None', `assassin` int(10) NOT NULL DEFAULT '-1', `target` int(10) NOT NULL DEFAULT '-1', `complete` char(10) NOT NULL DEFAULT 'false', `pointsEarned` int(10) NOT NULL DEFAULT '0', PRIMARY KEY (`id`));";
		
		if (!MySQL.tableExists("HAZ_Contracts"))
		{
			if (MySQL.executeUpdate(contractTable))
			{
				
			}
		}
		
		String controlTable = "CREATE TABLE `HAZ_Assassins` (`type` char(10), `value` char(100))";
		
		if (!MySQL.tableExists("HAZ_Assassins"))
		{
			if (MySQL.executeUpdate(controlTable))
			{
				MySQL.executeUpdate("INSERT INTO `HAZ_Assassins` (`type`, `value`) VALUES ('method','Small Melee')");
			}
		}
	}
	
	public static boolean isValidNumber(String number)
	{
		try
		{
			int test = Integer.valueOf(number);
			
			return true;
		}
		catch (Exception e){}
		
		try
		{
			double test = Double.parseDouble(number);
			
			return true;
		}
		catch (Exception e){}
		
		return false;
	}
	
	public static void refreshAll()
	{
		//Clear All Objects
		MainWindow.me.RocketList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		MainWindow.me.SpartaList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		MainWindow.me.DragonList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		MainWindow.me.contractList.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		MainWindow.me.nameTxt.setText("");
		MainWindow.me.nameTxt.setEnabled(false);
		
		MainWindow.me.emailTxt.setText("");
		MainWindow.me.emailTxt.setEnabled(false);
		
		MainWindow.me.collegeTxt.setText("");
		MainWindow.me.collegeTxt.setEnabled(false);
		
		MainWindow.me.courseTxt.setText("");
		MainWindow.me.courseTxt.setEnabled(false);
		
		MainWindow.me.yearTxt.setText("");
		MainWindow.me.yearTxt.setEnabled(false);
		
		MainWindow.me.accommTxt.setText("");
		MainWindow.me.accommTxt.setEnabled(false);
		
		MainWindow.me.SocietiesTxt.setText("");
		MainWindow.me.SocietiesTxt.setEnabled(false);
		
		MainWindow.me.activeContractsCount.setText("0");
		MainWindow.me.targettedByCount.setText("0");
		MainWindow.me.completedContractsCount.setText("0");
		MainWindow.me.pointsCount.setText("0");
		
		MainWindow.me.profileImage.setIcon(null);
		
		MainWindow.me.btnSave.setEnabled(false);
		MainWindow.me.btnChangeProfileImage.setEnabled(false);
		MainWindow.me.btnCompleteContract.setEnabled(false);
		
		MainWindow.me.PLAYERS.clear();
		MainWindow.me.CONTRACTS.clear();
		
		//Get ALL players and store in LIST
		try
		{
			Connection conn = MySQL.getConnection();
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM `HAZ_Players`");
			
			List<String> SPARTA = new ArrayList<String>();
			List<String> ROCKET = new ArrayList<String>();
			List<String> DRAGON = new ArrayList<String>();
			
			while(rs.next())
			{
				MainWindow.me.PLAYERS.add(new Player(rs.getInt("id"), rs.getString("faction"), rs.getString("name"), rs.getInt("points"), rs.getString("email"), rs.getString("college"), rs.getString("course"), rs.getString("year"), rs.getString("accommodation"), rs.getString("other")));
				
				if (rs.getString("faction").equalsIgnoreCase("sparta"))
				{
					SPARTA.add(rs.getInt("id") + " | " + rs.getString("name"));
				}
				else if (rs.getString("faction").equalsIgnoreCase("rocket"))
				{
					ROCKET.add(rs.getInt("id") + " | " + rs.getString("name"));
				}
				else
				{
					DRAGON.add(rs.getInt("id") + " | " + rs.getString("name"));
				}
			}
			
			String[] SinList = new String[SPARTA.size()];
			int i = 0;
			for (String player : SPARTA)
			{
				SinList[i] = player;
				 
				i ++;
			}
			
			MainWindow.me.SpartaList.setModel(new AbstractListModel() {
				String[] values = SinList;
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			
			String[] RinList = new String[ROCKET.size()];
			i = 0;
			for (String player : ROCKET)
			{
				RinList[i] = player;
				 
				i ++;
			}
			
			MainWindow.me.RocketList.setModel(new AbstractListModel() {
				String[] values = RinList;
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			
			String[] DinList = new String[DRAGON.size()];
			i = 0;
			for (String player : DRAGON)
			{
				DinList[i] = player;
				 
				i ++;
			}
			
			MainWindow.me.DragonList.setModel(new AbstractListModel() {
				String[] values = DinList;
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Get ALL the contracts and store in LIST
		try
		{
			Connection conn = MySQL.getConnection();
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM `HAZ_Contracts`");
			
			while (rs.next())
			{
				MainWindow.me.CONTRACTS.add(new Contract(rs.getInt("id"), rs.getInt("assassin"), rs.getInt("target"), rs.getString("dateSet"), Boolean.valueOf(rs.getString("complete")), rs.getInt("pointsEarned"), rs.getString("method")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Download the Correct Double points method
		try
		{
			Connection conn = MySQL.getConnection();
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT `value` FROM `HAZ_Assassins` WHERE `type`='method'");
			
			if (rs.next())
			{
				MainWindow.me.CURRENT_DOUBLE_POINTS = rs.getString("value");
				MainWindow.me.doublePointsKillMethod.setText(MainWindow.me.CURRENT_DOUBLE_POINTS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setActivePlayer(String data)
	{
		if (data == null)
		{
			return;
		}

		String id = data.split(" ")[0].trim();
		
		Player player = null;
		
		for (Player p : MainWindow.me.PLAYERS)
		{
			if (p.getID().toString().equals(id))
			{
				player = p;
				break;
			}
		}
		
		MainWindow.me.CURRENT_PLAYER = player;
		
		if (player == null)
		{
			MainWindow.notifScroller.setString("Error Loading Player");
			return;
		}
		
		MainWindow.me.currentImagePath = System.getProperty("user.dir") + "/Images/" + id + ".png";
		
		MainWindow.me.nameTxt.setText(player.getName());
		MainWindow.me.nameTxt.setEnabled(true);
		
		MainWindow.me.emailTxt.setText(player.getEmail());
		MainWindow.me.emailTxt.setEnabled(true);
		
		MainWindow.me.collegeTxt.setText(player.getCollege());
		MainWindow.me.collegeTxt.setEnabled(true);
		
		MainWindow.me.courseTxt.setText(player.getCourse());
		MainWindow.me.courseTxt.setEnabled(true);
		
		MainWindow.me.yearTxt.setText(player.getYear());
		MainWindow.me.yearTxt.setEnabled(true);
		
		MainWindow.me.accommTxt.setText(player.getAccommodation());
		MainWindow.me.accommTxt.setEnabled(true);
		
		MainWindow.me.SocietiesTxt.setText(player.getOtherInfo());
		MainWindow.me.SocietiesTxt.setEnabled(true);
		
		MainWindow.me.profileImage.setIcon(downloadPlayerImage(player.getID()));
		MainWindow.me.profileImage.repaint();
		
		MainWindow.me.btnSave.setEnabled(true);
		MainWindow.me.btnChangeProfileImage.setEnabled(true);
		
		MainWindow.me.nameTxt.setBackground(Color.BLACK);
		MainWindow.me.emailTxt.setBackground(Color.BLACK);
		MainWindow.me.collegeTxt.setBackground(Color.BLACK);
		MainWindow.me.courseTxt.setBackground(Color.BLACK);
		MainWindow.me.yearTxt.setBackground(Color.BLACK);
		
		MainWindow.me.accommTxt.setBackground(Color.BLACK);
		MainWindow.me.SocietiesTxt.setBackground(Color.BLACK);
		
		MainWindow.me.contractList.setModel(new AbstractListModel() {
			String[] values = new String[]{};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		List<Contract> myContracts = new ArrayList<Contract>();
		
		for (Contract c : MainWindow.me.CONTRACTS)
		{
			if (c.getAssassin() == player.getID())
			{
				myContracts.add(c);
			}
		}
		
		List<Contract> activeContracts = new ArrayList<Contract>();
		
		int active = 0;
		
		for (Contract c : myContracts)
		{
			if (!c.isComplete() || !MainWindow.me.chckbxShowActiveContracts.isSelected())
			{
				if (!c.isComplete())
				{
					active ++;
				}
				
				activeContracts.add(c);
			}
		}
		
		String[] contractDesc = new String[activeContracts.size() + 1];
		int i = 0;		
		
		for (Contract c : activeContracts)
		{
			Player toKill = null;
			
			for (Player p : MainWindow.me.PLAYERS)
			{
				if (c.getTarget() == p.getID())
				{
					toKill = p;
					break;
				}
			}
			
			if(c.getTarget() == -1)
			{
				contractDesc[i] = c.getID() + " | " + c.getDateSet() + " Popup Target";
			}
			else
			{
				contractDesc[i] = c.getID() + " | " + player.getName() + " to kill " + toKill.getName();
			}
			
			if (c.isComplete())
			{
				contractDesc[i] = "[COMPLETE] " + contractDesc[i];
			}
			
			i ++;
		}
		
		contractDesc[i] = "-1 | Todays Popup Contract";
		
		int completed = myContracts.size() - active;
		
		int targetted = 0;
		
		for (Contract c : MainWindow.me.CONTRACTS)
		{
			if (c.getTarget() == player.getID() && !c.isComplete())
			{
				targetted ++;
			}
		}
		
		int points = player.getPoints();
		
		MainWindow.me.activeContractsCount.setText("" + active);
		MainWindow.me.targettedByCount.setText("" + targetted);
		MainWindow.me.completedContractsCount.setText("" + completed);
		MainWindow.me.pointsCount.setText("" + points);
		
		MainWindow.me.contractList.setModel(new AbstractListModel() {
			String[] values = contractDesc;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	public static boolean isValidEmail(String email, boolean alreadyExists)
	{
		try
		{
			Connection conn = MySQL.getConnection();
			Statement s = conn.createStatement();
			
			ResultSet rs;
			
			if (alreadyExists)
			{
				rs = s.executeQuery("SELECT * FROM `HAZ_Players` WHERE `email`='" + email + "' AND `id`!='" + MainWindow.me.CURRENT_PLAYER.getID() + "'");
			}
			else
			{
				rs = s.executeQuery("SELECT * FROM `HAZ_Players` WHERE `email`='" + email + "'");
			}
			
			rs.last();
			int amount = rs.getRow();
			
			if (alreadyExists)
			{
				if (amount == 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (amount == 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static ImageIcon downloadPlayerImage(int id)
	{
		ImageIcon selected = null;
		
		File check = new File(System.getProperty("user.dir") + "/Images/" + id + ".png");
		
		MainWindow.me.currentImagePath = check.getPath();
		
		if (check.exists())
		{
			selected = new ImageIcon(check.getPath());
		}
		else
		{
			check.getParentFile().mkdirs();
			
			try {
				check.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MainWindow.ftpManager.client.downloadBytesToLocalFile("/Assassins/" + id + ".png", check.getPath());
			selected = new ImageIcon(check.getPath());
		}
		
		BufferedImage image = null;
	    try 
	    {
	        image = ImageIO.read(check);

	    } catch (Exception ex) 
	    {
	        ex.printStackTrace();
	    }
		
		selected = new ImageIcon(AddPlayer.fitimage(image, MainWindow.me.profileImage.getWidth(), MainWindow.me.profileImage.getHeight()));
		
		return selected;
	}
}
