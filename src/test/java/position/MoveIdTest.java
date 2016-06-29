//package position;
//
//public class MoveIdTest {
//
//    public static void main(String[] args) {
//        int caseO, caseX;
////        TypeDeCoups type;
//        for (TypeDeCoups t : TypeDeCoups.values()) {
//            System.out.print(t.ordinal() + ",");
//        }
//        System.out.println();
//        int p=4;
//        for (caseO = 0; caseO < 64; caseO++) {
//            for (caseX = 0; caseX < 64; caseX++) {
//                for (int t = 0; t < TypeDeCoups.values().length(); t++) {
//
//                    int move =  (caseO + caseX * 100 + t * 10000+p*100000);
//                    int rr=move%100000;
//                     p=move/100000;
//                    int r = rr % 10000;
//                    int type =rr/10000;
//                    int cO = r % 100;
//                    int cX = r / 100;
//
////                    System.out.println(move + "type=" + type + " [caseO:" + caseO + "," + "caseX:" + caseX + "] -->"
////                            + "(cO=" + cO + "," + "cX=" + cX + ")");
//                    System.out.println(move + "(cO=" + cO + "," + "cX=" + cX + ")");
//                }
//            }
//        }
//
//    }
//
//}
