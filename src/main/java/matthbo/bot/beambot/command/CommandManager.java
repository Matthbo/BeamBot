package matthbo.bot.beambot.command;

import matthbo.bot.beambot.BeamBot;
import pro.beam.api.resource.BeamUser;

import java.io.*;

public class CommandManager{

    static String[] commands = {"test", "bot", "disconnect", "addcom", "delcom", "editcom", "announcements"};

    public static void getCommand(String msg, String name, BeamUser.Role role){
        String[] command = msg.split("!", 2);
        String cmd = command[1];
        File commandFile = new File(BeamBot.getCmdFolder(), cmd + ".txt");

        for(int i = 0; i< commands.length; i++){
            if(cmd.startsWith(commands[i])) command(i, cmd, name, role);
        }
        if(commandFile.exists()) try{customCommand(commandFile, role);}catch(IOException e){BeamBot.report(e, "Couldn't load the command file!");}
    }

    public static void command(int i, String msg,String name, BeamUser.Role role){
        switch (i){
            case 0: new CommandTest(); break;
            case 1: new CommandBot(); break;
            case 2: new CommandDisconnect(name,role); break;
            case 3: new CommandAddcom(msg, role); break;
            case 4: new CommandDelcom(msg, role); break;
            case 5: new CommandEditcom(msg, role); break;
            case 6: new CommandAnnouncements(msg, role); break;
        }
    }

    public static void customCommand(File commandFile, BeamUser.Role role) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(commandFile));
        String str = br.readLine();
        if(str.startsWith("lvl=")){
            Command.ccWithPermission(str, role);
        }else {
            BeamBot.send(str);
        }        br.close();
    }



}
