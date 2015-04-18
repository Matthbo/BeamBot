package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CommandEditcom extends Command{

    public CommandEditcom(String msg, BeamUser.Role role){
        if(getModPermitted(role)){
            String[] args = getArguments(msg);
            String command;
            if(args != null)command = args[1];
            else{
                BeamBot.send("The !editcom command requires: !command arguments"); command = "";}

            StringBuilder str = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                str.append(args[i] + " ");
            }
            String response = str.toString();

            if(!command.startsWith("!")) BeamBot.send("The !editcom command requires: !command arguments");
            else{
                String[] fileName = command.split("!");
                File commandFile = new File(BeamBot.getCmdFolder(), fileName[1] + ".txt");
                if(commandFile.exists()) editFile(response, fileName[1]);
                else BeamBot.send("This command doesn't exists!");
            }
        }
    }

    public void editFile(String response, String name){
        clearCommand(name);
        logToFile(response, name);
    }

    public void clearCommand(String fileName){
        try{
            File dataFolder = BeamBot.getCmdFolder();

            File list = new File(dataFolder, fileName+".txt");
            if(list.exists())list.delete();
        }catch(Exception e){e.printStackTrace();}
    }

    public void logToFile(String msg, String name){
        try{
            File dataFolder = BeamBot.getCmdFolder();
            if(!dataFolder.exists()) dataFolder.mkdir();

            File saveTo = new File(dataFolder, name + ".txt");
            if(!saveTo.exists()) saveTo.createNewFile();
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(msg);
            pw.flush();
            pw.close();
            BeamBot.send("Command !"+name+" has been edited!");
        }catch(IOException e){BeamBot.report(e, "Could not edit the command!"); BeamBot.send("Could not edit the command!");}
    }

    public String[] getArguments(String msg){
        String[] all = msg.split("editcom");
        if(all.length <= 1) return null;
        String[] contends = all[1].split(" ");
        return contends;
    }

}
