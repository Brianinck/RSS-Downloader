package rssDownloader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DownloaderPanel extends JPanel{
	JLabel invalidURL, invalidDestination;
	JPanel panel = new JPanel();
	JTextField urlBox, directoryBox;
	JButton downloadButton, directoryButton;
	JComboBox fileTypes;
	JFileChooser directorySelector;
	Listener listener = new Listener();
	String[] acceptedTypes = {"mp3", "zip", "mp4", "wav", "wma"};
	String destination = null;
	
	public DownloaderPanel(){
		//panel.add(new JLabel("Download from RSS Feed."));
		panel.add(new JLabel("Enter url of RSS Feed: "));
		
		urlBox = new JTextField(25);
		invalidURL = new JLabel();
		panel.add(urlBox);
		panel.add(invalidURL);
		
		panel.add(new JLabel("Enter destination directory: "));
		
		directoryBox = new JTextField(25);
		invalidDestination = new JLabel();
		panel.add(directoryBox);
		panel.add(invalidDestination);
		
		directoryButton = new JButton("Browse");
		directoryButton.addActionListener(listener);
		panel.add(directoryButton);
		
		fileTypes = new JComboBox(acceptedTypes);
		panel.add(fileTypes);

		downloadButton = new JButton("Download");
		downloadButton.addActionListener(listener);
		panel.add(downloadButton);
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	private class Listener extends JFrame implements ActionListener{
		private Validator validator = new Validator();
		
		public void actionPerformed(ActionEvent action){
			if(action.getSource() == downloadButton)
				download();
			else if(action.getSource() == directoryButton)
				selectDirectory();
		}
		
		private void download(){
			String url = urlBox.getText();
			if(validator.isValidURL(url) && validator.isValidDirectory(destination)){
				String fileType = (String) fileTypes.getSelectedItem();
				HashMap<String, String> filesSources = parseHTML(url, fileType);
				Downloader downloader = new Downloader(destination);
				downloader.download(filesSources);
			} else {
				if(!validator.isValidURL(url)){
					invalidURL.setText("Please enter a valid url.");
					invalidURL.setForeground(Color.red);
				} else {
					invalidURL.setText(""); //Clear and previous errors
				}
				if(!validator.isValidDirectory(destination)){
					invalidDestination.setText("Please select a valid directory.");
					invalidDestination.setForeground(Color.red);
				} else {
					invalidDestination.setText("");
				}
			}
		}

		private HashMap<String, String> parseHTML(String url, String format){
			HashMap<String, String> filesAndSources = new HashMap<String, String>();
			try {
				URL feed = new URL(url);
				BufferedReader html = new BufferedReader(new InputStreamReader(feed.openStream()));
				String fileRegex = 
						"(?:http:\\/\\/|https:\\/\\/)?(?:www)?[-a-zA-Z0-9@:%_\\+.~%?\\/=]+\\/((?:[-a-zA-Z0-9_.]+)\\."+format+")";
				Pattern filePattern = Pattern.compile(fileRegex);
				String htmlLine;
				while((htmlLine = html.readLine()) != null){
					Matcher foundFile = filePattern.matcher(htmlLine);
					if(foundFile.find())
						filesAndSources.put(foundFile.group(1), foundFile.group(0));
				}
			} catch (MalformedURLException e) {
				System.out.println("Malformed URL");
			} catch (IOException e) {
				System.out.println("IOException");
			} finally {
				return filesAndSources;
			}
		}
		
		private void selectDirectory(){
			directorySelector = new JFileChooser();
			directorySelector.setFileHidingEnabled(true);
			directorySelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = directorySelector.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
				destination = directorySelector.getSelectedFile().getAbsolutePath();
				directoryBox.setText(destination);
			}
		}
	}
}
