package graphics;

import javax.microedition.media.Player;
import javax.microedition.media.Manager;
import javax.microedition.media.control.VolumeControl;
import java.io.InputStream;

public class Sound implements Runnable {

    private InputStream is;
    private int volume;

    public Sound(int id, int vol) {
        String filename = new String();

        switch (id) {
            case 0:
                filename = "/menu.wav";
                break;
            case 1:
                filename = "/play.wav";
                break;
            case 2:
                filename = "/changemode.wav";
                break;
            case 3:
                filename = "/gameover.wav";
                break;
        }

        is = getClass().getResourceAsStream(filename);
        this.volume = vol;
    }

    public void playSound() {
        try {
            Player player = Manager.createPlayer(is, "audio/x-wav");

            player.realize();

            // volume
            VolumeControl vc = (VolumeControl) player.getControl("VolumeControl");
            if (vc != null) {
                vc.setLevel(volume);
            }

            player.prefetch();
            player.start();
        } catch (Exception e) {
            System.out.println("Error! Cannot play sound: " + e.getMessage());
        }
    }

    public void run() {
        playSound();
    }
}
