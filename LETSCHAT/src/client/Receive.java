package client;

import org.jivesoftware.smack.XMPPException;

public class Receive implements Runnable{

	SendReceive sr;
	Thread r;
	Receive(SendReceive sr){
		this.sr=sr;
		r= new Thread(this,"Start");
		r.start();
	}
	
	public void run(){
		while(true){
			try {sr.receiveMessage();} catch (XMPPException e) {e.printStackTrace();}
		}
		
	}

}