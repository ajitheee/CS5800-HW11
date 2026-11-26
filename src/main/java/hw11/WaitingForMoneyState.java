package hw11;

import java.math.BigDecimal;

public final class WaitingForMoneyState extends StateOfVendingMachine {
    public WaitingForMoneyState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public String getName() {
        return "Waiting for Money";
    }

    @Override
    public void selectSnack(String snackName) {
        System.out.println("Complete or cancel the current selection before selecting another snack.");
    }

    @Override
    public void insertMoney(BigDecimal amount) {
        machine.addMoney(amount);
        System.out.println("Inserted " + VendingMachine.formatMoney(amount)
                + "; total " + VendingMachine.formatMoney(machine.getMoneyInserted()) + ".");
        dispenseSnack();
    }

    @Override
    public void dispenseSnack() {
        BigDecimal amountDue = machine.getSelectedSnack().getPrice().subtract(machine.getMoneyInserted());
        if (amountDue.signum() > 0) {
            System.out.println("Insufficient money. Please insert " + VendingMachine.formatMoney(amountDue) + " more.");
            return;
        }
        machine.setState(machine.getDispensingState());
        // VendingMachine delegates this request to the new current state.
        machine.dispenseSnack();
    }
}
