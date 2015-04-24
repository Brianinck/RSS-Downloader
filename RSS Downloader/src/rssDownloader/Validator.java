package rssDownloader;

import java.io.File;

public class Validator {

	//TODO: this doesn't work
	public boolean isValidURL(String url){
		String urlRegex = "\\b(https?|ftp|file|ldap)://" + "[-A-Za-z0-9+&@#/%?=~_|!:,.;]"
				+ "*[-A-Za-z0-9+&@#/%=~_|]";
		return url.matches(urlRegex);
	}

	public boolean isValidDirectory(String directory){
		if(directory != null){
			File destination = new File(directory);
			return destination.exists() && destination.isDirectory()
					&& destination.canWrite();
		}
		return false;
	}
	
	public boolean isValidDownloadNum(int num){
		return num > 0;
	}
}
