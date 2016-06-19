package client;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

public class Storage {
	
	
	String msgq[];
	String ipq[];
	String msg;
	int front=-1,rear=-1;
	Map<String,Integer> unread;
	String location;
	File file;
	Storage(){
		unread= new HashMap<String,Integer>();
		ipq= new String[50];
		msgq= new String[50];
		location=new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		location=location+"/LetsChat";
		File f=new File(location);
		f.mkdir();
	}


	
	
	public void storeFile(String message,String name) {//stores unread messages
		
		String path;
		int flag=0;
		for(Map.Entry<String, Integer> entry : unread.entrySet()){//adding the number of unread to hash
			String key=entry.getKey();
			if(key.equals(name)){
				unread.put(key, unread.get(key) + 1);
				flag=1;
				break;
			}		
		}
		
		if(flag!=1){unread.put(name,1);}
		flag=0;
		path=location+"/"+name;
		file= new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		try{
			PrintWriter outFile = new PrintWriter(new FileWriter(path+"/unread.txt",true));
			outFile.println(message);
			outFile.close();
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public String[] extractFile(String name) {//extracts unread messages and store to main messages
		
		String tosend[]=new String[20];
		int i=0;
		String path;
		path=location+"/"+name;
		System.out.println(path);
		try{
			File f=new File(path+"/unread.txt");
			FileInputStream in= new FileInputStream(path+"/unread.txt");
			//FileOutputStream out = new FileOutputStream(path+"/message.txt",new Boolean(true));
		    BufferedReader br= new BufferedReader(new InputStreamReader(in));
		    PrintWriter outFile = new PrintWriter(new FileWriter(path+"/message.txt",true));
		    String msg;
			while((msg=br.readLine())!=null){
				
				tosend[i]=msg;
				outFile.println(new String("#R: "+msg));
				System.out.println("MESSAGE= "+tosend[i]);
				i++;
			}
			i=0;
			in.close();
			outFile.close();
			br.close();
			f.delete();
		}catch(Exception e){e.printStackTrace();}
		
		List<String> list = new ArrayList<String>();
		for(String s : tosend){
			if(s!=null && s.length()>0){
				list.add(s);
			}
		}
		
		
		return list.toArray(new String[list.size()]);
	}
	
	
	public void storeMessage(String name,String message, boolean send){// stores send messages and received messages if currently chatting.
		String path;
		path=location+"/"+name;
		
		if(!new File(path).isDirectory()){
			new File(path).mkdirs();
		}
		try{
			
			PrintWriter outFile = new PrintWriter(new FileWriter(path+"/message.txt",true));
			if(send)
				message="#S: "+message;
			else
				message="#R: "+message;
			outFile.println(message);
			outFile.close();	
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	
	public int checkUnread(String ip){
		
		int no=0;
		for(Map.Entry<String, Integer> entry : unread.entrySet()){
			String key=entry.getKey();
			if(key.equals(ip)){
				no=unread.get(key);
				break;
			}
		}
		return no;
	}
	
	
	
	
}