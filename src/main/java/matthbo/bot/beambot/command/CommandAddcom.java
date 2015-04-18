package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CommandAddcom extends Command{

    public CommandAddcom(String msg, BeamUser.Role role){
        if(getModPermitted(role)) {
            String[] args = getArguments("addcom",msg);
            String command;
            if (args != null) command = args[1];
            else {
                BeamBot.send("The !addcom command requires: !command arguments");
                command = "";
            }

            StringBuilder str = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                str.append(args[i] + " ");
            }
            String response = str.toString();

            if (!command.startsWith("!") || response.equalsIgnoreCase(""))
                BeamBot.send("The !addcom command requires: !command arguments");
            else {
                String[] fileName = command.split("!");
                File commandFile = new File(BeamBot.getCmdFolder(), fileName[1] + ".txt");
                if (!commandFile.exists()) logToFile(response, fileName[1]);
                else BeamBot.send("This command already exists!");
            }
        }
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
            BeamBot.send("Command !"+name+" has been created!");
        }catch(IOException e){BeamBot.report(e, "Could not make the command!"); BeamBot.send("Could not make the command!");}
    }
}
