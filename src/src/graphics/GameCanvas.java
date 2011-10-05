package graphics;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import javax.microedition.m3g.World;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Group;

import logic.*;

/**
 * Hra
 */
public class GameCanvas extends Canvas implements Runnable {

    final int STICKS_NUM = 16;	// pocet tyci
    final int STICKS_IN_ROW = 4;	// pocet tyci v jedne rade
    final int SPHERES_NUM = 64;	// maximalni pocet kouli
    final int STICKS_DIST = 3;	// vzdalenost mezi tycemi
    final int SIZE_OF_AREA = (STICKS_IN_ROW - 1) * STICKS_DIST; // velikost herniho prostoru
    // nemame enum :(
    final int up = 0;
    final int left = 1;
    final int down = 2;
    final int right = 3;
    final int zoom_in = 4;
    final int zoom_out = 5;
    final int put = 6;
    final int idle = 7;
    int s_num = 0; // kolikatou kouli hrajeme (index do pole kouli)
    boolean gameOver = false;	// priznak konce hry
    boolean finished = false;
    boolean running = false;
    Thread myThread = null;
    int boardID = 1;	// ID desky
    int stickID = 2;	// ID tyce
    int sphereRedID = 3;	// ID koule
    int sphereBlueID = 4;	// ID koule
    Graphics3D g3d = null;	// zajistuje vykreslovani sceny
    World world = null;	// svet
    Camera cam = null;	// kamera
    Mesh board = null;	// deska
    Mesh stickMesh = null;	// tyc
    Mesh sphereRedMesh = null;	// koule
    Mesh sphereBlueMesh = null;	// koule
    Mesh[] stick = new Mesh[STICKS_NUM];	// pole tyci
    Mesh[] sphere = new Mesh[SPHERES_NUM];	// pole kouli
    // skupiny grafickych objektu
    Group stickGroup = new Group();
    Group sphereGroup = new Group();
    Group gameArea = new Group();
    Group worldCoord = new Group();
    int angleV = -50;       // uhel vertikalni
    int angleH = 0;         // uhel horizontalni
    float distance = 0.0f;  // vzdalenost od kamery
    int button = idle;      // ktere tlacitko bylo stisknuto
    boolean gameMode = false; // pro prepinani mezi hernim a prohlizecim modem
    boolean sphereActive = false; // urcuje, kdy s kouli muzeme pohnout
    boolean putActive = false;
    float x_dist, y_dist, z_dist;    // pozice aktivni koule
    int context = 0;
    int shift = idle;
    float high[][] = {
        {0.0f, 0.0f, 0.0f, 0.0f},
        {0.0f, 0.0f, 0.0f, 0.0f},
        {0.0f, 0.0f, 0.0f, 0.0f},
        {0.0f, 0.0f, 0.0f, 0.0f}
    };
    int indexX = 0;
    int indexY = 0;
    boolean dontLand = true; // koule jeste nedopadla na zem, pripadne na jinou kouli
    private Board gameboard;
    private GameMIDlet midlet;
    private MenuCanvas menu;
    private Sound sndPut;
    private Sound sndSwitch;
    private Sound sndEnd;
    private Sound sndMenu;

    /**
     * Konstruktor tridy
     *
     */
    public GameCanvas(GameMIDlet m, Displayable n) {
        sndMenu = new Sound(0, 100);
        sndPut = new Sound(1, 100);
        sndSwitch = new Sound(2, 100);
        sndEnd = new Sound(3,100);
        setFullScreenMode(true);
        loadWorld("/kulkul.m3g");
        loadObjects();
        loadSticks();
        gameboard = new Board();
        SetSphere(gameboard.GetPlayer());
        worldCoord.setOrientation(angleV, 1, 0, 0);

        myThread = new Thread(this);
        running = true;
        myThread.start();
        this.midlet = m;
        this.menu = (MenuCanvas) n;
    }

    public void runGameMode() {
        // v zavislosti na natoceni hraci plochy menim funci tlacitek
        if (angleH > 0) { // reseni pro natoceni hraci plochy v kladnem smeru
            if (angleH % 360 > 315) {
                context = 0;
            } else if (angleH % 360 > 225) {
                context = 1;
            } else if (angleH % 360 > 135) {
                context = 2;
            } else if (angleH % 360 > 45) {
                context = 3;
            } else {
                context = 0;
            }
        } else { // reseni pro natoceni hraci plochy v zapornem smeru
            if (angleH % 360 < -315) {
                context = 0;
            } else if (angleH % 360 < -225) {
                context = 3;
            } else if (angleH % 360 < -135) {
                context = 2;
            } else if (angleH % 360 < -45) {
                context = 1;
            } else {
                context = 0;
            }
        }

        if (button != idle && button != put) {
            shift = (button + context) % 4;
        } else {
            shift = button;
        }

        switch (shift) {
            case up: // posun nahoru
                if (y_dist < (float) SIZE_OF_AREA && sphereActive) {
                    y_dist += STICKS_DIST;
                    sphere[s_num - 1].setTranslation(x_dist, y_dist, z_dist);
                    indexY++;
                }
                break;
            case left: // posun vlevo
                if (x_dist > 0.0f && sphereActive) {
                    x_dist -= STICKS_DIST;
                    sphere[s_num - 1].setTranslation(x_dist, y_dist, z_dist);
                    indexX--;
                }
                break;
            case right: // posun vpravo
                if (x_dist < (float) SIZE_OF_AREA && sphereActive) {
                    x_dist += STICKS_DIST;
                    sphere[s_num - 1].setTranslation(x_dist, y_dist, z_dist);
                    indexX++;
                }
                break;
            case down: // posun dolu
                if (y_dist > 0.0f && sphereActive) {
                    y_dist -= STICKS_DIST;
                    sphere[s_num - 1].setTranslation(x_dist, y_dist, z_dist);
                    indexY--;
                }
                break;
            case put: // umisteni koule
                sphereActive = false;
                putActive = true;
                if (z_dist > high[indexX][indexY]) { // koule pada
                    z_dist -= 2;
                } else if (dontLand) { // koule jeste nedopadla, ale uz nemuze dal padat
                    high[indexX][indexY] += 2.0f;
                    dontLand = false; // koule dopadla
                    try {
                        gameboard.Insert(indexX, indexY);
                        //gameboard.Print();
                        SetSphere(gameboard.GetPlayer());
                    } catch (GameException ex) {
                        //game over
                        finished = true;
                        gameMode = !gameMode;
                        menu.setMenu();

                        if (menu.getSound()) {
                            Thread tEnd = new Thread(sndEnd);
                            tEnd.start();
                        }

                        //zobrazeni wins splash
                        SplashScreen splashScreen = new SplashScreen(gameboard.GetPlayer());
                        midlet.splashAction(splashScreen);
                        Thread t = new Thread(splashScreen);
                        t.start();
                    }
                    putActive = false;
                }
                sphere[s_num - 1].setTranslation(x_dist, y_dist, z_dist);
                break;
        }
        if (button != put) {
            button = idle; // zabranim posunu koule pri drzeni tlacitka
        }
    }

    public void runViewMode() {
        switch (button) {
            case zoom_in: // priblizovani
                if (distance > -15.0f) {
                    distance -= 2.0f;
                    cam.setTranslation(0.0f, 0.0f, distance);
                }
                break;
            case zoom_out: // oddalovani
                if (distance < 25.0f) {
                    distance += 2.0f;
                    cam.setTranslation(0.0f, 0.0f, distance);
                }
                break;
            case up: // otaceni nahoru
                if (angleV < 0) {
                    angleV += 10;
                    worldCoord.setOrientation(angleV, 1, 0, 0);
                }
                break;
            case down: // otaceni dolu
                if (angleV > -90) {
                    angleV -= 10;
                    worldCoord.setOrientation(angleV, 1, 0, 0);
                }
                break;
            case left: // otoceni vlevo
                angleH += 10;
                gameArea.setOrientation(angleH, 0, 0, 1);
                break;
            case right: // otaceni vpravo
                angleH -= 10;
                gameArea.setOrientation(angleH, 0, 0, 1);
                break;
        }
    }

    public void run() {
        while (running) {
            if (gameMode) {
                if (s_num > 0) {
                    runGameMode();
                }
            } else {
                runViewMode();
            }

            try {
                Thread.sleep(30);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Nacteni sveta z m3g souboru
     *
     */
    public void loadWorld(String path) {
        try {
            Object3D[] buffer = Loader.load(path);
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] instanceof World) {
                    world = (World) buffer[i];
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        world.addChild(worldCoord);
        worldCoord.addChild(gameArea);
        gameArea.addChild(stickGroup);
        gameArea.addChild(sphereGroup);
    }

    /**
     * Nacteni objektu
     *
     */
    public void loadObjects() {
        cam = world.getActiveCamera(); // nacteni kamery
        cam.setPerspective(60.0f, (float) getWidth() / (float) getHeight(), 0.1f, 500000); // nastaveni perspektivy kamery

        // nacteni objektu
        board = (Mesh) world.find(boardID);
        stickMesh = (Mesh) world.find(stickID);
        sphereRedMesh = (Mesh) world.find(sphereRedID);
        sphereBlueMesh = (Mesh) world.find(sphereBlueID);

        world.removeChild(board);
        world.removeChild(stickMesh);
        world.removeChild(sphereRedMesh);
        world.removeChild(sphereBlueMesh);
        gameArea.addChild(board);
    }

    /**
     * Vytvoreni kopii tyci
     *
     */
    public void loadSticks() {
        int c = 0;
        for (int i = 0; i <= SIZE_OF_AREA; i += STICKS_DIST) {
            for (int j = 0; j <= SIZE_OF_AREA; j += 3) {
                stick[c] = (Mesh) stickMesh.duplicate();
                stick[c].setTranslation(i, j, 0);
                stickGroup.addChild(stick[c]);
                c++;
            }
        }
    }

    /**
     * Vytvoreni jedne koule
     *
     */
    public void getSphere(int player) {
        // pocatecni poloha koule
        x_dist = 0.0f;
        y_dist = 0.0f;
        z_dist = 8.0f;

        indexX = 0;
        indexY = 0;

        if (player == Constants.BLACK) {
            sphere[s_num] = (Mesh) sphereBlueMesh.duplicate();
        } else {
            sphere[s_num] = (Mesh) sphereRedMesh.duplicate();
        }

        sphere[s_num].setTranslation(x_dist, y_dist, z_dist);
        sphereGroup.addChild(sphere[s_num]);
        s_num++;
    }

    /**
     * Vykresleni sceny
     *
     */
    public void paint(Graphics g) {
        g3d = Graphics3D.getInstance();
        try {
            g3d.bindTarget(g, true, 0); // Graphics3D.ANTIALIAS | Graphics3D.TRUE_COLOR
            g3d.render(world);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            g3d.releaseTarget();
        }

        // HUD
        g.setColor(0, 0, 0);
        if (gameMode) {
            g.drawString("Play mode", getWidth(), 0, Graphics.TOP | Graphics.RIGHT);
        } else {
            g.drawString("View mode", getWidth(), 0, Graphics.TOP | Graphics.RIGHT);
        }

        String wins = "";
        if (finished) {
            wins = " wins!";
        }

        if (gameboard.GetPlayer() == 0) {
            g.setColor(0, 0, 204);
            g.drawString("Player 1" + wins, 2, 2, Graphics.TOP | Graphics.LEFT);
        } else {
            g.setColor(204, 0, 0);
            g.drawString("Player 2" + wins, 2, 2, Graphics.TOP | Graphics.LEFT);
        }

        repaint();
    }

    /**
     * Inicializace podminek pri spusteni hry
     *
     */
    protected void init() {
        gameOver = false;
    }

    public void endGame() {
        gameOver = true;
    }

    public boolean isOver() {
        return gameOver;
    }

    /**
     * Nastaveni ovladani
     *
     */
    protected void keyPressed(int keyCode) {
        if (putActive) {
//            keyCode = KEY_NUM5;
            return;
        }
        if (keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT)) {
            button = left;
        } else if (keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT)) {
            button = right;
        } else if (keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN)) {
            button = down;
        } else if (keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP)) {
            button = up;
        } else if (keyCode == KEY_NUM1) {
            button = zoom_in;
        } else if (keyCode == KEY_NUM3) {
            button = zoom_out;
        } else if (keyCode == KEY_NUM5 || keyCode == getKeyCode(Canvas.FIRE)) {
            if (high[indexX][indexY] < 8.0f) { // muzeme umistit kouli, pouze pokud je pro ni misto
                button = put;

                if (gameMode && menu.getSound()) {
                    Thread tPut = new Thread(sndPut);
                    tPut.start();
                }
            }
        } else if (keyCode == KEY_NUM7) {
            button = idle;
            if (!finished) {
                gameMode = !gameMode;

                if (menu.getSound()) {
                    Thread tSwitch = new Thread(sndSwitch);
                    tSwitch.start();
                }
            }
        } else if (keyCode == KEY_NUM0) {
            if (finished) {
                gameOver = true;
            }
            midlet.menuAction();

            if (menu.getSound()) {
                Thread tMenu = new Thread(sndMenu);
                tMenu.start();
            }
        }
    }

    private void SetSphere(int player) {
        sphereActive = true;
        dontLand = true;
        button = idle;
        getSphere(player);
    }

    protected void keyReleased(int keyCode) {
        if (putActive) {
//            keyCode = KEY_NUM5;
            return;
        }
        if (keyCode == KEY_NUM4 || keyCode == getKeyCode(Canvas.LEFT) ||
                keyCode == KEY_NUM6 || keyCode == getKeyCode(Canvas.RIGHT) ||
                keyCode == KEY_NUM8 || keyCode == getKeyCode(Canvas.DOWN) ||
                keyCode == KEY_NUM2 || keyCode == getKeyCode(Canvas.UP) ||
                keyCode == KEY_NUM1 || keyCode == KEY_NUM3) {
            button = idle;
        }
    }
}
