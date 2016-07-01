package ia;

import position.Generateur;

public interface IAlphaBeta {

    int alphabeta(final Generateur gPosition, final int pDepth, final int pAlpha, final int pBeta);
}
