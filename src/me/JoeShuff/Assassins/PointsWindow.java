package me.JoeShuff.Assassins;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import me.JoeShuff.Assassins.SQL.MySQL;
import javax.swing.JLabel;

public class PointsWindow extends JFrame {

	private JPanel panel;
	private JList pointsList;
	private JLabel rocketScore;
	private JLabel spartaScore;
	private JLabel dragonScore;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PointsWindow window = new PointsWindow();
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
	public PointsWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddPlayer.class.getResource("/biohazard.png")));
		getContentPane().setBackground(Color.BLACK);
		setBounds(100, 100, 498, 394);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setUndecorated(true);
		setVisible(true);
		setResizable(false);
		
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		setContentPane(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 332, 343);
		panel.add(scrollPane);
		
		pointsList = new JList();
		pointsList.setForeground(Color.WHITE);
		pointsList.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(pointsList);
		
		spartaScore = new JLabel("score");
		spartaScore.setForeground(Color.WHITE);
		spartaScore.setBounds(436, 13, 46, 14);
		panel.add(spartaScore);
		
		rocketScore = new JLabel("score");
		rocketScore.setForeground(Color.WHITE);
		rocketScore.setBounds(436, 38, 46, 14);
		panel.add(rocketScore);
		
		dragonScore = new JLabel("score");
		dragonScore.setForeground(Color.WHITE);
		dragonScore.setBounds(436, 64, 46, 14);
		panel.add(dragonScore);
		
		try
		{
			Connection conn = MySQL.getConnection();
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM `HAZ_Players` ORDER BY `points` DESC");
			
			List<String> results = new ArrayList<String>();
			
			int sparta = 0;
			int rocket = 0;
			int dragon = 0;
			
			while (rs.next())
			{
				results.add(rs.getInt("points") + " | " + rs.getString("faction") + " | " + rs.getString("name"));
				
				if (rs.getString("faction").equals("Sparta"))
				{
					sparta = sparta + rs.getInt("points");
				}
				else if (rs.getString("faction").equals("Rocket"))
				{
					rocket = rocket + rs.getInt("points");
				}
				else if (rs.getString("faction").equals("Dragon Army"))
				{
					dragon = dragon + rs.getInt("points");
				}
			}
			
			dragonScore.setText("" + dragon);
			spartaScore.setText("" + sparta);
			rocketScore.setText("" + rocket);
			
			String[] arrResults = new String[results.size()];
			arrResults = results.toArray(arrResults);
			
			final String[] arr = arrResults;
			
			pointsList.setModel(new AbstractListModel() {
				String[] values = arr;
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			
			JLabel lblSparta = new JLabel("Sparta:");
			lblSparta.setForeground(Color.WHITE);
			lblSparta.setBounds(352, 13, 68, 14);
			panel.add(lblSparta);
			
			JLabel lblRocket = new JLabel("Rocket:");
			lblRocket.setForeground(Color.WHITE);
			lblRocket.setBounds(352, 38, 68, 14);
			panel.add(lblRocket);
			
			JLabel lblDragonArmy = new JLabel("Dragon Army:");
			lblDragonArmy.setForeground(Color.WHITE);
			lblDragonArmy.setBounds(352, 64, 68, 14);
			panel.add(lblDragonArmy);
			

		}
		catch (Exception e){}
	}
}
