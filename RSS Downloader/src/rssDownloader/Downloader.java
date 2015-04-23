package rssDownloader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class Downloader{
	private String destination;
	private int NUM_CONCURRENT_DOWNLOADS;
	private ForkJoinPool workerCreator;
	private DefaultTableModel table;

	public Downloader(String destination, int numParrallelDownloads, DefaultTableModel table){
		this.destination = destination;
		this.NUM_CONCURRENT_DOWNLOADS = numParrallelDownloads;
		this.table = table;
	}

	public void download(HashMap<String, String> filesSources){
		workerCreator = new ForkJoinPool(NUM_CONCURRENT_DOWNLOADS);
		for(Entry<String, String> entry: filesSources.entrySet()){
			Object[] row = {entry.getKey(), "Pending", "0%"};
			table.addRow(row);
			workerCreator.execute(
					new Worker(entry.getKey(), destination, entry.getValue(), row));
		}
		while(!workerCreator.isQuiescent());
		workerCreator.shutdownNow();
	}

	private class Worker extends RecursiveAction {
		private String fileURL, filename;
		private Path fileDestination;
		private Object[] row;

		public Worker(String filename, String fileDestination, String fileURL, Object[] row){
			this.fileURL = fileURL;
			this.fileDestination = Paths.get(fileDestination);
			this.filename = filename;
			this.row = row;
		}

		public void compute(){
			if(Files.exists(fileDestination)&& Files.isDirectory(fileDestination)
					&& !Files.exists(Paths.get(destination + filename))){
				try {
					row[1] = "Downloading";
					URL website = new URL(fileURL);
					long downloaded = 0;
					int fileSize = website.openConnection().getContentLength();
					int read;
					byte data[] = new byte[1024];
					InputStream toGet = new BufferedInputStream(website.openStream());
					FileOutputStream file = new FileOutputStream(destination + filename);
					while((read = toGet.read(data)) != -1){
						downloaded += read;
						row[2] = updateProgress(downloaded, fileSize) + "%";
						file.write(data);
					}
				} catch (Exception e) {
				}
			}
		}
		
		private int updateProgress(long downloaded, int fileSize){
			return ((int) ((downloaded / fileSize) * 100));
		}
	}
}
