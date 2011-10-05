package graphics;

//import bluetooth.Server;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * Hlavni trida hry
 */
public class GameMIDlet extends MIDlet implements CommandListener {

    private GameCanvas canvas;
    private Displayable menu;
    private Displayable help;
    private boolean inited = false;
    private static GameMIDlet instance;
    public static final Command backCmd = new Command("Back", Command.BACK, 1);
    private final String helpText = "Game controls\n" +
            "=============\n" +
            "   View mode\n" +
            "   ---------\n" +
            "     Joystick - changes scene view\n" +
            "     Keys 1,3 - zoom in/out\n" +
            "     Keys 2,4,6,8 - change scene view\n" +
            "     Key 7 - change game mode\n" +
            "     Key 0 - change game mode\n\n" +
            "   Play mode\n" +
            "   ---------\n" +
            "     Joystick - moves the stone\n" +
            "     Keys 2,4,6,8 - move the stone\n" +
            "     Key 5 or fire - drops the stone\n" +
            "     Key 7 - change game mode\n" +
            "     Key 0 - change game mode";

    public GameMIDlet() {
        super();
        instance = this;
    }

    /**
     * @return vrati instanci tridy GameMIDlet
     */
    public static GameMIDlet getInstance() {
        return instance;
    }

    protected void startApp() throws MIDletStateChangeException {
        if (canvas == null) {
            // spusteni aplikace
            SplashScreen splashScreen = new SplashScreen();
            Display.getDisplay(this).setCurrent(splashScreen);
            Thread t = new Thread(splashScreen);
            t.start();

            menu = new MenuCanvas();
            canvas = new GameCanvas(this, menu);


            initHelp();
            inited = true;
        } else {
            // Aktivace aplikace po jejim pobytu v pasivnim stavu
        }
    }

    /**
     * @return true, je-li instance inicializovana, jinak false
     */
    public boolean isInited() {
        return inited;
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) {
        notifyDestroyed();
    }

    /**
     * 
     * @return Instance of game canvas
     */
    public GameCanvas getCanvas() {
        return canvas;
    }

    public Displayable getMenu() {
        return menu;
    }

    /**
     * Zobrazi vlastni hru
     *
     */
    public void playAction() {
        if (canvas.isOver()) {
            canvas = new GameCanvas(this, menu);
        }
        Display.getDisplay(this).setCurrent(canvas);
    }

    /**
     * Zobrazi menu
     *
     */
    public void menuAction() {
        Display.getDisplay(this).setCurrent(menu);
    }

    /**
     * Zobrazi splash
     *
     */
    public void splashAction(SplashScreen s) {
        Display.getDisplay(this).setCurrent(s);
    }

    /**
     * Zobrazi napovedu
     *
     */
    public void helpAction() {
        Display.getDisplay(this).setCurrent(help);
    }

    /**
     * Inicializuje napovedu
     *
     */
    protected void initHelp() {
        Form help = new Form("Help");
        help.append(helpText);
        help.addCommand(backCmd);
        help.setCommandListener(this);
        this.help = help;
    }

    public void commandAction(Command c, Displayable d) {
        if (d == help && c == backCmd) {
            menuAction();
        } else if (d == canvas && c == backCmd) {
            menuAction();
        }
    }
}
