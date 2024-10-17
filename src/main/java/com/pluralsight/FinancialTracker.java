package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName)
    {
        File file = new File(fileName);
        String line;

        if (file.exists())
        {
            try
            {
                FileReader reader = new FileReader(fileName);
                BufferedReader buffer = new BufferedReader(reader);



                while ((line = buffer.readLine()) != null)
                {
                    System.out.println(line);
                    String[] tokens = line.split("\\|");
                    LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                    String description = tokens[2];
                    String vendor = tokens[3];
                    double amount = Double.parseDouble(tokens[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            } catch (Exception e)
            {
                System.err.println("Error processing file");
                e.printStackTrace();
            }
        } else
        {
            System.out.println("File does not exist. Creating new file: " + fileName);
            try
            {
                File myFile = new File(fileName);
                myFile.createNewFile();
            }
            catch (Exception e) {
                System.err.println("Error creating file");
                e.printStackTrace();
            }

        }

            // This method should load transactions from a file with the given file name.
            // If the file does not exist, it should be created.
            // The transactions should be stored in the `transactions` ArrayList.
            // Each line of the file represents a single transaction in the following format:
            // <date>|<time>|<description>|<vendor>|<amount>
            // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
            // After reading all the transactions, the file should be closed.
            // If any errors occur, an appropriate error message should be displayed.

    }

    private static void addDeposit(Scanner scanner) {
        try {
            System.out.println("Please enter the date: ");
            LocalDate currentDate = LocalDate.parse(scanner.nextLine().formatted(DATE_FORMAT));
            System.out.println("Please enter the current time: ");
            LocalTime currentTime = LocalTime.parse(scanner.nextLine().formatted(TIME_FORMAT));
            System.out.println("Please enter a description of deposit: ");
            String depositDescription = scanner.nextLine();
            System.out.println("Please enter the vendor:");
            String depositVendor = scanner.nextLine();
            System.out.println("Please enter the amount you would like to deposit: ");
            double depositAmount = scanner.nextDouble();
           scanner.nextLine();
            if (depositAmount <= 0)
            {
                System.out.println("The amount must be a positive value. Would you like to proceed?  Y/N");
                if (scanner.nextLine().equalsIgnoreCase("Y"))
                {
                    Math.abs(depositAmount);
                } else if (scanner.nextLine().equalsIgnoreCase("N"))
                {
                    return;
                }
            }
            Transaction deposit = new Transaction(currentDate, currentTime, depositDescription, depositVendor, depositAmount);
            transactions.add(deposit);
            System.out.println("Deposit succsessful!");

            FileWriter writer = new FileWriter(FILE_NAME ,true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

//Delete transaction and create toString method in Transaction class, separating by pipes
            bufferedWriter.write(deposit.toString());
            bufferedWriter.close();


        }
        catch (Exception e)
        {
            System.err.println("Invalid input");
            e.printStackTrace();
        }


        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
        try
        {
            System.out.println("Please enter the date: ");
            LocalDate currentDate = LocalDate.parse(scanner.nextLine().formatted(DATE_FORMAT));
            System.out.println("Please enter the current time: ");
            LocalTime currentTime = LocalTime.parse(scanner.nextLine().formatted(TIME_FORMAT));
            System.out.println("Please enter a description of deposit: ");
            String paymentDescription = scanner.nextLine();
            System.out.println("Please enter the vendor:");
            String paymentVendor = scanner.nextLine();
            System.out.println("Please enter the amount you would like to deposit: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();
            if (paymentAmount >= 0)
            {
                System.out.println("The amount must be a negative value. Would you like to proceed? Y/N");

                if (scanner.nextLine().equalsIgnoreCase("Y"))
                {

                    paymentAmount *= -1;
                }
                else if (scanner.nextLine().equalsIgnoreCase("N"))
                {
                    return;
                }

            }
            Transaction payment = new Transaction(currentDate,currentTime, paymentDescription, paymentVendor, paymentAmount);
            transactions.add(payment);
            FileWriter writer = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(payment.toString());
            bufferedWriter.close();
        }
        catch (Exception e)
        {
            System.err.println("Invalid input");
            e.printStackTrace();
        }

        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
    }

    private static void ledgerMenu(Scanner scanner){
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {

        for (int i = 0; i < transactions.size(); i++)
        {
            System.out.println("Date      " + "|" + "Time         " + "|" + "Description                      " + "|" + "Vendor                    " + "|" + "Amount          ");
            System.out.println(transactions.get(i));
        }

        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayDeposits()
    {
        for (Transaction deposit : transactions)
        {
            System.out.println("Date      " + "|" + "Time         " + "|" + "Description                      " + "|" + "Vendor                    " + "|" + "Amount          ");
            System.out.println(deposit);
        }

        }
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.


    private static void displayPayments()
    {

       for (Transaction payment : transactions)
        {
            //if amount is negative
            System.out.println("Date      " + "|" + "Time         " + "|" + "Description                      " + "|" + "Vendor                    " + "|" + "Amount          ");
            System.out.println(payment);
        }
    }
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate monthToDate = LocalDate.now();
                    LocalDate md1 = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(md1, monthToDate);
                    break;
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    LocalDate previousMonth = LocalDate.now().minusMonths(1);
                    LocalDate monthLength = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());
                    filterTransactionsByDate(previousMonth,monthLength);
                    break;
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    LocalDate yearToDate = LocalDate.now();
                    LocalDate yr1 = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(yr1,yearToDate);
                    break;
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    LocalDate previousYear = LocalDate.now().minusYears(1);
                    LocalDate yearLength = LocalDate.now().withDayOfYear(previousYear.lengthOfYear());
                    filterTransactionsByDate(previousYear,yearLength);
                    break;
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    System.out.println("Enter a vendor: ");
                    String selectedVendor = scanner.nextLine();
                    filterTransactionsByVendor(selectedVendor);
                    break;
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {

        for (Transaction transaction : transactions)
        {

            LocalDate transactionDate = transaction.getDate();
            if (startDate == null  || endDate == null)
                {
                    System.out.println("Invalid date");
                    return;

                }else if (transactionDate.isBefore(endDate) && transactionDate.isAfter(startDate.minusDays(1)))
                {
                    System.out.printf("%-11s|%-9s|%-28s|%-20s|%-10.2f",
                            transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

                }else
                {
                    System.out.println("Transactions not in specified range");
                    return;
                }


        }

        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        for (Transaction transaction : transactions) {
            String theVendor = transaction.getVendor();

            if (vendor.equals(theVendor)) {
                System.out.printf("%-11s", "%-9s", "%-28s", "%-20s", "%-10.2f",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            } else {
                System.out.println("There are no results in this range");
            }

        }

    }
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.




}
