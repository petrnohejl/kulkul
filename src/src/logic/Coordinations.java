/**
 * Trida zpracovavajici souradnice systemu
 */

package logic;

public class Coordinations {

    ///jednotlive souradnice
    private int x;
    private int y;
    private int z;

    /**
     * Konstruktor
     * @param x x-ova souradnice
     * @param y y-ova souradnice
     * @param z z-ova souradnice
     */
    Coordinations(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Konstruktor
     * @param x x-ova souradnice
     * @param y y-ova souradnice
     */
    Coordinations(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Nastavi dane souradnice
     * @param x x-ova souradnice
     * @param y y-ova souradnice
     * @param z z-ova souradnice
     */
    public void SetCoordinations(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Nastavi parametr
     * @param z z-ova souradnice
     */
    public void SetZ(int z) {
        this.z = z;
    }

    /**
     *
     * @return x-ova souradnice
     */
    public int GetX() {
        return this.x;
    }

    /**
     *
     * @return y-ova souradnice
     */
    public int GetY() {
        return this.y;
    }

    /**
     *
     * @return z-ova souradnice
     */
    public int GetZ() {
        return this.z;
    }

    /**
     * Porovna 2 souradnice
     * @param c Souradnice, se kterou se porovnava
     * @return True, pokud se c rovna teto instanci, jinak false
     */
    public boolean equals(Coordinations c) {
        if (this.x == c.GetX() && this.y == c.GetY() && this.z == c.GetZ()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
//    public boolean isEdge() {
//        return isEdge(this.x) || isEdge(this.y) || isEdge(this.z);
//    }
//
//    /**
//     *
//     * @param pos
//     * @return
//     */
//    private boolean isEdge(int pos) {
//        if (pos == 0 || pos == Constants.side - 1) {
//            return true;
//        }
//        return false;
//    }

    /**
     *
     * @return True, pokud je tato instance v hracim poli
     */
    public boolean isLegal() {
        return isLegal(x) && isLegal(y) && isLegal(z);
    }

    /**
     *
     * @param cor jedna souradnice, ktera se testuje
     * @return pokud souradnice vyhovuje, vraci true
     */
    public boolean isLegal(int cor) {
        if (cor >= 0 && cor < Constants.side) {
            return true;
        }
        return false;
    }
}
