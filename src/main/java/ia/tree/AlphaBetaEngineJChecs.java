package ia.tree;

import ia.IEval;
import ia.IIA;
import position.GCoups;
import position.GPositionS;
import position.UndoGCoups;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.mutable.ListBuffer;

public class AlphaBetaEngineJChecs implements IIA {

    private final int depth;
    private final IEval f_eval;
    private final GPositionS gp;

    public AlphaBetaEngineJChecs(GPositionS gp, IEval f_eval, int depth) {
        this.f_eval = f_eval;
        this.gp = gp;
        this.depth = depth;
    }

    private int alphabeta(final GPositionS gp, final int pProfondeur, final int pAlpha, final int pBeta) {

        final int trait = gp.getTrait();
        if (pProfondeur == 0) {
            return evaluate(gp, trait);
        }

        final ListBuffer<GCoups> coups = getValidMoves(gp, trait);

        final int l = coups.size();
        if (l == 0) {
            return evaluate(gp, trait);
        }

        int res = MAT_VALUE - 1;
        int alpha = pAlpha;
      Iterator<GCoups> it = coups.iterator();
        while (it.hasNext()) {
            GCoups mvt=it.next();
            UndoGCoups ug = new UndoGCoups();
            gp.exec(mvt, ug);
            final int note = -alphabeta(gp, pProfondeur - 1, -pBeta, -alpha);
            gp.unexec(ug);

            if (note > res) {
                res = note;
                if (res > alpha) {
                    alpha = res;
                    if (alpha > pBeta) {
                        return res;
                    }
                }
            }
        }

        return res;
    }

    @Override
    public GCoups search() {
        return searchMoveFor(gp, gp.getCoupsValides());
    }

    public GCoups searchMoveFor(final GPositionS gp, final ListBuffer<GCoups> pCoups) {

        final int l = pCoups.size();

        GCoups res = pCoups.apply(0);
        int alpha = MAT_VALUE - 1;

        Iterator<GCoups> it = pCoups.iterator();
        while (it.hasNext()) {
GCoups mvt= it.next();
            UndoGCoups ug = new UndoGCoups();
            gp.exec(mvt, ug);
            final int note = -alphabeta(gp, depth - 1, MAT_VALUE, -alpha);
            gp.unexec(ug);
            if ((note > alpha)) {
                alpha = note;
                res = mvt;
            }
        }
        return res;
    }

    private int evaluate(GPositionS gp, int trait) {
        return f_eval.evaluate(gp, trait);
    }

    private ListBuffer<GCoups> getValidMoves(GPositionS gp, int trait) {
        return gp.getCoupsValides(trait);
    }

}
