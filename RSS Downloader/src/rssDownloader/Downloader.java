package rssDownloader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class Downloader{
	private String destination;
	private int NUM_CONCURRENT_DOWNLOADS;
	private ForkJoinPool workerCreator;
	private UpdateTableModel table;

	public Downloader(String destination, int numParrallelDownloads, UpdateTableModel table){
		this.destination = destination;
		this.NUM_CONCURRENT_DOWNLOADS = numParrallelDownloads;
		this.table = table;
	}

	public void download(HashMap<String, String> filesSources){
		workerCreator = new ForkJoinPool(NUM_CONCURRENT_DOWNLOADS);
		for(Entry<String, String> entry: filesSources.entrySet()){
			table.addRow(entry.getKey(), "Pending", 0);
			workerCreator.execute(
					new Worker(entry.getKey(), destination, entry.getValue(), table));
		}
		while(!workerCreator.isQuiescent());
		workerCreator.shutdownNow();
	}

	private class Worker extends SwingWorker<Void, Void> {
		private final String fileURL, filename;
		private final Path fileDestination;
		private UpdateTableModel model;

		public Worker(final String filename, String fileDestination, String fileURL, UpdateTableModel model){
			this.fileURL = fileURL;
			this.fileDestination = Paths.get(fileDestination);
			this.filename = filename;
			this.model = model;

			addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("progress")) {
						System.out.println("YAY");
						Worker.this.model.updateProgress(filename, (int) evt.getNewValue());
					} else if(evt.getPropertyName().equals("Status")){
						System.out.println("YAY2");
						Worker.this.model.updateStatus(filename, (String)evt.getNewValue());
					}
				}
			});
		}

		@Override
		protected Void doInBackground() throws Exception {
			if(Files.exists(fileDestination)&& Files.isDirectory(fileDestination)
					&& !Files.exists(Paths.get(destination + filename))){
	//			model.updateStatus(filename, "Downloading");
				URL website = new URL(fileURL);
				long downloaded = 0;
				int fileSize = website.openConnection().getContentLength();
				int read;
				byte data[] = new byte[1024];
				InputStream toGet = new BufferedInputStream(website.openStream());
				FileOutputStream file = new FileOutputStream(destination + filename);
				while((read = toGet.read(data)) != -1){
					downloaded += read;
					int progress = (int) Math.round(((double) downloaded / (double) fileSize) * 100d);
		//			model.updateProgress(this.filename, progress);
					setProgress(progress);
					file.write(data);
				}
			}
			setProgress(100);
//			model.updateProgress(filename, 100);
	//		model.updateStatus(filename, "Complete");
			return null;
		}
	}
}
