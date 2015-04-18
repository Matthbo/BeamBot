package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

public class Command {

    public Command(){
    }

    public static void ccWithPermission(String str, BeamUser.Role role){
        String[] args = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i =1; i < args.length; i++){
            sb.append(args[i]+ " ");
        }
        String msg = sb.toString();

        if(args[0].equalsIgnoreCase("lvl=mod")){
            if(getModPermitted(role)) BeamBot.send(msg);
        }
        else if(args[0].equalsIgnoreCase("lvl=owner")){
            if(getOwnerPermitted(role)) BeamBot.send(msg);
        }else{
            BeamBot.send(msg);
        }

    }

    public static boolean getModPermitted(BeamUser.Role role) {
        return !(role == BeamUser.Role.BANNED || role == BeamUser.Role.USER || role == BeamUser.Role.PREMIUM || role == BeamUser.Role.SUBSCRIBER);
    }

    public static boolean getOwnerPermitted(BeamUser.Role role){
        return !(role == BeamUser.Role.BANNED || role == BeamUser.Role.USER || role == BeamUser.Role.PREMIUM || role == BeamUser.Role.SUBSCRIBER || role == BeamUser.Role.MOD || role == BeamUser.Role.GLOBAL_MOD);
    }

    public String[] getArguments(String command, String msg){
        String[] all = msg.split(command);
        if(all.length <= 1) return null;
        String[] contends = all[1].split(" ");
        return contends;
    }

}
