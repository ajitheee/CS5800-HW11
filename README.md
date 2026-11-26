# CS5800 HW11

A vending machine using the State and Chain of Responsibility patterns.

Handler order:

```text
Coke -> Pepsi -> Cheetos -> Doritos -> KitKat -> Snickers
```

Snickers starts with one item. The driver buys it, then tries again to show
the sold-out refund.

## Run

Requires JDK 21.

Open the project in IntelliJ IDEA and run `hw11.VendingMachineDriver`.

From PowerShell in the project folder:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem src/main/java/hw11/*.java).FullName
java -cp out hw11.VendingMachineDriver
```

## Output

- [Console output](output/driver-output.txt)
- [IntelliJ screenshot](output/hw11-output.png)
