package ia;

import position.GPositionS;

public interface IAlphaBeta {

    int alphabeta(final GPositionS gPosition, final int pDepth, final int pAlpha, final int pBeta);
}
