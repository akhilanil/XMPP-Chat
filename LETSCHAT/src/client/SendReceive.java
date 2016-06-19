package client;

import java.awt.*;
import org.jivesoftware.smack.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public  class SendReceive {

	InetAddress serverip;
	int serverport,myport;
	DatagramSocket cs=null;
	byte[] sbyte,rbyte;
	boolean send=false;
	boolean connected=false;
	JProgressBar progressBar;
	JFrame frame;
	JPanel panel;
	JLabel name,password,port;
	JTextField name1,password1;
	JButton enter,quit;
	String myname;
	Interface inter;
	String smsg,destip,destport,destmsg;
	Storage s;
	JabberSmackAPI c;
	Collection<RosterEntry> entries;
	
	SendReceive(Storage s,Interface inter,JabberSmackAPI c){
		this.inter=inter;
		this.s=s;
		this.c =c;
		sbyte= new byte[1024];
		rbyte= new byte[1024];
	}
	public void serverConnect(String login, String password)throws XMPPException {// Initial Connection
		
		c.login(login, password);
		entries=c.getBuddyList();
		for(RosterEntry r:entries){
			inter.addOnline(r.getUser());
    	}
		
		
	}
	
	public void sendMessage(String send,String to) throws XMPPException{
		this.send=true;
		smsg=send;
		destmsg=to;
		
	}
	
	synchronized public void sendMessage()throws XMPPException{
		while(!this.send){
			try{
				wait();
			}catch(Exception e){e.printStackTrace();}
		}
		c.sendMessage(smsg, destmsg);
		this.send=false;
		notify();
	}
	
	synchronized public void receiveMessage() throws XMPPException{
		while(this.send){
			try{
				notify();
				wait();
			}catch(Exception e){e.printStackTrace();}
		}
		while(!this.send){
			for(RosterEntry r:entries){
				String msg=c.receiveMessage(r.getUser());
				
				if(msg!=null && !msg.equals("")){
					inter.getMessage(msg, r.getUser());
				}
    		}
    	}
		notify();
		
		
	}
	
	public void alterState(){
		
		if(send){send=false;}
		else{send=true;}
	}
	
	
	
	public void  createAuthentication(){
		
		Progress p=new Progress();
		frame= new JFrame("Authenticate");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350,200);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		
		panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc= new GridBagConstraints();
		
		name=new JLabel("User Name: ");
		password=new JLabel("Password: ");
		
		name1= new JTextField(20);
		password1=new JTextField(15);
		
		enter= new JButton("CONNECT");
		quit= new JButton("QUIT");
		
		gbc.insets=new Insets(10,10,0,0);
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		panel.add(name,gbc);
		
		gbc.gridx=1;
		gbc.gridy=0;
		gbc.gridwidth=2;
		gbc.gridheight=1;
		panel.add(name1,gbc);

		gbc.gridx=0;
		gbc.gridy=1;
		gbc.weightx=0.0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		panel.add(password,gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.weightx=0.0;
		//gbc.weighty=1.0;
		gbc.gridwidth=2;
		gbc.gridheight=1;
		panel.add(password1,gbc);
		
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.weightx=0.0;
		//gbc.weighty=1.0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.anchor=GridBagConstraints.CENTER;
		panel.add(enter,gbc);
		
		gbc.gridx=1;
		gbc.gridy=3;
		gbc.weightx=0.0;
		//gbc.weighty=1.0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.anchor=GridBagConstraints.CENTER;
		panel.add(quit,gbc);
		
		gbc.gridx=1;
		gbc.gridy=4;
		gbc.weightx=0.0;
		//gbc.weighty=1.0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.anchor=GridBagConstraints.CENTER;
		panel.add(progressBar,gbc);
		
		frame.add(panel);
		frame.validate();
		

		enter.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				myname=name1.getText();
				inter.frame.setTitle("LetsChat - "+myname);
				p.start();
			}
			
		});
		
		quit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(cs!=null)cs.close();
				frame.dispose();
				System.exit(0);
			}
			
		});
		
		 frame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	        	 if(cs!=null)cs.close();
	        	 frame.dispose();
	        	 System.exit(0);
	            
	         }        
	      }); 
		
		
	}
	
	private class Progress extends Thread {
		public Progress(){
			  progressBar = new JProgressBar(0, 100);
		      progressBar.setValue(0);
		      progressBar.setStringPainted(true);
		}
		public void run(){
	         for(int i =0; i<= 100; i+=50){
	            final int progress = i;
	            if(progress==50){	
	            	progressBar.setValue(progress);
	            	try {serverConnect(name1.getText(),password1.getText());} catch (XMPPException e1) {e1.printStackTrace();}
	            	try{Thread.sleep(3000);}catch(InterruptedException e){}
	            }
	            progressBar.setValue(progress);
	         }
	         
	         connected=true;
	         try{Thread.sleep(1000);}catch(InterruptedException e){}
	         frame.dispose();
	         try{Thread.sleep(200);}catch(InterruptedException e){}
	         
	         try{checkConnection();}catch(Exception e){}
	         //notifyAll();
	         
	      }
	
	}
	
	
	public String getName(){return myname;}
	public synchronized void checkConnection() throws InterruptedException{
		while(!connected){
			createAuthentication();
			wait();
			
		}
		
		notify();
	}
	
	
	public String getMessage(){return destmsg;}
	
	
}