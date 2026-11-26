package hw11;

import java.math.BigDecimal;

public final class DispensingState extends StateOfVendingMachine {
    public DispensingState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public String getName() {
        return "Dispensing Snack";
    }

    @Override
    public void selectSnack(String snackName) {
        System.out.println("Please wait until dispensing is complete.");
    }

    @Override
    public void insertMoney(BigDecimal amount) {
        System.out.println("Dispensing is in progress. Returning " + VendingMachine.formatMoney(amount) + ".");
    }

    @Override
    public void dispenseSnack() {
        boolean dispensed = machine.getSnackDispenser().dispenseSnack(
                machine.getSelectedSnack(), machine.getMoneyInserted());
        // The matching handler checks stock and payment before changing inventory.
        machine.finishTransaction(dispensed);
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Please wait until dispensing is complete.");
    }
}
