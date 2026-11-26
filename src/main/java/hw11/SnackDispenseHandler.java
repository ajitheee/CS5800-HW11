package hw11;

import java.math.BigDecimal;

/** Chain of Responsibility: unmatched requests move to the next snack handler. */
public abstract class SnackDispenseHandler {
    private final String snackName;
    private SnackDispenseHandler nextHandler;

    protected SnackDispenseHandler(String snackName) {
        this.snackName = snackName;
    }

    public SnackDispenseHandler setNext(SnackDispenseHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public final boolean dispenseSnack(Snack snack, BigDecimal moneyInserted) {
        System.out.println(snackName + " handler checking " + snack.getName() + ".");
        if (!snackName.equalsIgnoreCase(snack.getName())) {
            if (nextHandler != null) {
                return nextHandler.dispenseSnack(snack, moneyInserted);
            }
            System.out.println("No handler can dispense " + snack.getName() + ".");
            return false;
        }
        if (snack.getQuantity() == 0) {
            System.out.println(snackName + " is sold out. No snack dispensed.");
            return false;
        }
        if (moneyInserted.compareTo(snack.getPrice()) < 0) {
            System.out.println("Not enough money for " + snackName + ". No snack dispensed.");
            return false;
        }
        snack.dispenseOne();
        System.out.println("Dispensed " + snackName + ". Quantity remaining: " + snack.getQuantity() + ".");
        return true;
    }

    public final String describeChain() {
        return snackName + (nextHandler == null ? "" : " -> " + nextHandler.describeChain());
    }
}
