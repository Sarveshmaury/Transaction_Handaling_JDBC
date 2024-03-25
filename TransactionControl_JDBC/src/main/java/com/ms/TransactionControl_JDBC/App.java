package com.ms.TransactionControl_JDBC;
import java.sql.*;
import java.util.Scanner;

public class App 
{
	private static final String url="jdbc:mysql://localhost:3306/transactioncontrol";
	private static final String username="root";
	private static final String password="Sa@7505286528";
	
	
    public static void main( String[] args ){
	try {
	    		
	        	Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch(ClassNotFoundException e) {
	        	//e.printStackTrace();
	        	System.out.println(e.getMessage());
	        }
	       transaction();
	    }
    
    public static void transaction() {
    	try {
    		Connection connection =DriverManager.getConnection(url,username,password);
    		connection.setAutoCommit(false);
    		
    		String debit_query="update Accounts set balance=balance - ? where accountNumber=?";
    		String credit_query="update Accounts set balance=balance + ? where accountNumber=?";
    		PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
    		PreparedStatement creditPreparedStatement =connection.prepareStatement(credit_query);
    		
    		Scanner sc= new Scanner(System.in);
    		System.out.print("Enter the Amount : ");
    		double amount=sc.nextDouble();
    		
    		System.out.print("Enter account number : ");
    		int acc=sc.nextInt();
    		
    		System.out.print("Enter account number where you want to send : ");
    		int cacc=sc.nextInt();
    		
    		debitPreparedStatement.setDouble(1, amount);
    		debitPreparedStatement.setInt(2, acc);
    		
    		creditPreparedStatement.setDouble(1, amount);
    		creditPreparedStatement.setInt(2, cacc);
    		
    		debitPreparedStatement.executeUpdate();
    	    creditPreparedStatement.executeUpdate();
    		
    		if(isSufficient(connection ,acc,amount)) {
    			connection.commit();
    			System.out.println("Transaction successful !!!");
    			
    			
    		}
    		else {
    			connection.rollback();
    			System.out.println("Transaction Failed !!!");
    		}
    		
    		
    		
    	} catch(SQLException e) {
    		System.out.println(e.getMessage());
    	}
    }
    static boolean isSufficient(Connection connection , int accountNumber,double amount ) {
    	try {
    		String query="select balance from Accounts where accountNumber = ?";
    		PreparedStatement preparedStatement=connection.prepareStatement(query);
    		preparedStatement.setInt(1,accountNumber);
    		
    		ResultSet resultSet=preparedStatement.executeQuery();
    		
    		if(resultSet.next()) {
    			double current_balance=resultSet.getDouble("balance");
    			if(amount>current_balance) {
    				return false;
    			} 
    			else {
    				return true;
    			}
    		}
    	} catch(SQLException e) {
    		System.out.println(e.getMessage());
    	}
		return false;
    	
    }
}
