package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import matthbo.bot.beambot.chat.Announcements;
import pro.beam.api.resource.BeamUser;

public class CommandAnnouncements extends Command{

    public CommandAnnouncements(String msg, BeamUser.Role role){
        if(getModPermitted(role)){
            String[] args = getArguments("announcements",msg);
            String config = args[1];
            boolean oldConfig = BeamBot.getConfig().announcements;
            if(config.equalsIgnoreCase("on")){
                if(oldConfig)BeamBot.send("Announcements are already on!");
                if(!oldConfig) turnOn();
            }
            if(config.equalsIgnoreCase("off")){
                if(oldConfig) turnOff();
                if(!oldConfig)BeamBot.send("Announcements are already off!");
            }
        }
    }

    private void turnOn(){
        BeamBot.getConfig().announcements = true;
        BeamBot.announcements = new Announcements(BeamBot.getConfig().announceDelay);
        BeamBot.send("Announcements are now turned on!");
    }
    private void turnOff(){
        BeamBot.getConfig().announcements = false;
        BeamBot.announcements.stop();
        BeamBot.send("Announcements are now turned off!");
    }

}
