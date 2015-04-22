package rssDownloader;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import java.awt.*;
import java.awt.event.*;

public class RSSDownloaderGui extends JFrame{
	
	public static void main(String[] args){
		new RSSDownloaderGui();
	}
	
	public RSSDownloaderGui(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("RSS Downloader");
		this.setLocationRelativeTo(null);
		
		this.add(new DownloaderPanel().getPanel());
		this.pack();
		this.setVisible(true);
	}
}
