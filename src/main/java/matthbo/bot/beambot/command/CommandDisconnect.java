package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

public class CommandDisconnect extends Command{

    public CommandDisconnect(String owner, BeamUser.Role role){
        if(getOwnerPermitted(role)){
            BeamBot.disconnect();
        }
    }

}
