/**
 * Trida reprezentujici logickyhraci kamen
 */

package logic;

public class Stone {
    private int player;                  //Barva kamene

    /**
     * Konstruktor
     * @param type Barva kamene
     */
    Stone(int type) {
        this.player = type;
    }

    /**
     * konstrolni vypis barvy
     */
    public void Print() {
        if (player == Constants.BLACK)
            System.out.print("B");
        else
            System.out.print("W");
    }

    /**
     *
     * @return Barvu kamene
     */
    public int GetPlayer() {
        return player;
    }
}
