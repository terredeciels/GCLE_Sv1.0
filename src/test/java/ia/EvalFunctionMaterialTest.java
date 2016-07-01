package ia;

import ia.eval.EvalFunctionMaterial;
import org.junit.*;
import position.Generateur;
import position.ICodage;

public class EvalFunctionMaterialTest {

    private final Generateur gp;

    public EvalFunctionMaterialTest() {
        String f = "r3k2r/8/8/8/8/8/8/4K3 w kq - 0 1";
//        f = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
        f = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
//        f=ICodage.FEN_INITIALE;
//        f="rnb1kb1r/ppp1pppp/8/8/3QP3/8/PP2P1PP/RNB1KBNR b KQkq - 0 6";
        f = "rnq4r/pp2bkpp/2p5/8/8/8/PPP1NKPP/RNBQ3R b - - 0 10";
        f = "rnbqkb1r/pppppp1p/5np1/8/2PP4/2N5/PP2PPPP/R1BQKBNR b KQkq - 1 3";

        Search g = new Search(f);
        gp = g.getGPositionMove();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        int trait = ICodage.BLANC();
        EvalFunctionMaterial eval = new EvalFunctionMaterial();
        int result = eval.evaluate(gp, trait);
        System.out.println(result);
    }

}
