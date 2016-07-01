package ggame;

import position.FenToGPosition;
import position.GCoups;
import position.Generateur;
import position.ICodage;

/*
    @TODO Thread
 */
public class GGame {

    public GCoups lastmove;
    public Generateur gPosition;

    public GGame() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE());
    }

    public Generateur getGPositionMove() {
        return gPosition;
    }

    public void setGPositionMove(Generateur gPosition) {
        this.gPosition = gPosition;
    }

    public void resetTo() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE());
    }
}
