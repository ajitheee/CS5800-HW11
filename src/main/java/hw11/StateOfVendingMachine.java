package hw11;

import java.math.BigDecimal;

/** State pattern: the current state decides how each user action is handled. */
public abstract class StateOfVendingMachine {
    protected final VendingMachine machine;

    protected StateOfVendingMachine(VendingMachine machine) {
        this.machine = machine;
    }

    public abstract String getName();

    public abstract void selectSnack(String snackName);

    public abstract void insertMoney(BigDecimal amount);

    public abstract void dispenseSnack();

    public void cancelTransaction() {
        System.out.println("Transaction cancelled.");
        machine.finishTransaction(false);
    }
}
