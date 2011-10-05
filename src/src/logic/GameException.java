/**
 * Trida vyhazujici vyjimku ve hre (ukonceni)
 */

package logic;

public class GameException extends Exception {
    private String message;         //Co se stalo, pro kontrolni vypisy
    private int winner;             //Kdo je vitez

    /**
     * Konstruktor
     * @param msg Zprava pro kontrolu
     * @param player Kdo vyhral
     */
    GameException(String msg, int player) {
        this.message = msg;
        this.winner = player;
    }

    /**
     * Patova vyjimka
     * @param msg Pro konstrolu patu
     */
    GameException(String msg) {
        this.message = msg;
        this.winner = Constants.NONE;
    }

    /**
     *
     * @return Typ zpravy pro kontrolu
     */
    public String GetMessage() {
        return this.message;
    }

    /**
     *
     * @return Vitez
     */
    public int GetWinner() {
        return this.winner;
    }
}
