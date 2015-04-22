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
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("RSS Downloader");
		this.setLocationRelativeTo(null);
		
		this.add(new DownloaderPanel().getPanel());

		this.setVisible(true);
	}
	/*
	private class urlHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(!e.getActionCommand().equals("") && isValidURL(e.getActionCommand())){
				url = e.getActionCommand();
			} else if (e.getActionCommand().equals("")) {
				urlError = "Must enter url.";
			} else {
				urlError = "Invalid url.";
			}
		}
		
		private boolean isValidURL(String url){
			
		}
	}*/
}
