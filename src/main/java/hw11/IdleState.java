package hw11;

import java.math.BigDecimal;

public final class IdleState extends StateOfVendingMachine {
    public IdleState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public String getName() {
        return "Idle";
    }

    @Override
    public void selectSnack(String snackName) {
        Snack snack = machine.getSnack(snackName);
        if (snack == null) {
            System.out.println("Unknown snack: " + snackName + ". Please select an available snack name.");
            return;
        }
        machine.beginSelection(snack);
        System.out.println("Selected " + snack.getName() + "; price "
                + VendingMachine.formatMoney(snack.getPrice()) + ".");
        machine.setState(machine.getWaitingForMoneyState());
    }

    @Override
    public void insertMoney(BigDecimal amount) {
        System.out.println("Select a snack first. Returning " + VendingMachine.formatMoney(amount) + ".");
    }

    @Override
    public void dispenseSnack() {
        System.out.println("Select a snack and insert enough money first.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("There is no active transaction to cancel.");
    }
}
