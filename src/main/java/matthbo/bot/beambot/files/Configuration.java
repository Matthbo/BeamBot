package matthbo.bot.beambot.files;

import matthbo.bot.beambot.BeamBot;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private Properties prop = new Properties();

    public String username;
    public String password;
    public String channel;
    public boolean debug;
    public boolean announcements;
    public int announceDelay;

    File configFile = new File("beambot.cfg");

    public Configuration(){
        if(configFile.exists()) {try{loadConfig();}catch (IOException e){
            BeamBot.report(e, "Couldn't load the config file!");}}
        else {try{makeConfig();}catch (IOException e){BeamBot.report(e, "Couldn't make the config file!");}}
    }

    private void loadConfig() throws IOException{
        InputStream input = new FileInputStream(configFile);
        prop.load(input);

        username = prop.getProperty("username");
        password = prop.getProperty("password");
        channel = prop.getProperty("channel");
        debug = Boolean.valueOf(prop.getProperty("debug"));
        announcements = Boolean.valueOf(prop.getProperty("announcements"));
        announceDelay = Integer.valueOf(prop.getProperty("announceDelay"));

        input.close();
    }

    private void makeConfig() throws IOException{
        OutputStream output = new FileOutputStream(configFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

        writer.write("# use 'channel' if you want your bot in another channel\n");

        writer.write("username=yourUsername\n");
        writer.write("password=yourPassword\n");
        writer.write("channel=\n");
        writer.write("debug=false\n");
        writer.write("announcements=true\n");
        writer.write("announceDelay=20\n");

        writer.flush();
        output.close();

        BeamBot.report(null, "Please edit the generated config file.");
    }

}
