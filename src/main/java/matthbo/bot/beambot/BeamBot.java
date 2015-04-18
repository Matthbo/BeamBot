package matthbo.bot.beambot;

import matthbo.bot.beambot.chat.Announcements;
import matthbo.bot.beambot.command.CommandManager;
import matthbo.bot.beambot.files.AnnouncementFile;
import matthbo.bot.beambot.files.Configuration;
import org.apache.commons.logging.impl.SimpleLog;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.BeamChatConnectable;
import pro.beam.api.resource.chat.events.EventHandler;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.response.users.UserSearchResponse;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class BeamBot {
    static BeamAPI beam;

    static int channelID;// = 9430;
    static BeamChannel bChannel;
    static BeamUser bUser;
    static BeamChat bChat;
    static String authKey;
    static BeamChatConnectable chat;

    static BeamChat oChat;
    static BeamUser oUser;

    public static Announcements announcements;
    static AnnouncementFile announcementFile;

    private static Configuration config;

    private static String pass;
    private static String name;
    static boolean DEBUG = false;

    private static final String version = "0.3";

    private static final File cmdFolder = new File("commands/");

    static SimpleLog logger;

    public static void main(String[] args) {
        beam = new BeamAPI();
        logger = new SimpleLog("BeamBot");
        config = new Configuration();
        announcementFile = new AnnouncementFile();
        String newChannel;

        name = config.username;
        pass = config.password;
        newChannel = config.channel;
        if(config.debug)DEBUG = true;
        if(config.announcements)announcements = new Announcements(config.announceDelay);

        preInit();

        if(!newChannel.equalsIgnoreCase("")) joinChat(newChannel);

        try{
            loadChat();
        }catch(Exception e){
            report(e, "Couldn't load the chat!");
        }

    }

    public static void loadChat() throws InterruptedException, ExecutionException {
        chat = bChat.makeConnectable(beam);
        boolean connected = chat.connectBlocking();

        if(connected){
            chat.send(AuthenticateMessage.from(bChannel, bUser, authKey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    logger.info("Authenticated!");
                }public void onFailure(Throwable var1){
                    report(var1, "Failed to Authenticate!");
                }
            });
        }
        chat.on(IncomingMessageEvent.class, new EventHandler<IncomingMessageEvent>() {
            public void onEvent(IncomingMessageEvent event) {
                String msg = event.data.getMessage();
                String name = event.data.user_name;
                BeamUser.Role role = event.data.user_roles.get(0);
                logger.info("["+getTime() + "] MSG["+ getChannelName()+"]>> " + name + "[" + role + "]: " + msg);
                if (msg.startsWith("!")) CommandManager.getCommand(msg, name, role);
            }
        });
        send("I'm Working!");
        send("Make me a moderator so that I can fully function!");
    }

    public static void joinChat(String user){
        try {
            UserSearchResponse search = beam.use(UsersService.class).search(user).get();
            oUser = beam.use(UsersService.class).findOne(search.get(0).id).get();
            logger.info("Trying to find " + oUser.username + "'s chat");
            oChat = beam.use(ChatService.class).findOne(oUser.channel.id).get();
            bChat = oChat;
            //bUser = oUser;
            bChannel = oUser.channel;
            authKey = oChat.authkey;

        } catch (Exception e) {
            report(e, "Given chat not found");
        }
    }

    public static void disconnect(){
        announcements.stop();
        send("Shutting Down...");
        chat.close();
        logger.info("Shutting Down...");
        System.exit(1);
    }

    public static void preInit(){
        logger.info("Welcome to BeamBot v." + version + " made by Matthbo.\n");
        try {
            if(name == null || pass == null) {logger.warn("Please add a username and/or password!"); System.exit(1);}
            else bUser = beam.use(UsersService.class).login(name.toLowerCase(), pass.toLowerCase()).get();
        } catch(Exception e){
            report(e, "Failed to Log In!");
        }
        bChannel = bUser.channel;
        channelID = bChannel.id;
        try{
            bChat = beam.use(ChatService.class).findOne(channelID).get();
        }catch(Exception e){
            report(e, "Failed to find the chat!");
        }
        authKey = bChat.authkey;
    }

    public static void send(String msg){
        chat.send(ChatSendMethod.of(msg));
    }

    public static void report(Exception e, String msg){
        if(DEBUG)e.printStackTrace();
        logger.error(msg + "\n >> Enable 'debug' for more info");
        System.exit(1);
    }
    public static void report(Throwable t, String msg){
        if(DEBUG)t.printStackTrace();
        logger.error(msg + "\n >> Use +DEBUG for more info");
        System.exit(1);
    }

    public static String getName(){
        return name;
    }

    public static String getChannelName(){
        if(oUser != null) return oUser.username;
        return bUser.username;
    }

    public static String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static File getCmdFolder(){
        return cmdFolder;
    }

    public static Configuration getConfig(){
        return config;
    }

    public static AnnouncementFile getAFile(){
        return announcementFile;
    }

}
