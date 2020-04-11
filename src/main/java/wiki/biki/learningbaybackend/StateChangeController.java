package wiki.biki.learningbaybackend;

public class StateChangeController {

    public static int increaseStateFrom(int currentState) {
        switch (currentState) {
            case 0: return 1;
            case 1:
            case 2:
                return 3;
            default: return -1;
        }
    }

    public static int decreaseStateFrom(int currentState) {
        switch (currentState) {
            case 1:
            case 2:
                return 0;
            case 3: return 2;
            default: return -1;
        }
    }

}
