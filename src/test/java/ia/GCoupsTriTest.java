package ia;//package ia;
//
//import ia.eval.GCoupsTriStatic;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//import org.junit.Test;
//import position.GCoups;
//import static position.ICodage.*;
//import static position.ICodage.TYPE_DE_COUPS.*;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
////import fr.free.jchecs.core.Move;
////import fr.free.jchecs.core.Square;
///**
// * Tests unitaires des classes d'ordenancement des mouvements.
// *
// * @author David Cotton
// */
//public final class GCoupsTriTest {
//
//    /**
//     * Pour que JUnit puisse instancier les tests.
//     */
//    public GCoupsTriTest() {
//        // Rien de spécifique...
//    }
//
//    /**
//     * Valide la méthode de comparaison du tri static.
//     */
//    @Test
//    public void valideStaticCompareTo() {
//        final Comparator<GCoups> comp = new GCoupsTriStatic();
//        final GCoups mvt1 = new GCoups(NOIR * PION, a2, a4, 0, Deplacement);
//        final GCoups mvt2 = new GCoups(NOIR * PION, CASES117[8], CASES117[17], BLANC * PION, Prise);
//        final GCoups mvt3 = new GCoups(NOIR * TOUR, CASES117[16], CASES117[24], 0, Deplacement);
//        final GCoups mvt4 = new GCoups(NOIR * PION, CASES117[16], CASES117[24], 0, Deplacement);
//
//        assertTrue(comp.compare(mvt1, mvt1) == 0);
//
//        assertTrue(comp.compare(mvt1, mvt2) == 1);
//        assertTrue(comp.compare(mvt2, mvt1) == -1);
//
//        assertTrue(comp.compare(mvt1, mvt3) == 1);
//        assertTrue(comp.compare(mvt3, mvt1) == -1);
//
//        assertTrue(comp.compare(mvt2, mvt3) == -1);
//        assertTrue(comp.compare(mvt3, mvt2) == 1);
//
//        assertTrue(comp.compare(mvt1, mvt4) == 0);
//        assertTrue(comp.compare(mvt4, mvt1) == 0);
//
//        final ArrayList<GCoups> mvts = new ArrayList<>();
//        mvts.add(mvt1);
//        mvts.add(mvt2);
//        mvts.add(mvt3);
//        mvts.add(mvt4);
//        Collections.sort(mvts, comp);
//        assertEquals(mvts.get(0), mvt2);
//        assertEquals(mvts.get(1), mvt3);
//        assertEquals(mvts.get(2), mvt1);
//        assertEquals(mvts.get(3), mvt4);
//    }
//}
