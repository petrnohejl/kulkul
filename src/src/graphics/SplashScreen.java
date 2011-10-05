package graphics;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import logic.Constants;

/**
 * Uvodni obrazovka
 */
public class SplashScreen extends Canvas implements Runnable {
    // uvodni obrazek

    private Image splash;
    // je-li true, stiskl uzivatel klavesu
    private boolean pressed = false;
    private boolean end = false;

    public SplashScreen() {
        try {
            splash = Image.createImage("/splash.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SplashScreen(int id) {
        String filename;
        end = true;
        switch (id) {
            case Constants.BLACK:
                filename = "/wins1.gif";
                break;
            case Constants.WHITE:
                filename = "/wins2.gif";
                break;
            default:
                filename = "/pat.gif";
                break;
        }

        try {
            splash = Image.createImage(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Tato metoda pocka 5 vterin a pak spusti hru
     * nebo ji spusti ihned po stisku libovolne klavesy
     */
    public void run() {
        try {
            // vlakno uspime pouze, nahral-li se obrazek v poradku
            if (splash != null) {
                int i = 50;
                while (i > 0) {
                    // uspani aktualniho vlakna na 0,1 vteriny
                    Thread.sleep(100);
                    if (pressed) {
                        // preruseni cyklu, byla-li stisknuta klavesa
                        break;
                    }
                    i--;
                }
                while (!GameMIDlet.getInstance().isInited()) {
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // zruseni odkazu na obrazek
        splash = null;
        if (!end) {
            GameMIDlet.getInstance().menuAction();
        }
        else {
            GameMIDlet.getInstance().playAction();
        }
    }

    /*
     * Vykresleni obrazku
     */
    protected void paint(Graphics g) {
        if (splash != null) {
            // vykresleni obrazku a vycentrovani na stred
            g.drawImage(splash, getWidth() / 2, getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
        }
    }

    /*
     * Tuto metodu zavola aplikacni manazer,
     * nastane-li udalost stisk klavesy
     */
    protected void keyPressed(int key) {
        pressed = true;
    }
}
