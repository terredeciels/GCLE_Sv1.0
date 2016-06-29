package perft;

import position.FenToGPosition;
import position.GCoups;
import position.GPositionS;
import position.UndoGCoups;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compare perft results against the file supplied by ROCE epd file
 */
public class PerftCompare {

    public static void main(String[] args) throws IOException {
        int maxDepth = 4;
        FileReader fileReader = new FileReader("D:\\Documents\\ECHECS\\GCLEv1.1.4\\perftsuite.epd");
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        int passes = 0;
        int fails = 0;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length < 3) {
                continue;
            }
            String fen = parts[0].trim();
            for (int i = 1; i < parts.length; i++) {
                if (i > maxDepth) {
                    break;
                }
                String entry = parts[i].trim();
                String[] entryParts = entry.split(" ");
                int perftResult = Integer.parseInt(entryParts[1]);
                GPositionS position = FenToGPosition.toGPosition(fen);
                PerftResult result = Perft.perft(position, i);
                if (perftResult == result.moveCount) {
                    passes++;
                    System.out.println("PASS: " + fen + ". Moves " + result.moveCount + ", depth " + i);
                } else {
                    fails++;
                    System.out.println("FAIL: " + fen + ". Moves " + result.moveCount + ", depth " + i);
                    break;
                }
            }
        }

        System.out.println("Passed: " + passes);
        System.out.println("Failed: " + fails);
    }

    public static class PerftResult {

        public long moveCount = 0;
        public long timeTaken = 0;

    }

    public static class Perft {

        public static PerftResult perft(GPositionS gp, int depth) {

            PerftResult result = new PerftResult();
            if (depth == 0) {
                result.moveCount++;
                return result;
            }
            List<GCoups> moves = gp.getCoupsValides();
            for (int i = 0; i < moves.size(); i++) {
                UndoGCoups ui = new UndoGCoups();
                if (gp.exec(moves.get(i), ui)) {
                    PerftResult subPerft = perft(gp, depth - 1);
                    gp.unexec(ui);
                    result.moveCount += subPerft.moveCount;
                }
            }
            return result;
        }

        public static Map<String, Long> divide(GPositionS gp, int depth) {
            HashMap<String, Long> result = new HashMap<>();
            List<GCoups> moves = gp.getCoups();
            for (int i = 0; i < moves.size(); i++) {
                UndoGCoups ui = new UndoGCoups();
                if (gp.exec(moves.get(i), ui)) {
                    PerftResult subPerft = perft(gp, depth - 1);
                    gp.unexec(ui);
                    result.put(toString(moves.get(i)), subPerft.moveCount);
                }
            }
            return result;
        }

        public static String toString(GCoups gc) {
//        int fromSquare = gc.getCaseO();
//        int toSquare = gc.getCaseX();
//        int promotedPiece = gc.getPiecePromotion();
//		boolean isQueening = MoveUtils.isQueening(move);
//        boolean isEnPassentCapture = gc.getTypeDeCoups() == TYPE_DE_COUPS.EnPassant;
            //		if (isQueening && (promotedPiece == 0)) {
//			// The move is just a generated queening move without a specific piece
//			throw new NotationException("Unable to determine promoted piece");
//		}
//		if (isQueening) {
//			result.append(fromPiece(promotedPiece));
//		}
            return GCoups.getString(gc);
        }
    }
}
