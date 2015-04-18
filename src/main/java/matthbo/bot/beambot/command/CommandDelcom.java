package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

import java.io.File;

public class CommandDelcom extends Command{

    public CommandDelcom(String msg, BeamUser.Role role){
        if(getModPermitted(role)) {
            String[] args = getArguments(msg);
            String command = "";
            if (args != null) command = args[1];
            //else{BeamBot.send("The !delcom command requires: !command"); command = "";}

            if (!command.startsWith("!")) BeamBot.send("The !delcom command requires: !command");
            else {
                String[] fileName = command.split("!");
                File commandFile = new File(BeamBot.getCmdFolder(), fileName[1] + ".txt");
                if (commandFile.exists()) clearCommand(fileName[1]);
                else BeamBot.send("This command doesn't exists!");
            }
        }
    }

    public void clearCommand(String fileName){
        try{
            File dataFolder = BeamBot.getCmdFolder();

            File list = new File(dataFolder, fileName+".txt");
            if(list.exists())list.delete();
            BeamBot.send("Deleted command !"+fileName);
        }catch(Exception e){e.printStackTrace();}
    }

    public String[] getArguments(String msg){
        String[] all = msg.split("delcom");
        if(all.length <= 1) return null;
        String[] contends = all[1].split(" ");
        return contends;
    }
}
