package service;

import util.DBConnection;
import java.sql.*;

public class BankService {

    public void initDB() throws Exception {
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();

        st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, role TEXT)");
        st.execute("CREATE TABLE IF NOT EXISTS accounts (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, balance REAL)");
        st.execute("CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, from_account INTEGER, to_account INTEGER, amount REAL, type TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    public void createUser(String name, String email, String password) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)"
        );
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, "USER");
        ps.executeUpdate();
    }

    public void createAccount(int userId) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO accounts(user_id,balance) VALUES(?,0)"
        );
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    public void deposit(int accountId, double amount) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "UPDATE accounts SET balance = balance + ? WHERE id = ?"
        );
        ps.setDouble(1, amount);
        ps.setInt(2, accountId);
        ps.executeUpdate();
    }

    public void withdraw(int accountId, double amount) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement check = con.prepareStatement(
            "SELECT balance FROM accounts WHERE id=?"
        );
        check.setInt(1, accountId);
        ResultSet rs = check.executeQuery();

        if (rs.next() && rs.getDouble("balance") >= amount) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE id = ?"
            );
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    public void transfer(int fromId, int toId, double amount) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            withdraw(fromId, amount);
            deposit(toId, amount);
            con.commit();
        } catch (Exception e) {
            con.rollback();
        }
    }
}
