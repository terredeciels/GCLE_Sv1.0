package ia.tree;

import ia.IEval;
import ia.IIA;
import position.GCoups;
import position.GPositionS;
import position.UndoGCoups;
import scala.collection.Iterator;
import scala.collection.mutable.ListBuffer;

/**
 * Moteur d'IA basé sur l'algorithme NegaScout (aussi appelé Principal Variation
 * Search), avec table de transposition, sur 5 demi-coups.
 */
public class NegaScoutEngine implements IIA {

    private final int depth;
    private final IEval f_eval;
    private final GPositionS gp;

    public NegaScoutEngine(GPositionS gp, IEval f_eval, int depth) {
        this.f_eval = f_eval;
        this.gp = gp;
        this.depth = depth;
    }

    /**
     * Recherche le meilleur coup évalué à partir d'une position.
     */
    private int negascout(final GPositionS gp, final int depth, final int pAlpha, final int pBeta, final int pLimite) {

        final int trait = gp.trait();

        if (depth <= pLimite) {
            final int valeur = evaluate(gp, trait);
            return valeur;
        }

        final ListBuffer<GCoups> coups = getValidMoves(gp, trait);
        final int l = coups.size();
        if (l == 0) {
            final int valeur = evaluate(gp, trait);
            return valeur;
        }

        int res = MAT_VALUE - 1;
        int alpha = pAlpha;

        for (int i = 0; i < l; i++) {
            UndoGCoups ug = new UndoGCoups();
            final GCoups mvt = coups.apply(i);
            gp.exec(mvt, ug);
            final int limite;
            if (((depth == 1) && ((l <= 3) || (mvt.getPiecePrise() != 0))) || gp.isInCheck(gp.trait())) {
                limite = -1;
            } else {
                limite = 0;
            }
            int note;
            if (i == 0) {
                note = -negascout(gp, depth - 1, -pBeta, -alpha, limite);
//                gp.unexec(ug);
            } else {
                note = -negascout(gp, depth - 1, -alpha - 1, -alpha, limite);
//                gp.unexec(ug);
            }
            if (note > res) {
                if ((i > 0) && (alpha < note) && (note < pBeta) && (depth > limite + 2)) {
                    note = -negascout(gp, depth - 1, -pBeta, -note, limite);
                    gp.unexec(ug);
                }
                res = note;
                if (res > alpha) {
                    alpha = res;
                    if (alpha > pBeta) {
                        break;
                    }
                }
            }
        }
        return res;
    }

    protected GCoups searchMoveFor(final GPositionS gp, final ListBuffer<GCoups> pCoups) {

        final int l = pCoups.size();
        GCoups res = pCoups.apply(0);
        int alpha = MAT_VALUE - 1;
        Iterator<GCoups> it = pCoups.iterator();
        while (it.hasNext()) {
            GCoups mvt = it.next();
            UndoGCoups ug = new UndoGCoups();
            gp.exec(mvt, ug);
            final int note = -negascout(gp, depth - 1, MAT_VALUE, -alpha, 0);
            gp.unexec(ug);
            if (note > alpha || note == alpha) {
                alpha = note;
                res = mvt;
            }
        }
        return res;
    }

    @Override
    public GCoups search() {
        return searchMoveFor(gp, gp.getCoupsValides());
    }

    private int evaluate(GPositionS gp, int trait) {
        return f_eval.evaluate(gp, trait);
    }

    private ListBuffer<GCoups> getValidMoves(GPositionS gp, int trait) {
        return gp.getCoupsValides(trait);
    }
}
