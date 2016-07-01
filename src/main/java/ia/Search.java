package ia;

import ia.eval.EvalFunctionMaterial;
import ia.tree.AlphaBetaEngineJChecs;
import position.FenToGPosition;
import position.GCoups;
import position.Generateur;

/**
 * TODO Runnable
 */
public class Search {

    private final Generateur gPosition;
    private final IIA ia;
    private final IEval f_eval;
    private final int depth;

    public Search(Generateur gPosition) {
        this.gPosition = gPosition;
        depth = 4;
        f_eval = new EvalFunctionMaterial();
        ia = new AlphaBetaEngineJChecs(gPosition, f_eval, depth);
        // ia = new NegaScoutEngine(gPosition, f_eval, depth);
        // ia = new RandomEngine(gPosition);
    }

    public Search(String f) {
        gPosition = FenToGPosition.toGPosition(f);
        depth = 4;
        f_eval = new EvalFunctionMaterial();
        ia = new AlphaBetaEngineJChecs(gPosition, f_eval, depth);
        //  ia = new NegaScoutEngine(gPosition, f_eval, depth);
        // ia = new RandomEngine(gPosition);
    }

    public final GCoups getBestMove() {
        return ia.search();
    }

    public Generateur getGPositionMove() {
        return gPosition;
    }

}
