package me.JoeShuff.Assassins.Objects;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextScroller implements Runnable {
	  protected JLabel label;
	  protected String str = "The Message to Scroll";

	  Integer removeIn = 50;
	  boolean completedMessage = false;
	  
	  public TextScroller(JLabel label) 
	  {
	    this.label = label;
	    setString(label.getText());
	    Thread t = new Thread(this);
	    t.start();
	  }

	  public void setString(String message)
	  {
		  str = message + "          ";
		  completedMessage = false;
		  removeIn = 50;
	  }
	  
	  public void run()
	  {
		  List<Character> previous = new ArrayList<Character>();
		  
		  while(true)
		  {
			  char c = str.charAt(0);
			  String rest = str.substring(1);
			  
			  str = rest + c;
			  
			  label.setText(str);
			  removeIn --;
			  
			  if (removeIn <= 0)
			  {
				  label.setText("");
				  
			  }
			  
			  try{
				  Thread.sleep(200);
			  }catch(InterruptedException e){}
		  }
	  }
}
