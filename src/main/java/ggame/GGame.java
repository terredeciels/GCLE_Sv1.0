package ggame;

import position.FenToGPosition;
import position.GCoups;
import position.GPositionS;
import position.ICodage;

/*
    @TODO Thread
 */
public class GGame {

    public GCoups lastmove;
    public GPositionS gPosition;

    public GGame() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE());
    }

    public GPositionS getGPositionMove() {
        return gPosition;
    }

    public void setGPositionMove(GPositionS gPosition) {
        this.gPosition = gPosition;
    }

    public void resetTo() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE());
    }
}
