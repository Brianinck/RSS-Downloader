package rssDownloader;

import java.io.FileOutputStream;
import java.io.IOException;
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

public class Downloader{
	private String destination;
	private int NUM_CONCURRENT_DOWNLOADS = 5;
	private ForkJoinPool workerCreator;

	public Downloader(String destination){
		this.destination = destination;
	}

	public void download(HashMap<String, String> filesSources){
		workerCreator = new ForkJoinPool(NUM_CONCURRENT_DOWNLOADS);
		for(Entry<String, String> entry: filesSources.entrySet()){
			workerCreator.execute(
					new Worker(entry.getKey(), destination, entry.getValue()));
		}
	}

	private class Worker extends RecursiveAction {
		private String fileURL, filename;
		private Path fileDestination;

		public Worker(String fileURL, String fileDestination, String filename){
			this.fileURL = fileURL;
			this.fileDestination = Paths.get(fileDestination);
			this.filename = filename;
		}

		public void compute(){
			if(Files.exists(fileDestination)&& Files.isDirectory(fileDestination)
					&& Files.exists(Paths.get(destination + filename))){
				try {
					URL website = new URL(fileURL);
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream file = new FileOutputStream(destination + filename);
					file.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				} catch (IOException e) {
				}
			}
		}
	}
}
