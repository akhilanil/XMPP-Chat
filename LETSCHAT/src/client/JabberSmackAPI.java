package client;
import java.util.*;
import java.io.*;
 
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
 
public class JabberSmackAPI implements MessageListener{
 
    XMPPConnection connection;
    String received=null;
    public void login(String userName, String password) throws XMPPException
    {
    ConnectionConfiguration config = new ConnectionConfiguration("xmpp.jp",5222,"xmpp.jp");
    connection = new XMPPConnection(config);
 
    connection.connect();
    System.out.println("Username: "+userName+"\tPassword: "+password);
    connection.login(userName, password);
    Roster roster = connection.getRoster();
    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    
    Presence p = new Presence(Presence.Type.available, "Always available", 42, Mode.available);
    connection.sendPacket(p);
    }
 
    public void sendMessage(String message, String to) throws XMPPException{
    	
    	Chat chat = connection.getChatManager().createChat(to, this);
    	chat.sendMessage(message);
    	
    }
    public String receiveMessage(String toSend)throws XMPPException{
    	
    	received=null;
    	ChatManager chatmanager=connection.getChatManager();
    	Chat chat=chatmanager.createChat(toSend, new MessageListener(){

		public void processMessage(Chat chat, Message message) {
				received=message.getBody();
			}});
    	if(received!=null && !received.equals("")){
			System.out.println("Received: "+received);
    	}
    	return received;
   
    	
    }
    public Collection<RosterEntry> getBuddyList(){
    	Roster roster = connection.getRoster();
    	Collection<RosterEntry> entries = roster.getEntries();
    	//Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
    	return entries;
    	
    	
    	
    	
    	/*Presence presence = new Presence(Presence.Type.subscribe);
        presence.setTo(jid);
        connection.sendPacket(presence);*/
    }
 
    public void disconnect()
    {
    	connection.disconnect();
    }
 
    public void processMessage(Chat chat, Message message)
    {
    if(message.getType() == Message.Type.chat)
    System.out.println(chat.getParticipant() + " says: " + message.getBody());
    }

	
   /* public static void main(String args[]) throws XMPPException, IOException
    {
    // declare variables
    JabberSmackAPI c = new JabberSmackAPI();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String msg;
 
 
    // turn on the enhanced debugger
    //XMPPConnection.DEBUG_ENABLED = true;
 
 
    // Enter your login information here
    c.login("akhilanil95@xmpp.jp", "lostandfounD");
 
    c.displayBuddyList();
 
    System.out.println("-----");
 
    System.out.println("Who do you want to talk to? - Type contacts full email address:");
    String talkTo = br.readLine();
 
    System.out.println("-----");
    System.out.println("All messages will be sent to " + talkTo);
    System.out.println("Enter your message in the console:");
    System.out.println("-----\n");
 
    while( !(msg=br.readLine()).equals("bye"))
    {
    	System.out.println("to send");
        c.sendMessage(msg, talkTo);
    	System.out.println("to receive");
    	c.receiveMessage(talkTo);
    	
        
        
    }
 
    c.disconnect();
    System.exit(0);
    }*/
 
}


