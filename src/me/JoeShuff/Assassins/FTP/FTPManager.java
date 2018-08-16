package me.JoeShuff.Assassins.FTP;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Line;

import me.JoeShuff.Assassins.MainWindow;

public class FTPManager {
	
	public FTPClient client;
	
	public MainWindow plugin;
	
	public FTPManager(MainWindow plugin, String host, String username, String password) 
	{
		this.plugin = plugin;
		client = new FTPClient(host, username, password);
		System.out.println("Connecting to " + host + " with " + username + ":" + password);
	}
}
