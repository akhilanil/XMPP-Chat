package client;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.swing.*;
import javax.swing.text.*;

import org.jivesoftware.smack.XMPPException;



public class ChatBox {
	JFrame frame;
	JPanel toppanel,message,text,area,whole;
	JTextArea type;//chats
	JTextPane chats;
	JSplitPane topdivider;
	JButton send;
	String name;
	InetAddress ip;
	int port;
	boolean receive,state;
	SendReceive sr;
	StyleContext sc;
	Storage s;
	String[] toread;
	JScrollBar vertical;
	JScrollPane jschats;
	JabberSmackAPI c;
	@SuppressWarnings("serial")
	private class Details extends JLabel{
		
		Details(String s){
			
			super(s);
			this.setForeground(Color.WHITE);
			this.setFont(new Font("Times New Roman",Font.ITALIC,20));
		}
		
	}
	
	
@SuppressWarnings("serial")
private class Values extends JLabel{
		
		Values(String s){
			
			super(s);
			this.setForeground(Color.RED);
			this.setFont(new Font("Serif",Font.ITALIC,20));
		}
	}
	

	

	ChatBox(String name, int unread, SendReceive sr, JabberSmackAPI c,Storage s,Interface i){
		
		this.name=name;
		this.sr=sr;
		this.c=c;
		this.s=s;
		frame= new JFrame(this.name.toUpperCase());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(550,500);
		frame.setLocationRelativeTo(null);
		
		
		
		text=new JPanel();
		area=new JPanel();
		whole= new JPanel();
		
		
		
		
		createTop();
		createMessage();
		
		topdivider= new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		topdivider.setLeftComponent(toppanel);
		topdivider.setRightComponent(message);
		topdivider.setDividerLocation(100);
		
		
		whole.add(topdivider);
		frame.add(topdivider);
		frame.validate();
		
		if(unread>0){
			this.toread=s.extractFile(this.name);
			try {extract(this.toread);} catch (XMPPException e) {e.printStackTrace();}
		}
		
		
		frame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				i.isAlive="";
			}
			
		});
		
		
		
	}
	
	public void extract(String [] msgs)throws XMPPException{//displays unread messages if any
		
		for(int i=0; i<msgs.length; i++){
			display(false,false,msgs[i]);
		}
		
	}
	
	public void createTop(){
		
		toppanel= new JPanel();
		toppanel.setBackground(Color.GREEN);
		toppanel.setLayout(new FlowLayout());
		
		JLabel name= new Details("Name: ");
		JLabel name1=new Values(this.name);
		
		toppanel.add(name);
		toppanel.add(name1);
		
	}
	
	public void createMessage(){
		
		message=new JPanel();
		message.setLayout(new GridBagLayout());
		
		
		sc = new StyleContext();
		
		chats=new JTextPane();
		chats.setEditable(false);
		
		type=new JTextArea(3,42);
		type.setWrapStyleWord(true);
		type.setLineWrap(true);
		
		send= new JButton("SEND");
		
		jschats= new JScrollPane(chats);
		jschats.setViewportView(chats);
		jschats.setPreferredSize(new Dimension(525,295));
		JScrollPane jstype= new JScrollPane(type,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jschats.getVerticalScrollBar().setValue(jschats.getVerticalScrollBar().getMaximum());

		
		
		
		GridBagConstraints gbc= new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx = 0.2;
	    gbc.weighty = 0.8;
	    gbc.anchor = GridBagConstraints.SOUTHWEST;
	    gbc.gridwidth=3;
	    gbc.gridheight=2;
	   gbc.anchor=GridBagConstraints.CENTER;
	    message.add(jschats,gbc);

	    gbc.gridwidth=2;
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.weightx = 0.8;
	    gbc.weighty = 0.2;
	    gbc.gridheight=1;
	    gbc.anchor = GridBagConstraints.SOUTHWEST;
		message.add(jstype,gbc);
		
		gbc.gridx=1;
		gbc.gridy=2;
		gbc.weightx = 0.2;
	    gbc.weighty = 0.2;
	    gbc.gridwidth=1;
	    gbc.gridheight=1;
	    gbc.ipady=20;
	    gbc.anchor = GridBagConstraints.LAST_LINE_END;
	    message.add(send,gbc);
	    
	    
	    
	    
	    send.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(type.getText().trim().length()!=0){
					s.storeMessage(name,type.getText(),true);
					try {display(true,true,type.getText());} catch (XMPPException e1) {e1.printStackTrace();}
					state=true;
				}	
				
			}
	    	
	    });
		
	}
	
	
	public void display(boolean state, boolean send,String msg) throws XMPPException{

		
		
		int i=40,k=0,length,j=1;
		
		
		
		StyledDocument doc=chats.getStyledDocument();
		
		StringBuilder str=new StringBuilder();
		str.append(msg);

		SimpleAttributeSet keyWord1 = new SimpleAttributeSet();
		SimpleAttributeSet keyWord2 = new SimpleAttributeSet();
		StyleConstants.setBold(keyWord1, true);
		StyleConstants.setFontSize(keyWord1, 13);
		StyleConstants.setFontSize(keyWord2, 13);
		
		if(msg.length()>40){
			length=msg.length()/40;
			while(j<=length){
				i=40*j;
				if(i>=msg.length()){break;}
				while(msg.charAt(i)!=' ' && i<=msg.length() && i<4){
					i++;
				}
				str.insert(i, "\n");
				j++;
			}
		}
			
		if(!send){
		
			try {
				doc.insertString(doc.getLength(), "\n"+this.name+" Says: ", keyWord1);
				StyleConstants.setForeground(keyWord2, Color.BLUE);
				doc.insertString(doc.getLength(), "\n"+str.toString(), null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
		}
		else{
			sr.sendMessage(type.getText(),name);
			try {
					doc.insertString(doc.getLength(),"\nME:" , keyWord1);
					
					doc.insertString(doc.getLength(), "\n"+str.toString(), keyWord2);
			} catch (BadLocationException e) {
					e.printStackTrace();
			}
			
			type.setText("");
		}
		vertical = jschats.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum()*10);
	}	
	
	 
	

}
