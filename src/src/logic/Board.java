/**
 * Trida hraci plochy
 */
package logic;

public class Board {

    private Stone[][][] board;      //pole kamenu
    private int turn;               //hrac, ktery je na tahu
    private int count;              //pocet kamenu v poli

    /**
     *
     * @return Kdo je na tahu
     */
    public int GetPlayer() {
        return turn;
    }

    /**
     * Konstruktor, inicializuje pole, hrace a pocet kamenu
     */
    public Board() {
        this.board = new Stone[Constants.side][Constants.side][Constants.side];
        turn = Constants.BLACK;
        count = 0;
        for (int z = 0; z < Constants.side; z++) {
            for (int y = 0; y < Constants.side; y++) {
                for (int x = 0; x < Constants.side; x++) {
                    this.board[x][y][z] = null;
                }
            }
        }
    }

    /**
     * Kontrolni vypis hraci plochy
     */
    public void Print() {
        try {
            for (int y = 0; y < Constants.side; y++) {
                System.out.println(y + " wall:");
                for (int z = Constants.side - 1; z >= 0; z--) {
                    System.out.print("|");
                    for (int x = 0; x < Constants.side; x++) {
                        if (this.board[x][y][z] == null) {
                            System.out.print(" ");
                        } else {
                            this.board[x][y][z].Print();
                        }
                        System.out.print("|");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        } catch (java.lang.NullPointerException ex) {
            System.err.println("Null pointer ex: " + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Vlozi kamen na danou pozici do hraciho pole
     * @param x x-ova souradnice
     * @param y y-ova souradnice
     * @throws GameException Pokud je konec hry nebo nelze vlozit kamen, vyhazuje se vyjimka
     */
    public void Insert(int x, int y) throws GameException {
        for (int z = 0; z < Constants.side; z++) {
            if (this.board[x][y][z] == null) {
                this.board[x][y][z] = new Stone(turn);
                try {
                    Check(new Coordinations(x, y, z), turn);    //otestuje vyjimky
                    count++;

                    if (count == 64) {            //posledni kamen
                        throw new GameException("Pat", Constants.NONE);
                    }

                } catch (GameException ex) {
                    throw ex;
                }

                break;
            } else if (z == Constants.side - 1) {
                throw new GameException("Nelze vlozit kamen na pozici: [" + x + ";" + y + ";" + z + "]");
            }

        }

        //Tahne dalsi hrac
        if (turn == Constants.BLACK) {
            turn = Constants.WHITE;
        } else {
            turn = Constants.BLACK;
        }

    }

    /**
     * Rekurzivne testuje, jestil nejsou uz 4 kamenu nekde v rade od pozice vlozeneho kamene
     * @param cor Souradnice vlozeneho kamene
     * @throws GameException Nastal konec
     */
    private void Check(Coordinations cor, int player) throws GameException {
        //Testuje okoli kamenu
        for (int x = cor.GetX() - 1; x <= cor.GetX() + 1; x++) {
            if (x >= 0 && x < Constants.side) {
                for (int y = cor.GetY() - 1; y <= cor.GetY() + 1; y++) {
                    if (y >= 0 && y < Constants.side) {
                        for (int z = cor.GetZ() - 1; z <= cor.GetZ() + 1; z++) {
                            if (z >= 0 && z < Constants.side && this.board[x][y][z] != null && (x != cor.GetX() || y != cor.GetY() || z != cor.GetZ()) && this.board[x][y][z].GetPlayer() == player) {
                                //Pro kazde okoli kamenu zjisti smer testovani
                                int dirX = x - cor.GetX();
                                int dirY = y - cor.GetY();
                                int dirZ = z - cor.GetZ();
                                Coordinations sCor = new Coordinations(cor.GetX() - dirX, cor.GetY() - dirY, cor.GetZ() - dirZ);
                                if (sCor.isLegal()) {
                                    Stone s = this.board[sCor.GetX()][sCor.GetY()][sCor.GetZ()];
                                    if (s != null && s.GetPlayer() == player) {
                                        //Kamen je i na druhe strane, nez se bude testovat, zvetsi hloubku
                                        Check(new Coordinations(x, y, z), new Coordinations(dirX, dirY, dirZ), player, 2);
                                        continue;
                                    }
                                }
                                //Testuju ve smeru
                                Check(new Coordinations(x, y, z), new Coordinations(dirX, dirY, dirZ), player, 1);
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * Rekurzivne testuje ve smeru, ktery prijal na ctverici
     * @param cor Souradnice testujiciho kamene
     * @param dir Smer, ve kterem se testuje
     * @param player Na jakou barvu se testuje
     * @param depth Kolik kamenu uz je v rade
     * @throws GameException Nastal konec
     */
    private void Check(Coordinations cor, Coordinations dir, int player, int depth) throws GameException {
        int currentPlayer = this.board[cor.GetX()][cor.GetY()][cor.GetZ()].GetPlayer();

        if (currentPlayer != player) {
            return;
        }

        if (depth == 3) {
            throw new GameException("End", player);
        }
        int newX = cor.GetX() + dir.GetX();
        int newY = cor.GetY() + dir.GetY();
        int newZ = cor.GetZ() + dir.GetZ();
        //Ve smeru otestuje kamen
        if (newX >= 0 && newX < Constants.side && newY >= 0 && newY < Constants.side && newZ >= 0 && newZ < Constants.side) {
            Stone s = this.board[newX][newY][newZ];
            if (s != null && s.GetPlayer() == player) {
                //Je to stejna barva, testuje dalsi smer
                Check(new Coordinations(newX, newY, newZ), dir, player, depth + 1);
            }
        }
    }
}
