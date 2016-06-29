package ia.eval;

import position.GCoups;
import position.PieceType;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Propose un tri statique des mouvements, classant les mouvements avec prise
 * devant les mouvements sans prise. Les mouvements avec prise sont classés de
 * la prise la plus forte à la pris la plus faible.
 */
public class GCoupsTriStatic implements Comparator<GCoups>, Serializable {

    private static final long serialVersionUID = 1L;

    public GCoupsTriStatic() {
    }

    /**
     * Tri des mouvements.
     *
     * @return -1, 0, 1 en accord avec le contrat de compare().
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(final GCoups pMvt1, final GCoups pMvt2) {
        final int prise1 = pMvt1.getPiecePrise();
        final int prise2 = pMvt2.getPiecePrise();
        if (prise1 == 0) {
            if (prise2 != 0) {
                return 1;
            }
        } else if ((prise2 == 0) || (valueType(prise1) > valueType(prise2))) {
            return -1;
        }
        final int val1 = valueType(pMvt1.getPiece());
        final int val2 = valueType(pMvt2.getPiece());
        int res = 0;
        if (val1 > val2) {
            res = -1;
        } else if (val1 < val2) {
            res = 1;
        }
        if ((prise1 != 0) && (prise2 != 0)) {
            res = -res;
        }

        return res;
    }

    private int valueType(int piece) {
        int type_de_piece = (piece < 0) ? -piece : piece;
        return PieceType.getValue(type_de_piece);

    }
}
