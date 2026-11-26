package hw11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Holds inventory, transaction data, the current state, and the dispensing chain. */
public final class VendingMachine {
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private final Map<String, Snack> snacks = new LinkedHashMap<>();
    private final SnackDispenseHandler snackDispenser;
    private final StateOfVendingMachine idleState = new IdleState(this);
    private final StateOfVendingMachine waitingForMoneyState = new WaitingForMoneyState(this);
    private final StateOfVendingMachine dispensingState = new DispensingState(this);
    private StateOfVendingMachine currentState = idleState;
    private Snack selectedSnack;
    private BigDecimal moneyInserted = ZERO;

    public VendingMachine(SnackDispenseHandler snackDispenser, Snack... inventory) {
        this.snackDispenser = Objects.requireNonNull(snackDispenser, "A dispensing chain is required.");
        if (inventory.length == 0) {
            throw new IllegalArgumentException("At least one snack is required.");
        }
        for (Snack snack : inventory) {
            Objects.requireNonNull(snack, "Snacks cannot be null.");
            String key = snack.getName().toLowerCase(Locale.ROOT);
            if (snacks.putIfAbsent(key, snack) != null) {
                throw new IllegalArgumentException("Duplicate snack: " + snack.getName());
            }
        }
    }

    public void selectSnack(String snackName) {
        currentState.selectSnack(snackName);
    }

    public void insertMoney(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Insert a positive amount of money.");
            return;
        }
        BigDecimal payment;
        try {
            payment = BigDecimal.valueOf(amount).setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            System.out.println("Money must be in whole cents. Payment rejected.");
            return;
        }
        currentState.insertMoney(payment);
    }

    public void dispenseSnack() {
        currentState.dispenseSnack();
    }

    public void cancelTransaction() {
        currentState.cancelTransaction();
    }

    public StateOfVendingMachine getCurrentState() {
        return currentState;
    }

    public Snack getSelectedSnack() {
        return selectedSnack;
    }

    public BigDecimal getMoneyInserted() {
        return moneyInserted;
    }

    public Snack getSnack(String name) {
        return name == null ? null : snacks.get(name.trim().toLowerCase(Locale.ROOT));
    }

    public void displayInventory() {
        System.out.println("Snack inventory:");
        for (Snack snack : snacks.values()) {
            System.out.printf("  %-8s %s  quantity: %d%n", snack.getName(),
                    formatMoney(snack.getPrice()), snack.getQuantity());
        }
    }

    StateOfVendingMachine getWaitingForMoneyState() {
        return waitingForMoneyState;
    }

    StateOfVendingMachine getDispensingState() {
        return dispensingState;
    }

    SnackDispenseHandler getSnackDispenser() {
        return snackDispenser;
    }

    void setState(StateOfVendingMachine state) {
        System.out.println("State: " + currentState.getName() + " -> " + state.getName());
        currentState = state;
    }

    void beginSelection(Snack snack) {
        selectedSnack = snack;
    }

    void addMoney(BigDecimal amount) {
        moneyInserted = moneyInserted.add(amount);
    }

    void finishTransaction(boolean dispensed) {
        BigDecimal returnedMoney = dispensed
                ? moneyInserted.subtract(selectedSnack.getPrice()) : moneyInserted;
        if (returnedMoney.signum() > 0) {
            System.out.println((dispensed ? "Change returned: " : "Money refunded: ")
                    + formatMoney(returnedMoney) + ".");
        }
        selectedSnack = null;
        moneyInserted = ZERO;
        setState(idleState);
    }

    static String formatMoney(BigDecimal amount) {
        return "$" + amount.setScale(2, RoundingMode.UNNECESSARY).toPlainString();
    }
}
