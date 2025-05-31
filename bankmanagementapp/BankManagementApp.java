/****** STUDENT NAME: Chris Garate *******/

package bankmanagementapp;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * BankManagementApp class: Simulates the Bank Management System in the main() method.
 */
public class BankManagementApp {

    public static void main(String[] args) {
        try (BankManagement manager = new BankManagement(new File("bankrecords.dat"));) {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                BankManagementApp.displayMenu();
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        manager.viewAllRecords();
                        break;
                    case 2:
                        System.out.print("Enter Account Number: ");
                        int accNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter Customer Name: ");
                        String custName = scanner.nextLine();
                        System.out.print("Enter Balance: ");
                        double balance = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline character
                        BankRecord newRecord = new BankRecord(accNumber, custName, balance);
                        manager.addRecord(newRecord);
                        break;
                    case 3:
                        System.out.print("Enter Account Number to Delete: ");
                        int delAccNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        manager.deleteRecord(delAccNumber);
                        break;
                    case 4:
                        System.out.print("Enter Account Number to Modify: ");
                        int modAccNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter New Customer Name: ");
                        String newCustName = scanner.nextLine();
                        System.out.print("Enter New Balance: ");
                        double newBalance = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline character
                        BankRecord modifiedRecord = new BankRecord(modAccNumber, newCustName, newBalance);
                        manager.modifyRecord(modifiedRecord);
                        break;
                    case 5:
                        System.out.print("Enter Account Number to Search: ");
                        int searchAccNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        BankRecord foundRecord = manager.searchRecord(searchAccNumber);
                        if (foundRecord != null) {
                            System.out.println(foundRecord);
                        } else {
                            System.out.println("Record not found.");
                        }
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 6);

            scanner.close();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void displayMenu() {
        System.out.println("\nBank Management System");
        System.out.println("1. View All Records");
        System.out.println("2. Add New Record");
        System.out.println("3. Delete Record");
        System.out.println("4. Modify Record");
        System.out.println("5. Search Record");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    } 
}

/**
 * BankManagement class: Contains all the methods to process bank records.
 */
class BankManagement implements Closeable {
    private RandomAccessFile file;
    private static final int DELETED = -1;

    public BankManagement(File dataFile) throws FileNotFoundException {
        file = new RandomAccessFile(dataFile, "rw"); 
    }

    public void addRecord(BankRecord record) throws IOException {
        file.seek(file.length());
        record.write(file);
        System.out.println("Record added.");
    }

    public void viewAllRecords() throws IOException {
        file.seek(0);
        while (file.getFilePointer() < file.length()) {
            BankRecord record = BankRecord.read(file);
            if (record.getAccountNumber() != DELETED) {
                System.out.println(record);
            }
        }
    }

    public void deleteRecord(int accountNumber) throws IOException {
        file.seek(0);
        boolean found = false;
        while (file.getFilePointer() < file.length()) {
            long pos = file.getFilePointer();
            BankRecord record = BankRecord.read(file);
            if (record.getAccountNumber() == accountNumber) {
                file.seek(pos);
                file.writeInt(DELETED);
                file.writeChars(String.format("%-30s", ""));
                file.writeDouble(0.0);
                System.out.println("Record deleted.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Record not found.");
        }
    }

    public void modifyRecord(BankRecord modifiedRecord) throws IOException {
        file.seek(0);
        boolean found = false;
        while (file.getFilePointer() < file.length()) {
            long pos = file.getFilePointer();
            BankRecord record = BankRecord.read(file);
            if (record.getAccountNumber() == modifiedRecord.getAccountNumber()) {
                file.seek(pos);
                modifiedRecord.write(file);
                System.out.println("Record modified.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Record not found.");
        }
    }

    public BankRecord searchRecord(int accountNumber) throws IOException {
        file.seek(0);
        while (file.getFilePointer() < file.length()) {
            BankRecord record = BankRecord.read(file);
            if (record.getAccountNumber() == accountNumber && record.getAccountNumber() != DELETED) {
                return record;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}

/**
 * BankRecord class: Represents a bank record. This class is provided and should not be modified.
 */
class BankRecord {
    private int accountNumber;
    private String customerName;
    private double balance;
    private static final int NAME_LENGTH = 30;

    public BankRecord(int accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void write(RandomAccessFile file) throws IOException {
        file.writeInt(accountNumber);
        StringBuilder nameBuilder = new StringBuilder(customerName);
        nameBuilder.setLength(NAME_LENGTH);
        for (int i = 0; i < NAME_LENGTH; i++) {
            file.writeChar(nameBuilder.charAt(i));
        }
        file.writeDouble(balance);
    }

    public static BankRecord read(RandomAccessFile file) throws IOException {
        int accNum = file.readInt();
        char[] nameChars = new char[NAME_LENGTH];
        for (int i = 0; i < NAME_LENGTH; i++) {
            nameChars[i] = file.readChar();
        }
        String name = new String(nameChars).trim();
        double bal = file.readDouble();
        return new BankRecord(accNum, name, bal);
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
               ", Customer Name: " + customerName +
               ", Balance: $" + balance;
    }
}
