package perft;

import perft.PerftCompare.PerftResult;
import position.*;

import java.io.IOException;
import java.util.List;

public class PerftSpeedTest {

    public static void main(String[] args) throws IOException {
        perftTest();
    }

    public static void perftTest() {
        //voir http://chessprogramming.wikispaces.com/Perft+Results     
        String f = ICodage.FEN_INITIALE();
        GPositionS gp = FenToGPosition.toGPosition(f);
        int max_depth = 6;
        double t0 = System.nanoTime();
        for (int depth = 1; depth <= max_depth; depth++) {
            PerftResult res = perft(gp, depth);
            double t1 = System.nanoTime();
            System.out.println("Depth " + depth + " : " + (t1 - t0) / 1000000000 + " sec");
            System.out.println("Count = " + res.moveCount);
        }
//        res = perft(gp, 2);
//        res = perft(gp, 3);
//        res = perft(gp, 4);
//        res = perft(gp, 5);
//        res = perft(gp, 6);
    }

    public static PerftResult perft(GPositionS gp, int depth) {

        PerftResult result = new PerftResult();
        if (depth == 0) {
            result.moveCount++;
            return result;
        }
        List<GCoups> moves = gp.getCoupsValides();
        for (GCoups move : moves) {
            UndoGCoups ui = new UndoGCoups();
            if (gp.exec(move, ui)) {
                PerftResult subPerft = perft(gp, depth - 1);
                gp.unexec(ui);
                result.moveCount += subPerft.moveCount;
            }
        }
        return result;
    }
}
