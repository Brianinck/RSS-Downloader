package rssDownloader;

//Taken from http://stackoverflow.com/a/13755155/2980766
public class RowData{
	private String name, status, fileSize;
	private float progress;
	
	public RowData(String filename, String status, float progress, String fileSize){
		this.name = filename;
		this.status = status;
		this.progress = progress;
		this.fileSize = fileSize;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public float getProgress(){
		return this.progress;
	}
	
	public String getFileSize(){
		return this.fileSize;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public void setFileSize(String size){
		this.fileSize = size;
	}
	
	public void setProgress(float progress){
		this.progress = progress;
	}
	
	
}
