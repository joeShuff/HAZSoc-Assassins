package me.JoeShuff.Assassins.Objects;

public class Contract {

	private int ID = -1;
	
	private int assassin = -1;
	private int target = -1;
	
	private String dateSet = "00/00/0000"; 
	
	private boolean complete = false;
	
	private int pointsEarned = 0;
	
	private String method = "";
	
	public Contract(int ID, int assassin, int target, String date, boolean complete, int points, String method)
	{
		this.ID = ID;
		this.assassin = assassin;
		this.target = target;
		this.dateSet = date;
		this.complete = complete;
		this.pointsEarned = points;
		this.method = method;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public int getAssassin()
	{
		return assassin;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public String getDateSet()
	{
		return dateSet;
	}
	
	public boolean isComplete()
	{
		return complete;
	}
	
	public int getPointsEarned()
	{
		return pointsEarned;
	}
	
	public String getMethod()
	{
		return method;
	}
}
