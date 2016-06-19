package client;

import org.jivesoftware.smack.XMPPException;

public class Send implements Runnable {
	
	SendReceive sr;
	Thread s;
	Send(SendReceive sr){
		this.sr=sr;
		s= new Thread(this,"Start");
		s.start();
	}
	@Override
	public void run() {
		while(true){
			try {sr.sendMessage();} catch (XMPPException e) {e.printStackTrace();}
		}
		
	}
	
		

}
