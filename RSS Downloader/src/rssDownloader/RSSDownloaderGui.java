package rssDownloader;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RSSDownloaderGui extends JFrame{
	private JPanel downloadsPanel, optionsPanel, pages;
	private JTextField urlBox, directoryBox;
	private JButton downloadButton, directoryButton, closeButton;
	private JComboBox<String> fileTypes;
	private JFileChooser directorySelector;
	private JSpinner numDownloads;
	private Listener listener = new Listener();
	private UpdateTableModel downloads;
	private String[] acceptedTypes = {"mp3", "zip", "mp4", "wav", "wma", "mkv"};
	private String destination = null;
	private final String MAIN = "main";
	private final String DOWNLOADS = "downloads";
	
	public static void main(String[] args){
		new RSSDownloaderGui();
	}
	
	public RSSDownloaderGui(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("RSS Downloader");
		
		optionsPanel = makeMainPanel();
		downloadsPanel = makeDownloadsPanel();
		pages = new JPanel(new CardLayout());
		pages.add(optionsPanel, MAIN);
		pages.add(downloadsPanel, DOWNLOADS);
		
		this.add(pages);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private JPanel makeMainPanel(){
		JPanel mainPanel = new JPanel(false);
		mainPanel.setLayout(new GridBagLayout());

		//url option
		addItem(mainPanel, new JLabel("Enter url of RSS Feed: "), 0, 0, 1, 1,
				GridBagConstraints.EAST);
		urlBox = new JTextField(25);
		addItem(mainPanel, urlBox, 1, 0, 1, 1, GridBagConstraints.WEST);

		//download path
		addItem(mainPanel, new JLabel("Enter destination directory: "), 0, 1,
				1, 1, GridBagConstraints.EAST);
		directoryBox = new JTextField(25);
		addItem(mainPanel, directoryBox, 1, 1, 1, 1, GridBagConstraints.WEST);
		directoryButton = new JButton("Select Path");
		directoryButton.addActionListener(listener);
		addItem(mainPanel, directoryButton, 2, 1, 1, 1, GridBagConstraints.WEST);

		//filetype dropdown
		addItem(mainPanel, new JLabel("Choose file type: "), 0, 2, 1, 1,
				GridBagConstraints.WEST);
		fileTypes = new JComboBox<String>(acceptedTypes);
		addItem(mainPanel, fileTypes, 0, 2, 1, 1, GridBagConstraints.EAST);
		
		//number of parallel downloads
		addItem(mainPanel, new JLabel("Number of parallel downloads: "), 1, 2, 1, 1,
				GridBagConstraints.EAST);
		SpinnerModel restraints = new SpinnerNumberModel(5, 1, 15, 1);
		numDownloads = new JSpinner(restraints);
		addItem(mainPanel, numDownloads, 2, 2, 1, 1, GridBagConstraints.CENTER);
		
		//download and close buttons
		Box buttonBox = Box.createHorizontalBox();
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(listener);
		buttonBox.add(downloadButton);
		addItem(mainPanel, buttonBox, 2, 3, 1, 1, GridBagConstraints.SOUTH);
		
		return mainPanel;
	}
	
	private JPanel makeDownloadsPanel(){
		JPanel downloadsPanel = new JPanel(false);
		downloads = new UpdateTableModel();
		JTable table = new JTable(downloads);
		table.getColumn("Progress").setCellRenderer(new ProgressCellRenderer(0, 100));
		JScrollPane scrollPane = new JScrollPane(table);
		downloadsPanel.add(scrollPane);
		return downloadsPanel;
	}

	private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align){
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.gridheight = height;
		gc.weightx = 100.0;
		gc.weighty = 100.0;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = align;
		gc.fill = GridBagConstraints.NONE;
		p.add(c, gc);
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
			int numParrallelDownloads = (int) numDownloads.getValue();
			if(validator.isValidURL(url) && validator.isValidDirectory(destination)
					&& validator.isValidDownloadNum(numParrallelDownloads)){
				String fileType = (String) fileTypes.getSelectedItem();
				HashMap<String, String> filesSources = parseHTML(url, fileType);
				CardLayout layout = (CardLayout)pages.getLayout();
				this.revalidate();
				this.repaint();
				this.pack();
				layout.show(pages, DOWNLOADS);
				Downloader downloader = new Downloader(destination, numParrallelDownloads, downloads, filesSources);
				new Thread(downloader).start();
			} else {
				if(!validator.isValidURL(url))
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid url.");
				else if(!validator.isValidDirectory(destination))
					JOptionPane.showMessageDialog(new JFrame(), "Please select a valid directory.");
				else if(!validator.isValidDownloadNum(numParrallelDownloads))
					JOptionPane.showMessageDialog(new JFrame(), "Number of parrallel downloads must be greater than 0.");
			}
		}

		@SuppressWarnings("finally")
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
					if(foundFile.find()){
						filesAndSources.put(foundFile.group(1), foundFile.group(0));
						downloads.addDownload(new RowData(foundFile.group(1), "Pending", 0, "Pending"));
					}
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
				destination = directorySelector.getSelectedFile().getAbsolutePath() + "/";
				directoryBox.setText(destination);
			}
		}
	}
}
