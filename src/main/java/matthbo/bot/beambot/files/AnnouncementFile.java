package matthbo.bot.beambot.files;

import matthbo.bot.beambot.BeamBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnnouncementFile {

    private static List<String> announcements = new ArrayList<String>();
    private static Random rand;

    File file = new File("announcements.txt");

    public AnnouncementFile(){
        rand = new Random();
        if(file.exists()) {try{loadFile();}catch (IOException e){
            BeamBot.report(e, "Couldn't load the announcements file!");}}
        else {try{makeFile();}catch (IOException e){BeamBot.report(e, "Couldn't make the announcements file!");}}
    }

    private void loadFile() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while((str = br.readLine()) != null){
            announcements.add(str);
        }
        br.close();
    }

    private void makeFile() throws IOException{
        file.createNewFile();
    }

    public String getRandomAnnouncement(){
        int size = announcements.size();
        if(size > 0) return announcements.get(rand.nextInt(size));
        return null;
    }

}
