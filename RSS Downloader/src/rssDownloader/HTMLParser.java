package rssDownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
	
/*
	private static int downloadFiles(String destination){
		Path downloadTo = Paths.get(destination);
		if(Files.exists(downloadTo)){
			for(Entry<String, String> entry : filesAndSources.entrySet()){
				String fileName = entry.getKey(), source = entry.getValue();
				try {
					URL website = new URL(source);
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream file = new FileOutputStream(destination + fileName);
					file.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				} catch (IOException e) {
					return 2;
				}
			}
			return 1;
		} else {
			return 3;
		}
	} */
}
