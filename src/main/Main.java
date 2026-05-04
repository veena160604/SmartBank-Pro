package main;

import service.BankService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        BankService bank = new BankService();
        bank.initDB();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Create User\n2.Create Account\n3.Deposit\n4.Withdraw\n5.Transfer\n6.Exit");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Email: ");
                    String email = sc.next();
                    System.out.print("Password: ");
                    String pass = sc.next();
                    bank.createUser(name, email, pass);
                    break;

                case 2:
                    System.out.print("User ID: ");
                    bank.createAccount(sc.nextInt());
                    break;

                case 3:
                    System.out.print("Account ID: ");
                    int acc = sc.nextInt();
                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    bank.deposit(acc, amt);
                    break;

                case 4:
                    System.out.print("Account ID: ");
                    bank.withdraw(sc.nextInt(), sc.nextDouble());
                    break;

                case 5:
                    System.out.print("From: ");
                    int f = sc.nextInt();
                    System.out.print("To: ");
                    int t = sc.nextInt();
                    System.out.print("Amount: ");
                    double a = sc.nextDouble();
                    bank.transfer(f, t, a);
                    break;

                case 6:
                    System.exit(0);
            }
        }
    }
}
