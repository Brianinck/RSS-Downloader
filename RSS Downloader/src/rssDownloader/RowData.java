package rssDownloader;

public class RowData {
	private String name, status;
	private float progress;
	
	public RowData(String filename, String status, float progress){
		this.name = filename;
		this.status = status;
		this.progress = progress;
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
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public void setProgress(float progress){
		this.progress = progress;
	}
}
