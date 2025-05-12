package Banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class TransactionProcessor implements Runnable {
    private final BankAccount account;
    private final String fileName;
    private final List<BankAccount> allAccounts;

    public TransactionProcessor(BankAccount account, String fileName, List<BankAccount> allAccounts) {
        this.account = account;
        this.fileName = fileName;
        this.allAccounts = allAccounts;
    }

    @Override
    public void run() {
        InputStreamReader is = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)));
        try (BufferedReader reader = new BufferedReader(is)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "Deposit":
                        int depositAmount = Integer.parseInt(parts[1]);
                        account.deposit(depositAmount);
                        break;

                    case "Withdraw":
                        int withdrawAmount = Integer.parseInt(parts[1]);
                        account.withdraw(withdrawAmount);
                        break;

                    case "Transfer":
                        int targetIndex = Integer.parseInt(parts[1])-1;
                        int transferAmount = Integer.parseInt(parts[2]);

                        BankAccount target = allAccounts.get(targetIndex);
                        account.transfer(target, transferAmount);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}