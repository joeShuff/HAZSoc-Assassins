package me.JoeShuff.Assassins.Objects;

public class Player {

	private int ID;
	
	private int points;
	
	private String faction;
	
	private String name;
	private String email;
	
	private String college;
	private String course;
	private String year;
	private String accommodation;
	private String otherInfo;
	
	public Player(int ID, String faction, String name, int points, String email, String college, String course, String year, String accommod, String otherInfo)
	{
		this.ID = ID;
		this.faction = faction;
		this.name = name;
		this.points = points;
		this.email = email;
		this.college = college;
		this.course = course;
		this.year = year;
		this.accommodation = accommod;
		this.otherInfo = otherInfo;
	}
	
	public Integer getID()
	{
		return ID;
	}
	
	public String getFaction()
	{
		return faction;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getCollege()
	{
		return college;
	}
	
	public String getCourse()
	{
		return course;
	}
	
	public String getYear()
	{
		return year;
	}
	
	public String getAccommodation()
	{
		return accommodation;
	}
	
	public String getOtherInfo()
	{
		return otherInfo;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Player)
		{
			Player p = (Player) obj;
			
			return this.getID() == p.getID();
		}
		else
		{
			return false;
		}
		
	}
}
