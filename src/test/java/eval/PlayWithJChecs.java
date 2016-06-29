package eval;

import sanutils.SANException;

import static xboard.XBoardAdapter.parseCommand;

public class PlayWithJChecs {

    public static void main(String[] args) throws SANException {
//        parseCommand("usermove e4");
//        parseCommand("usermove c4");
        String fen = "r4rk1/2p1bppp/p1q2n2/7b/1p6/1BPP1N1P/PP3PP1/R1BQR1K1 w - - 0 16";
//        getXbSAN();
//        System.out.println("move " + XbSAN);
//        String fen = ICodage.FEN_INITIALE;
//        String pCommande = "setboard " + fen;
//        parseCommand(pCommande);
//        parseCommand("go");
//        String san = getSANBestMove();
//        System.out.println(fen+";"+san);
//
//        fen = "r1bq1rk1/pppp2pN/2n2n2/4pp2/P7/RP6/2PPPPPP/2BQKB1R b K - 0 7";
        fen = "r2q1rk1/ppp1bppp/1nn5/4p3/6b1/P1NP1NP1/1P2PPBP/R1BQ1RK1 w - - 3 10";
        fen = "2kr4/p4p2/5p2/8/p3P1b1/BP2P3/2r4P/4K2R b - - 2 25";
        parseCommand("setboard " + fen);
        parseCommand("usermove Kb7");
//        parseCommand("go");
//        String san = getXbSAN();
//        System.out.println(fen + ";" + san);
    }

}
