package matthbo.bot.beambot.chat;

import matthbo.bot.beambot.BeamBot;
import matthbo.bot.beambot.files.AnnouncementFile;

public class Announcements implements Runnable {

    private boolean running = false;
    private int delay = 0;
    private Thread thread;

    private AnnouncementFile file = BeamBot.getAFile();

    public Announcements(int delay){
        thread = new Thread(this);

        this.delay = delay;

        thread.start();
        running = true;
    }

    public void run() {
        while(running){
            try
            {
                synchronized(this)
                {
                    this.wait(delay*60000);
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            makeAnnouncement();
        }
    }

    private void makeAnnouncement(){
        String msg = file.getRandomAnnouncement();
        if(msg != null) BeamBot.send(msg);
    }

    public void stop(){
        running = false;
        thread.stop();//interrupt();
    }

}
