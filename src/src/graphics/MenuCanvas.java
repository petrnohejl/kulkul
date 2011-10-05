package graphics;

import javax.microedition.lcdui.*;

/**
 * Menu
 */
public class MenuCanvas extends Canvas {

    private Image background;				// obrazek pozadi
    private int bgX;						// X umisteni obrazku s pozadim
    private int bgY;						// Y umisteni obrazku s pozadim
    private Image menu;						// obrazek s polozkami menu
    private int menuY;						// X umisteni obrazku s polozkami menu
    private int menuX;						// Y umisteni obrazku s polozkami menu
    private boolean sound = true;
    private Sound snd;
    private boolean gameOn = false;
    private final int selectedColor = 0x99cc44;	// barva zvyraznovaciho pruhu
    private final int itemHeight = 24;			// vyska zvyraznovaciho pruhu
    private final int MENU_ITEMS = 5;				// pocet polozek menu
    private final int MENU2_ITEMS = 4;				// pocet polozek menu
    //menu items
    private final int MENU_CONTINUE = 0;
    private final int MENU_NEW_GAME = 1;
    private final int MENU_SOUND = 2;
    private final int MENU_HELP = 3;
    private final int MENU_EXIT = 4;

    /**
     * Menu2 items
     */
    private int selected;				// index vybrane polozky

    public MenuCanvas() {
        setFullScreenMode(true);
        //System.out.println("Width Of Display Screen: " + getWidth());
        //System.out.println("Height Of Display Screen: " + getHeight());

        setMenu();
        /* nastaveni umisteni leveho horniho rohu
         * pozadi a obrazku s polozkami menu tak,
         * aby tyto obrazky byly na displeji vycentrovany
         */
        int height = getHeight();
        int width = getWidth();

        menuX = (width - menu.getWidth()) / 2;
        menuY = (height - menu.getHeight()) / 2;
        bgX = (width - background.getWidth()) / 2;
        if (height >= background.getHeight()) {
            bgY = (getHeight() - background.getHeight()) / 2;
        } else {
            bgY = -background.getHeight() / 15;
        }
        snd = new Sound(0,100);
    }

    /* vykresleni menu */
    protected void paint(Graphics g) {
        // prekresleni celeho displeje bilou barvou
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());

        // nakresleni pozadi
        g.drawImage(background, bgX, bgY, Graphics.TOP | Graphics.LEFT);

        // nakresleni zvyraznovaciho pruhu
        g.setColor(selectedColor);
        g.fillRect(0, menuY + selected * itemHeight, getWidth(), itemHeight);

        // nakresleni polozek menu
        g.drawImage(menu, menuX, menuY, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int key) {
        int action = getGameAction(key);
        if (gameOn) {
            pushMenu(action);
        } else {
            pushMenu2(action);
        }
    }

    private void pushMenu(int action) {
        switch (action) {
            case Canvas.UP: // pohyb kurzorem nahoru
                selected = (--selected) % MENU_ITEMS;
                if (selected == -1) {
                    selected = MENU_ITEMS - 1;
                }
                repaint();
                if (sound) {
                    Thread sndThread = new Thread(snd);
                    sndThread.run();
                }
                break;

            case Canvas.DOWN: // pohyb kurzorem dolu
                selected = (++selected) % MENU_ITEMS;
                repaint();
                if (sound) {
                    Thread sndThread = new Thread(snd);
                    sndThread.run();

                }
                break;

            case Canvas.FIRE: // vybrani polozky
                GameMIDlet midlet = GameMIDlet.getInstance();
                switch (selected) {
                    case MENU_CONTINUE:
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.playAction();
                        break;
                    case MENU_NEW_GAME:
                        midlet.getCanvas().endGame();
                        midlet.playAction();
                        setMenu2();
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        break;
                    case MENU_SOUND:
                        sound = !sound;
                        //System.out.println("Sound: " + sound);
                        try {
                            if (sound) {
                                menu = Image.createImage("/menu3.png");
                            } else {
                                menu = Image.createImage("/menu4.png");
                            }
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        repaint();
                        break;
                    case MENU_HELP:
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.helpAction();
                        break;
                    case MENU_EXIT:
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.destroyApp(true);
                        break;
                }
        }
    }

    private void pushMenu2(int action) {
        switch (action) {
            case Canvas.UP: // pohyb kurzorem nahoru
                selected = (--selected) % MENU2_ITEMS;
                if (selected == -1) {
                    selected = MENU2_ITEMS - 1;
                }
                repaint();
                if (sound) {
                    Thread sndThread = new Thread(snd);
                    sndThread.run();

                }
                break;

            case Canvas.DOWN: // pohyb kurzorem dolu
                selected = (++selected) % MENU2_ITEMS;
                repaint();
                if (sound) {
                    Thread sndThread = new Thread(snd);
                    sndThread.run();

                }
                break;

            case Canvas.FIRE: // vybrani polozky
                GameMIDlet midlet = GameMIDlet.getInstance();
                switch (selected + 1) {
                    case MENU_NEW_GAME:
                        setMenu2();
//                        repaint();
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.playAction();
                        break;
                    case MENU_SOUND:
                        sound = !sound;
                        try {
                            if (sound) {
                                menu = Image.createImage("/menu1.png");
                            } else {
                                menu = Image.createImage("/menu2.png");
                            }
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        repaint();
                        break;
                    case MENU_HELP:
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.helpAction();
                        break;
                    case MENU_EXIT:
                        if (sound) {
                            Thread sndThread = new Thread(snd);
                            sndThread.run();

                        }
                        midlet.destroyApp(true);
                        break;
                }
        }
    }

    public void setMenu2() {
        gameOn = true;
        try {
//            background = Image.createImage("/menu-bg-2pl.gif");
            if (sound) {
                menu = Image.createImage("/menu3.png");
            } else {
                menu = Image.createImage("/menu4.png");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        selected = MENU_CONTINUE;
        repaint();
    }

    public void setMenu() {
        gameOn = false;
        try {
            // inicializace obrazku
            background = Image.createImage("/menu-bg.gif");
            if (sound) {
                menu = Image.createImage("/menu1.png");
            } else {
                menu = Image.createImage("/menu2.png");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        selected = MENU_CONTINUE;
    }

    public boolean getSound() {
        return this.sound;
    }
}
