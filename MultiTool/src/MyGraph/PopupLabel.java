package MyGraph;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;

public class PopupLabel extends JLabel{
	String path;
	File file;
	BufferedReader stream;
	
	
	public PopupLabel(String string) {
		setPath(string);
		makeContent();
	}

	public File getFile() {
		return file;
	}

	public void setTFile(File text) {
		this.file = text;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	protected void openFile(){
		setFile(new File(getPath()));
		try {
			setStream((new BufferedReader(new FileReader(getFile()))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void loadContent(){
		setText(new String(""));
		String s;
		try {
			setText(getText()+"<html><body<p>");
			while((s=getStream().readLine())!=null){
				setText(getText()+s+"<br>");
			}
			setText(getText()+"</p></body></html>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void makeContent(){
		openFile();
		loadContent();
		closeFile();
	}
	
	protected void closeFile(){
		try {
			getStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedReader getStream() {
		return stream;
	}

	public void setStream(BufferedReader bufferedReader) {
		this.stream = bufferedReader;
	}

	public void setFile(File file) {
		this.file = file;
	}


}
