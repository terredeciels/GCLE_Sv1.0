package ia.tree;

import ia.IIA;
import position.GCoups;
import position.GPositionS;
import scala.collection.mutable.ListBuffer;

import java.util.Random;

public class RandomEngine implements IIA {

    static final Random RANDOMIZER = new Random();
    private final GPositionS gp;

    public RandomEngine(GPositionS gp) {
        this.gp = gp;
//        setMoveSorter(new StaticMoveSorter());
    }

    protected GCoups searchMoveFor(final GPositionS pEtat, final ListBuffer<GCoups> pCoups) {
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
