package eazi.com.eazi.configure;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import eazi.com.eazi.R;
import eazi.com.eazi.StartChat;
import eazi.com.eazi.adapters.ChatAdapter;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Messages;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

public class MyXMPP {
    public static ArrayList<HashMap<String, String>> usersList=new ArrayList<HashMap<String, String>>();

    public static boolean connected = false;
    public boolean loggedin = false;
    public static boolean isconnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;
    private String serverAddress;
    public static XMPPTCPConnection connection;
    public static String loginUser;
    public static String passwordUser;
    Gson gson;
    MyService context;
    public static MyXMPP instance = null;
    public static boolean instanceCreated = false;

    public MyXMPP(MyService context, String serverAdress, String logiUser,
                  String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        this.passwordUser = passwordser;
        this.context = context;
        init();

    }

    public static MyXMPP getInstance(MyService context, String server,
                                     String user, String pass) {

            instance = new MyXMPP(context, server, user, pass);
            instanceCreated = true;

        return instance;

    }

    public Chat Mychat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text = "";
    String mMessage = "", mReceiver = "";
    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    public void init() {
        gson = new Gson();
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();
    }



    public void initialiseConnection(){
        // Create the configuration for this new connection

        //this function or code given in official documention give an error in openfire run locally to solve this error
        //first off firewall
        //then follow my steps
        //CommonMethods.dialog(context,true);
        new Thread(){
            @Override
            public void run() {



                InetAddress addr = null;
                try {

                    // inter your ip4address now checking it
                    addr = InetAddress.getByName("198.74.57.124");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    System.out.println("ChatConfig connected");

                }
                System.out.println("ChatConfig username  "+loginUser);

                HostnameVerifier verifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return false;
                    }
                };
                DomainBareJid serviceName = null;
                try {
                    serviceName = JidCreate.domainBareFrom("eazi.ai");
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setPort(5222)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .setXmppDomain(serviceName)
                        .setHostnameVerifier(verifier)
                        .setHostAddress(addr)
                        .setDebuggerEnabled(true)
                        .build();
                connection = new XMPPTCPConnection(config);

                try {
                    connection.connect();
                    login();

                    System.out.println("ChatConfig connected");

                    // CommonMethods.dialog(context,false);
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("Online, Programmatically!");
                    presence.setPriority(24);
                    presence.setMode(Presence.Mode.available);
                    Roster roster = Roster.getInstanceFor(connection);
                    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                        // all these proceedure also thrown error if you does not seperate this thread now we seperate thread create

                    if (connection.isAuthenticated() && connection.isConnected()) {
                        //now send message and receive message code here
                        System.out.println("ChatConfig receiver " + "run: auth done and connected successfully");
                       // getAllUser();

                        org.jivesoftware.smack.chat2.ChatManager chatManager = org.jivesoftware.smack.chat2.ChatManager.getInstanceFor(connection);
                        chatManager.addListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, Message message, org.jivesoftware.smack.chat2.Chat chat) {
                                System.out.println("ChatConfig receiver message " + chat.getXmppAddressOfChatPartner().asBareJid());
                                if ( message.getBody() != null) {
                                    processMessage(message,chat.getXmppAddressOfChatPartner().asBareJid().toString());
                                }
                            }
                        });
                    }
                } catch (SmackException e) {
                    e.printStackTrace();
                    System.out.println("ChatConfig SmackException "+e.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ChatConfig IOException "+e.toString());

                } catch (XMPPException e) {
                    e.printStackTrace();
                    System.out.println("ChatConfig XMPPException "+e.toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("ChatConfig InterruptedException "+e.toString());

                }
            }
        } .start();
    }

    public void login() {

        try {
            connection.login(loginUser, passwordUser);
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");
        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);
        }

    }

    public void sendMessage(String messageSend, String receiver) {
        if(!connection.isConnected()) {
           initialiseConnection();
        }
        EntityBareJid jid = null;
        try {
            System.out.println("ChatConfig "+receiver);
            if(!receiver.startsWith("91")) {
                receiver = "91"+receiver;
            }
            jid = JidCreate.entityBareFrom(receiver);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        System.out.println("");

        if(connection != null) {

            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Mychat = chatManager.createChat(jid,mMessageListener);
            Message newMessage = new Message();
            newMessage.setBody(messageSend);
            newMessage.setType(Message.Type.chat);
            try {
                Mychat.sendMessage(newMessage);
                System.out.println("ChatConfig MEsssage sent " );
            } catch (NotConnectedException e) {
                System.out.println("ChatConfig SmackException Exception" + e.toString());

                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("ChatConfig InterruptedException Exception" + e.toString());
                e.printStackTrace();
            }
        }
    }


    private void processMessage(final Message chatMessage, final String receiver) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                DataBaseHelper database = new DataBaseHelper(context);
                database.putMessages(database, CommonMethods.getSharedPrefValue(context, Constants.user_name)
                        ,receiver,chatMessage.getBody(),CommonMethods.getDate(),"false");
                //ChatAdapter chatAdapter = new ChatAdapter(context, receiver);
                if(StartChat.chatAdapter != null) {
                    StartChat.chatAdapter.add(context, receiver);
                    StartChat.chatAdapter.notifyDataSetChanged();
                }

                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(context, StartChat.class);
                intent.putExtra(Constants.userPhoneIntent,receiver);
                System.out.println("processMessage "+chatMessage.getBody() +" receiver "+receiver);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, new Random().nextInt(),
                        intent, 0);
                Notification.Builder builder = new Notification.Builder(context);
                                     builder.setSmallIcon(R.drawable. notification_template_icon_bg)
                                             .setContentTitle("New Message Receiver from "+receiver)
                                             .setContentIntent(pendingIntent);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //builder.setSound(alarmSound);
                Notification notification = builder.getNotification();
                // Hide the notification after its selected
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, notification);
            }
        });
    }

    public void getAllUser(){
        Jid jid = null;
        try {
            jid = JidCreate.from("919964062237@eazi.ai");
            String t1string = jid.toString();
            System.out.println("ChatConfig  getAllUser Jid converted to string:"+ t1string);

        } catch (XmppStringprepException e) {
            System.out.println("ChatConfig  getAllUser Jid converted XmppStringprepException:"+ e.toString());

            e.printStackTrace();
        }


        DomainBareJid DBJid = jid.asDomainBareJid();
        System.out.println("ChatConfig  getAllUser JDomainBareJid:"+ DBJid);

        UserSearchManager search = new UserSearchManager(connection);

        Form searchForm = null;

        try {
            searchForm = search.getSearchForm(DBJid);
            System.out.println("ChatConfig  getAllUser after getSearchForm "+searchForm);

        }catch (SmackException.NoResponseException | SmackException.NotConnectedException | InterruptedException Ie) {
            System.out.println("ChatConfig  getAllUser aException at getSearchForm "+Ie.toString());

            Ie.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            System.out.println("ChatConfig  getAllUser XMPPErrorException "+e.toString());

            e.printStackTrace();
        }

    }

    private class MMessageListener implements ChatMessageListener {

        public MMessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);
            System.out.println("ChatConfig Xmpp message received: '"
                    + message);

            System.out.println("Body-----"+message.getBody());
            System.out.println("ChatConfig Body '"
                    + message.getBody());

        }

        private void processMessage(final Messages chatMessage) {

            StartChat.chatlist.add(chatMessage);
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    StartChat.chatAdapter.notifyDataSetChanged();
                }
            });
        }


    }
}