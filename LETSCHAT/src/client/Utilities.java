package client;


import javax.swing.JFrame;

public class Utilities {
	
	
	SendReceive sr;
	Thread t;
	Interface inter;
	Storage s;
	public Utilities( ){
		JabberSmackAPI c = new JabberSmackAPI();
		
		s= new Storage();						
		
		sr=new SendReceive(s,inter,c);
		inter=new Interface(s,sr,c);
		inter.frame.setEnabled(false);
		sr.inter=this.inter;
		
	//	System.out.println("hey");
		
		try {						/*Connected to server*/
				sr.checkConnection();
		} catch (InterruptedException e) {e.printStackTrace();}
		inter.frame.setEnabled(true);
		new Send(sr);
		new Receive(sr);
		System.out.println("Ready to go");
	}
	
	

}