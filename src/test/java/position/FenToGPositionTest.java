package position;

import org.junit.*;


public class FenToGPositionTest {

    public FenToGPositionTest() {
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
    public void testToGPosition() {
        System.out.println("toGPosition");
        String fen = ICodage.FEN_INITIALE();
        GPositionS instance = FenToGPosition.toGPosition(fen);

        int rg = 0;
        for (int c = 0; c < 64; c++) {
            // pour debug
    //        int e = FenToGPosition.cp_etats()[c];
          //  System.out.print(e + ";");
            rg++;
            if (rg == 8) {
                System.out.println();
                rg = 0;
            }
        }
    }

}
