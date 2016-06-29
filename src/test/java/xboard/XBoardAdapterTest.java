package xboard;

import ggame.GGame;
import ia.Search;
import position.*;
import sanutils.SANException;
import sanutils.SANUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * interface entre l'I.A. et une I.H.M. ('Arena' par ex.) utilisant le protocole
 * XBoard/WinBoard.
 */
public final class XBoardAdapterTest {

    public static final String APPLICATION_NAME = "GCLE";
    public static final String APPLICATION_VERSION = "1.0";
    private static final String APPLICATION_STRING = APPLICATION_NAME + " " + APPLICATION_VERSION;
    private static final String FEATURES_STRING
            = "feature analyze=0 colors=0 myname=\"" + APPLICATION_STRING
            + "\" pause=0 ping=1 playother=0 san=1 setboard=1 sigint=0 sigterm=0 "
            + "time=0 usermove=1 variants=\"normal\" done=1";
    private static final Logger LOGGER = Logger.getLogger(XBoardAdapterTest.class.getName());
    private static final GGame game = new GGame();
    public static boolean DEBUG = false;
    private static GPositionS gPosition;
    private static boolean S_forceMode = false;
    /**
     * Etat du mode de jeu faible/fort.
     */
    private static boolean S_hardMode;// a voir
    private static boolean S_illegalPosition;

    private XBoardAdapterTest() {
    }

    public static void main(final String[] pArgs) throws SANException {
        assert pArgs != null;

        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter commands:");

        while (true) {
            String commande = null;
            try {
                commande = in.readLine();
            } catch (final IOException e) {
                LOGGER.severe(e.toString());
            }
            if (commande == null) {
                LOGGER.severe("Communication error.");
                System.exit(-1);
            } else {
                parseCommand(commande.trim());
            }
        }
    }

    /**
     * Interprète les commandes reçues.
     */
    private static void parseCommand(final String pCommande) throws SANException {
        assert pCommande != null;
        gPosition = game.gPosition;

        if (pCommande.startsWith("accepted ")) {
            // Tant mieux, mais il n'y a rien à faire.
        } else if (pCommande.startsWith("level ")) {
            // Non impléméntée...
        } else if (pCommande.startsWith("ping ")) {
            System.out.println("pong" + pCommande.substring(4));
        } else if (pCommande.startsWith("protover ")) {
            System.out.println(FEATURES_STRING);
        } else if (pCommande.startsWith("rejected ")) {
            System.out.println("tellusererror Missing feature " + pCommande.substring(9));
        } else if (pCommande.startsWith("result ")) {
            // Non impléméntée...
        } else if (pCommande.startsWith("setboard ")) {
            final String fen = pCommande.substring(9);
            gPosition = FenToGPosition.toGPosition(fen);
            game.setGPositionMove(gPosition);
            if (gPosition == null) {
                S_illegalPosition = true;
                System.out.println("tellusererror Illegal position");
            } else {
                S_illegalPosition = false;
//                gPosition = new XBoard(FenToGPosition.toGPosition(ICodage.FEN_INITIALE));
//                game.setGPositionMove(gPosition);
            }
        } else if (pCommande.startsWith("usermove ")) {

            String xbSAN = pCommande.substring(9);
            String san = xbSAN.replace("=", "").replace('O', '0');

            gPosition.setTrait(ICodage.BLANC());
//            System.out.println(gPosition.print());
//            System.out.println(gPosition);
//            System.out.println(gPosition.getTrait()==BLANC ? "trait aux blancs":"trait aux noirs");
            GCoups mvt = SANUtils.toMove(gPosition, san);

            if ((mvt == null) || S_illegalPosition) {
//                System.out.println("coups == null: " + mvt);
                System.out.println("Illegal move: " + xbSAN);
            } else {
                UndoGCoups ug = new UndoGCoups();
                gPosition.exec(mvt, ug);

//                System.out.println(gPosition.print());
                gPosition._fullmoveNumber_$eq(gPosition._fullmoveNumber() + 1);
                //gPosition._fullmoveNumber++;
                game.lastmove = mvt;

                if (!S_forceMode) {
                    Search search = new Search(gPosition);
                    GCoups gcoups_curr = null;
//                    try {
                    gcoups_curr = search.getBestMove();
//                    } catch (NodeNotFoundException ex) {
//                    }
                    san = SANUtils.toSAN(gPosition, gcoups_curr);

                    game.setGPositionMove(gPosition);
                    gPosition._fullmoveNumber_$eq(gPosition._fullmoveNumber() + 1);
                    //gPosition._fullmoveNumber++;
                    game.lastmove = gcoups_curr;

                    xbSAN = san.replace(" e.p.", "").replace("O", "0");
                    System.out.println("move " + xbSAN);

//                    ug = new UndoGCoups();
//                    gPosition.exec(gcoups_curr, ug);
//                    System.out.println(gPosition.print());
                }
            }
        } else if (pCommande.equals("computer")) {
            // Non impléméntée...
        } else if (pCommande.equals("easy")) {
            S_hardMode = false;
        } else if (pCommande.equals("force")) {
            S_forceMode = true;
        } else if (pCommande.equals("go")) {
            if (S_illegalPosition) {
                System.out.println("Error (illegal position): go");
            } else {
                S_forceMode = false;
                Search search = new Search(gPosition);
                GCoups gcoups_curr = null;
//                try {
                gcoups_curr = search.getBestMove();
//                } catch (NodeNotFoundException ex) {
//                }
                String san = SANUtils.toSAN(gPosition, gcoups_curr);

                game.setGPositionMove(gPosition);
                gPosition._fullmoveNumber_$eq(gPosition._fullmoveNumber() + 1);
                // gPosition._fullmoveNumber++;
                game.lastmove = gcoups_curr;

                final String xbSAN = san.replace(" e.p.", "").replace("O", "0");
                System.out.println("move " + xbSAN);

//                UndoGCoups ug = new UndoGCoups();
//                gPosition.exec(gcoups_curr, ug);
            }
        } else if (pCommande.equals("hard")) {
            S_hardMode = true;
        } else if (pCommande.equals("new")) {
            game.resetTo();
            S_forceMode = false;
            S_illegalPosition = false;
        } else if (pCommande.equals("nopost")) {
            // Non impléméntée...
        } else if (pCommande.equals("post")) {
            // Non impléméntée...
        } else if (pCommande.equals("quit")) {
            System.exit(0);
        } else if (pCommande.equals("random")) {
            // Rien à faire.
        } else if (pCommande.equals("xboard")) {
            System.out.println(APPLICATION_STRING + " started in xboard mode.");
        } else {
            System.out.println("Error (unknown command): " + pCommande);
        }
    }
}
