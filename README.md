# HW11 Behavioral Design Patterns

A Java vending machine using State and Chain of Responsibility, following
HW11_BehavioralPatterns.docx. Open this folder as a project in IntelliJ IDEA
with the installed JDK 21. Select the saved **VendingMachineDriver** run
configuration and click Run. The Java package is `hw11`.

The six snacks and their handler order are:

```text
Coke -> Pepsi -> Cheetos -> Doritos -> KitKat -> Snickers
```

`VendingMachine` holds all inventory, the current `StateOfVendingMachine`,
the selected snack, the money inserted, and the first `SnackDispenseHandler`.
Each state handles the public actions. `DispensingState` sends the selected
snack through the handler chain. Only the matching handler can dispense it,
after verifying both payment and quantity. Prices and payments use
`BigDecimal` so cents are exact.

State transitions:

```text
Idle -- select a known snack --> Waiting for Money
Waiting for Money -- insufficient money --> Waiting for Money
Waiting for Money -- enough money --> Dispensing Snack
Dispensing Snack -- in stock: dispense and return change --> Idle
Dispensing Snack -- sold out: refund all money --> Idle
Waiting for Money -- cancel: refund all money --> Idle
```

Unknown selections remain idle. Money inserted before selection is returned.
While waiting, a new selection is rejected until the current transaction is
completed or cancelled. Invalid payments do not change the balance.

The driver buys every snack, demonstrates partial payment, change,
cancellation, and invalid actions. Snickers starts at quantity 1; a purchase
reduces it to 0. A second attempt leaves it at 0 and refunds the full $2.00.
The driver includes checks that throw an exception if the demonstrated state,
inventory, or balance is wrong. A successful run ends with
`All driver checks passed.`

## Program output

- [Complete driver output](output/driver-output.txt)
- [IntelliJ output screenshot](output/hw11-output.png), showing Snickers at
  quantity 0, the full sold-out refund, and exit code 0

## Compile and run

To compile and run from PowerShell in this project folder:

```powershell
$javaFiles = Get-ChildItem -LiteralPath 'src/main/java/hw11' -Filter '*.java' | ForEach-Object FullName
New-Item -ItemType Directory -Force -Path out/production/HW11 | Out-Null
javac -encoding UTF-8 -Xlint:all -Werror -d out/production/HW11 $javaFiles
java -cp out/production/HW11 hw11.VendingMachineDriver
```
