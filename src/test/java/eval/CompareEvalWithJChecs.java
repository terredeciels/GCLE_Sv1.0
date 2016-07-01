package eval;

import ia.Search;
import main.Fen;
import main.Ui;
import position.FenToGPosition;
import position.GCoups;
import position.Generateur;
import sanutils.SANException;

import static sanutils.SANUtils.toSAN;
import static xboard.XBoardAdapter.parseCommand;

public class CompareEvalWithJChecs {

    public CompareEvalWithJChecs() throws SANException {

        initFenList();
        int nb = 0, egaux = 0;
        for (String f : Fen.getFenList()) {
            Generateur gp = FenToGPosition.toGPosition(f);
            Search search = new Search(gp);
            GCoups move = search.getBestMove();

            parseCommand("setboard " + f);
            parseCommand("go");
            String san = getSANBestMove();
            nb++;
            String gSan = toSAN(gp, move);
            System.out.println(f + ";" + gSan + ";" + san);
            if (gSan.equals(san)) {
                egaux++;
            }
        }
        System.out.println(egaux);
        System.out.println(nb);
        System.out.println("% d' égalités: " + egaux * 100 / nb);
    }

    public static void main(String[] args) {
//        CompareEvalWithJChecs evalTest = new CompareEvalWithJChecs();
    }

    private void initFenList() {
        String[] command = new String[3];
        command[0] = "-cli";
        command[1] = "D:\\Documents\\CHESS\\GCLE\\5partiestest.pgn";// %E = 10%
        Ui.main(command);
    }

    private String getSANBestMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
