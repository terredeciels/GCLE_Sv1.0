package ia.tree;

import ia.IIA;
import position.GCoups;
import position.Generateur;
import scala.collection.mutable.ListBuffer;

import java.util.Random;

public class RandomEngine implements IIA {

    static final Random RANDOMIZER = new Random();
    private final Generateur gp;

    public RandomEngine(Generateur gp) {
        this.gp = gp;
//        setMoveSorter(new StaticMoveSorter());
    }

    protected GCoups searchMoveFor(final Generateur pEtat, final ListBuffer<GCoups> pCoups) {
        final int l = pCoups.size();
        assert (l != 0);
//        addHalfmove(l);
        final GCoups res = pCoups.apply(RANDOMIZER.nextInt(l));
        return res;
    }

    @Override
    public GCoups search() {
        return searchMoveFor(gp, gp.coupsValides());
    }
}
