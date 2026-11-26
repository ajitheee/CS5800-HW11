package hw11;

import java.math.BigDecimal;

/** Demonstrates all six snacks, state transitions, payment handling, and sold-out Snickers. */
public final class VendingMachineDriver {
    private VendingMachineDriver() {
    }

    public static void main(String[] args) {
        Snack coke = new Snack("Coke", 1.50, 2);
        Snack pepsi = new Snack("Pepsi", 1.75, 2);
        Snack cheetos = new Snack("Cheetos", 1.25, 2);
        Snack doritos = new Snack("Doritos", 1.50, 2);
        Snack kitKat = new Snack("KitKat", 1.25, 2);
        Snack snickers = new Snack("Snickers", 1.50, 1);

        SnackDispenseHandler chain = new CokeDispenser();
        chain.setNext(new PepsiDispenser())
                .setNext(new CheetosDispenser())
                .setNext(new DoritosDispenser())
                .setNext(new KitKatDispenser())
                .setNext(new SnickersDispenser());

        VendingMachine machine = new VendingMachine(chain, coke, pepsi, cheetos, doritos, kitKat, snickers);
        System.out.println("HW11 - State and Chain of Responsibility");
        System.out.println("Chain: " + chain.describeChain());
        System.out.println("Initial state: " + machine.getCurrentState().getName());
        check(chain.describeChain().equals("Coke -> Pepsi -> Cheetos -> Doritos -> KitKat -> Snickers"),
                "The chain must follow the assignment's order.");
        checkIdle(machine);
        machine.displayInventory();

        scenario("1. Coke with exact payment");
        machine.selectSnack("Coke");
        machine.insertMoney(1.50);
        check(coke.getQuantity() == 1, "Coke should be dispensed once.");
        checkIdle(machine);

        scenario("2. Pepsi with insufficient money, then a second payment");
        machine.selectSnack("Pepsi");
        machine.insertMoney(1.00);
        machine.dispenseSnack();
        check(machine.getCurrentState() instanceof WaitingForMoneyState, "Wait for the remaining money.");
        check(pepsi.getQuantity() == 2, "Insufficient payment must not change inventory.");
        check(machine.getMoneyInserted().compareTo(new BigDecimal("1.00")) == 0, "Keep the partial payment.");
        machine.selectSnack("Coke");
        check(machine.getSelectedSnack() == pepsi, "Keep the current selection until completion or cancellation.");
        machine.insertMoney(0.75);
        check(pepsi.getQuantity() == 1, "Pepsi should be dispensed after full payment.");
        checkIdle(machine);

        scenario("3. Cheetos with overpayment and change");
        machine.selectSnack("Cheetos");
        machine.insertMoney(2.00);
        check(cheetos.getQuantity() == 1, "Cheetos should be dispensed once, with $0.75 change.");
        checkIdle(machine);

        scenario("4. Doritos with exact payment");
        machine.selectSnack("Doritos");
        machine.insertMoney(1.50);
        check(doritos.getQuantity() == 1, "Doritos should be dispensed once.");
        checkIdle(machine);

        scenario("5. Cancel a partial KitKat payment, then purchase KitKat");
        machine.selectSnack("KitKat");
        machine.insertMoney(0.50);
        machine.cancelTransaction();
        check(kitKat.getQuantity() == 2, "Cancellation must preserve KitKat inventory and refund $0.50.");
        checkIdle(machine);
        machine.selectSnack("KitKat");
        machine.insertMoney(1.25);
        check(kitKat.getQuantity() == 1, "KitKat should be dispensed once.");
        checkIdle(machine);

        scenario("6. Purchase the last Snickers: quantity reaches 0");
        machine.selectSnack("Snickers");
        machine.insertMoney(1.50);
        check(snickers.getQuantity() == 0, "The required Snickers case must reach quantity 0.");
        checkIdle(machine);

        scenario("7. Try sold-out Snickers: refund the entire payment");
        machine.selectSnack("Snickers");
        machine.insertMoney(2.00);
        check(snickers.getQuantity() == 0, "Sold-out Snickers must never become negative.");
        checkIdle(machine);

        scenario("8. Reject invalid actions without changing inventory");
        machine.selectSnack("Pretzels");
        machine.insertMoney(1.00);
        machine.dispenseSnack();
        checkIdle(machine);
        machine.selectSnack("Coke");
        machine.insertMoney(-1.00);
        machine.insertMoney(0.001);
        check(machine.getMoneyInserted().signum() == 0, "Invalid payments must not be accepted.");
        machine.cancelTransaction();
        check(coke.getQuantity() == 1, "Invalid actions must not dispense Coke.");
        checkIdle(machine);

        System.out.println();
        machine.displayInventory();
        System.out.println("All driver checks passed.");
    }

    private static void scenario(String description) {
        System.out.println("\n--- " + description + " ---");
    }

    private static void checkIdle(VendingMachine machine) {
        check(machine.getCurrentState() instanceof IdleState, "The machine must be idle.");
        check(machine.getSelectedSnack() == null, "The completed selection must be cleared.");
        check(machine.getMoneyInserted().signum() == 0, "The completed transaction balance must be zero.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("Driver check failed: " + message);
        }
    }
}
