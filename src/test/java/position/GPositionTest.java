package position;

import org.junit.*;
import scala.collection.mutable.ListBuffer;


public class GPositionTest {

    public GPositionTest() {
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
    public void testGetCoupsValides_boolean() {
        System.out.println("getCoupsValides");
//        String fen = ICodage.FEN_INITIALE;
//        GPosition instance = FenToGPosition.toGPosition(fen);
//        ArrayList<GCoups> result_blanc = instance.getCoupsValides(BLANC);
        String fen = "rnbq1rk1/ppp1bppp/4pn2/3P4/3P1B2/2N2N2/PP2PPPP/R2QKB1R b KQ - 0 6";
        GPositionS instance = FenToGPosition.toGPosition(fen);
        ListBuffer<GCoups> result = instance.getCoupsValides(instance.getTrait());//noirs
        System.out.println(result);

    }

    @Test
    public void testPrint() {
        System.out.println("print");
        String fen = ICodage.FEN_INITIALE();
        GPositionS instance = FenToGPosition.toGPosition(fen);
        String result = instance.print();
        int rg = 0;
        for (int c = 0; c < 64; c++) {
            int e = -instance.etats()[ICodage.CASES117()[c]];
            System.out.print(e + ";");
            rg++;
            if (rg == 8) {
                System.out.println();
                rg = 0;
            }
        }
        System.out.println();
        System.out.println(result);
    }

}
