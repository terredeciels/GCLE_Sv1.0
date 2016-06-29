package santests;//package santests;
//
//import java.util.regex.Pattern;
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;
//import static junit.framework.Assert.fail;
//import org.junit.Test;
//
//import position.FenToGPosition;
//import position.GCoups;
//import position.GPosition;
//import position.ICodage;
//
//import sanutils.SANException;
//import static position.ICodage.TypeDeCoups.Deplacement;
//import static position.ICodage.TypeDeCoups.Prise;
//import position.UndoGCoups;
//import sanutils.SANUtils;
//import static sanutils.SANUtils.toMove;
//import static sanutils.SANUtils.toSAN;
//
///**
// * Tests unitaires de la classe utilitaire pour la notation SAN.
// *
// * @author David Cotton
// */
//public final class SANUtilsTest implements ICodage {
//
//    /**
//     * Chaine FEN d'une position avec une prise ambiguë venant d'un cavalier.
//     */
//    private static final String AMBIGUOUS_KNIGHT_FEN
//            = "rnbqk1nr/ppp2ppp/3p4/2b1p3/2N5/5N2/PPPPPPPP/R1BQKB1R w KQkq - 6 3";
//
//    /**
//     * Chaine FEN d'une position avec une prise ambiguë venant d'un pion.
//     */
//    private static final String AMBIGUOUS_PAWN_FEN
//            = "r1b1kbnr/pppp1ppp/4p3/6q1/1n6/P1P1P3/1P1P1PPP/RNBQKBNR w KQkq - 6 3";
//
//    /**
//     * Chaine FEN d'une position avec une prise ambiguë venant d'une reine.
//     */
//    private static final String AMBIGUOUS_QUEEN_FEN = "8/k7/8/1Q1p1Q2/8/5Q2/8/7K w - - 40 40";
//
//    /**
//     * Pour que JUnit puisse instancier les tests.
//     */
//    public SANUtilsTest() {
//        // Rien de spécifique...
//    }
//
//    /**
//     * Teste l'expression de validation d'une chaîne SAN.
//     */
//    @Test
//    public void testSANValidator() {
//        final Pattern regexp = SANUtils.SAN_VALIDATOR;
//
//        // Gestion des roques...
//        assertTrue(regexp.matcher("0-0").matches());
//        assertTrue(regexp.matcher("0-0-0").matches());
//        assertTrue(regexp.matcher("0-0-0#").matches());
//        // Répétitions...
//        assertFalse(regexp.matcher("0-00-0").matches());
//        // Espaces en trop...
//        assertFalse(regexp.matcher(" 0-0 ").matches());
//        // Des "o" majuscules plutôt que des zéros...
//        assertFalse(regexp.matcher("O-O").matches());
//        assertFalse(regexp.matcher("O-O-O").matches());
//
//        // Mouvements de pions sans prise, avec promotion...
//        assertTrue(regexp.matcher("a8Q").matches());
//        assertTrue(regexp.matcher("c1K").matches());
//        assertTrue(regexp.matcher("h1Q+").matches());
//        assertTrue(regexp.matcher("h1Q++").matches());
//        assertTrue(regexp.matcher("h1Q#").matches());
//        assertTrue(regexp.matcher("f8Q(=)").matches());
//        // Le "=" pour noter une promotion, fréquent dans PGN, ne fait pas partie de SAN.
//        assertFalse(regexp.matcher("c1=K").matches());
//        // Rangs incompatibles avec une promotion...
//        assertFalse(regexp.matcher("b7Q").matches());
//        assertFalse(regexp.matcher("g3R").matches());
//        // Colonnes ou rangs inconnus...
//        assertFalse(regexp.matcher("ab2Q").matches());
//        assertFalse(regexp.matcher("i4Q").matches());
//
//        // Mouvements de pions sans prise et sans promotion...
//        assertTrue(regexp.matcher("e5").matches());
//        assertTrue(regexp.matcher("h8").matches());
//        assertTrue(regexp.matcher("e5").matches());
//        // Colonnes ou rangs inconnus...
//        assertFalse(regexp.matcher("a0").matches());
//        assertFalse(regexp.matcher("bc3").matches());
//        assertFalse(regexp.matcher("c9").matches());
//        assertFalse(regexp.matcher("f12").matches());
//
//        // Mouvements de pions avec prise et sans promotion...
//        assertTrue(regexp.matcher("gxf8").matches());
//        assertTrue(regexp.matcher("axb3+").matches());
//        assertTrue(regexp.matcher("cxd3 e.p.").matches());
//        assertTrue(regexp.matcher("cxd6 e.p.").matches());
//        // Rang incompatible pour une prise en passant...
//        assertFalse(regexp.matcher("cxd7 e.p.").matches());
//        assertFalse(regexp.matcher("gxh2 e.p.").matches());
//        // Notation de la prise en passant erronée...
//        assertFalse(regexp.matcher("dxe3 ep").matches());
//        assertFalse(regexp.matcher("fxd6 e.p").matches());
//        // Mouvements de pions avec prise et avec promotion...
//        assertTrue(regexp.matcher("cxd8Q").matches());
//        // Colonne de départ manquante (obligatoire d'après SAN pour les prises avec pion).
//        assertFalse(regexp.matcher("xd8Q").matches());
//
//        // Mouvements de pièces sans prise...
//        assertTrue(regexp.matcher("Ke1").matches());
//        assertTrue(regexp.matcher("Qd3++").matches());
//        assertTrue(regexp.matcher("Nef3").matches());
//        assertTrue(regexp.matcher("N5d7").matches());
//        assertTrue(regexp.matcher("Qe2f3").matches());
//        // Levée d'ambiguïté erronée...
//        assertFalse(regexp.matcher("Njh2").matches());
//        assertFalse(regexp.matcher("N9b7").matches());
//        // Promotion d'une pièce ?!
//        assertFalse(regexp.matcher("Rh8Q").matches());
//
//        // Mouvements de pièces avec prise...
//        assertTrue(regexp.matcher("Kxe1(=)").matches());
//        assertTrue(regexp.matcher("Qf5xd5(=)").matches());
//        assertTrue(regexp.matcher("Nexf3").matches());
//        assertTrue(regexp.matcher("N5xd7").matches());
//        // Marqueur de prise mal placé...
//        assertFalse(regexp.matcher("Nxef3").matches());
//        // Prise en passant avec une pièce !?
//        assertFalse(regexp.matcher("Bxe6 e.p.").matches());
//
//        //------------------------------------------------------------------------
//        assertTrue(regexp.matcher("Bg4").matches());
////        assertTrue(regexp.matcher("Fg4").matches());// non F:Fou french
//    }
//
//    /**
//     * Teste la convertion chaine SAN / mouvement.
//     */
//    @Test
//    public void testToMove() {
//        GPosition gp = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
//        GPosition etat = gp;
//
//        try {
//            GCoups mvt = SANUtils.toMove(etat, "e3");
//            GCoups jcoups = new GCoups(BLANC * PION, e2, e3, 0, Deplacement);
//            assertEquals(jcoups, mvt);
//            UndoGCoups ug = new UndoGCoups();
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "Na6");
//            assertEquals(new GCoups(NOIR * CAVALIER, b8, a6, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "Bc4");
//            assertEquals(new GCoups(BLANC * FOU, f1, c4, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "c5");
//            assertEquals(new GCoups(NOIR * PION, c7, c5, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "Qf3");
//            assertEquals(new GCoups(BLANC * DAME, d1, f3, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "b6");
//            assertEquals(new GCoups(NOIR * PION, b7, b6, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "Qxf7++");
//            assertEquals(new GCoups(BLANC * DAME, f3, f7, PION, Prise), mvt);
//            etat.exec(mvt, ug);
//        } catch (final SANException e) {
//            fail(e.toString());
//        }
//        assertFalse(etat.getTrait() == BLANC);
//        assertTrue(etat.isInCheck(NOIR));
//
//        try {
//            gp = FenToGPosition.toGPosition(AMBIGUOUS_PAWN_FEN);
//            etat = gp;
//            GCoups mvt = toMove(etat, "cxb4");
//            assertEquals(new GCoups(BLANC * PION, c3, b4, NOIR * CAVALIER, Prise), mvt);
//
//            gp = FenToGPosition.toGPosition(AMBIGUOUS_KNIGHT_FEN);
//            etat = gp;
//            mvt = toMove(etat, "Nfxe5");
//            assertEquals(new GCoups(BLANC * CAVALIER, f3, e5, NOIR * PION, Prise), mvt);
//
//            gp = FenToGPosition.toGPosition(AMBIGUOUS_QUEEN_FEN);
//            etat = gp;
//            mvt = toMove(etat, "Qf5xd5(=)");
//            assertEquals(new GCoups(BLANC * DAME, f5, d5, NOIR * PION, Prise), mvt);
//
//        } catch (final SANException e) {
//            fail(e.toString());
//        }
//        gp = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
//        etat = gp;
//        try {
//            GCoups mvt = SANUtils.toMove(etat, "d4");
//            GCoups jcoups = new GCoups(BLANC * PION, d2, d4, 0, Deplacement);
//            assertEquals(jcoups, mvt);
//            UndoGCoups ug = new UndoGCoups();
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "d6");
//            assertEquals(new GCoups(NOIR * PION, d7, d6, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//            mvt = toMove(etat, "c4");
//            assertEquals(new GCoups(BLANC * PION, c2, c4, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
//        } catch (final SANException e) {
//            fail(e.toString());
//        }
//    }
//
//    /**
//     * Teste la convertion mouvement / chaine SAN.
//     */
//    @Test
//    public void testToSAN() {
//        GPosition gp = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
//        GPosition etat = gp;
//
//        GCoups mvt = new GCoups(BLANC * PION, e2, e3, 0, Deplacement);
//        assertEquals("e3", toSAN(etat, mvt));
//        UndoGCoups ug = new UndoGCoups();
//        etat.exec(mvt, ug);
//        mvt = new GCoups(NOIR * CAVALIER, b8, a6, 0, Deplacement);
//        assertEquals("Na6", toSAN(etat, mvt));
//        etat.exec(mvt, ug);
//        mvt = new GCoups(BLANC * FOU, f1, c4, 0, Deplacement);
//        assertEquals("Bc4", toSAN(etat, mvt));
//        etat.exec(mvt, ug);
//        mvt = new GCoups(NOIR * PION, c7, c5, 0, Deplacement);
//        assertEquals("c5", toSAN(etat, mvt));
//        etat.exec(mvt, ug);
//        mvt = new GCoups(BLANC * DAME, d1, f3, 0, Deplacement);
//        assertEquals("Qf3", toSAN(etat, mvt));
//        etat.exec(mvt, ug);
//        mvt = new GCoups(NOIR * PION, b7, b6, 0, Deplacement);
//        assertEquals("b6", toSAN(etat, mvt));
//        etat.exec(mvt, ug);
//
//        gp = FenToGPosition.toGPosition(AMBIGUOUS_KNIGHT_FEN);
//        etat = gp;
//        mvt = new GCoups(BLANC * CAVALIER, f3, e5, NOIR * PION, Prise);
//        assertEquals("Nfxe5", toSAN(etat, mvt));
//
//        gp = FenToGPosition.toGPosition(AMBIGUOUS_PAWN_FEN);
//        etat = gp;
//        mvt = new GCoups(BLANC * PION, c3, b4, NOIR * CAVALIER, Prise);
//        assertEquals("cxb4", toSAN(etat, mvt));
//
//        /*
//        @TODO bug toSAN ?
//         */
////            gp = FenToGPosition.toGPosition(AMBIGUOUS_QUEEN_FEN);
////            etat = gp;
////        mvt = new GCoups(BLANC * DAME, f5, d5, NOIR * PION, Prise);
////        assertEquals("Qf5xd5(=)", toSAN(etat, mvt));
//        gp = FenToGPosition.toGPosition("r1bq1rk1/pppp2pN/2n2n2/4pp2/P7/RP6/2PPPPPP/2BQKB1R b K - 0 7");
//        etat = gp;
//        mvt = new GCoups(NOIR * ROI, g8, h7, BLANC * CAVALIER, Prise);
//        assertEquals("Kxh7", toSAN(etat, mvt));
//
//    }
//}
