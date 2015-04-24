package rssDownloader;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	public boolean isValidURL(String url){
		Pattern p = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(https?://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
		Matcher m = p.matcher(url);
		return m.matches();
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
