package com.nagarro.nagp.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.nagarro.nagp.support.DataBaseConnectivity;

class Person {
	private String firstName;
	private String middleName;
	private String lastName;
	private String contactNumber;
	private String address;

	List<String> plist = new ArrayList<>(); // create an array list to store person details

	public void readDetails(Scanner scanner) {
		// Read first name with validation
		do {
			System.out.print("Enter first name: ");
			firstName = scanner.nextLine().trim();

			if (firstName.isEmpty()) {
				System.out.println("First name cannot be empty. Please enter a valid first name.");
				continue; // Prompt again
			} else if (firstName.contains(" ")) {
				System.out.println("First name cannot have spaces.");
				continue; // Prompt again
			}

			else if (!firstName.matches("[a-zA-Z]+")) {
				System.out.println("Invalid input. First name must contain only letters.");
				continue; // Prompt again
			} else {
				plist.add(firstName);
				break; // Break the loop if first name is valid

			}

		} while (true);

		// Read middle name (Its not a mandatory field so empty value is allowed)
		do {
			System.out.print("Enter middle name: ");
			middleName = scanner.nextLine().trim();

			if (middleName.isEmpty()) {
				plist.add(middleName);

				break; // Dont Prompt again as middle name can be empty
			}

			else if (!middleName.matches("[a-zA-Z]+")) {
				System.out.println("Invalid input. Middle name must contain only letters.");
				continue; // Prompt again
			} else if (middleName.contains(" ")) {
				System.out.println("Middle name cannot have spaces.");
				continue; // Prompt again
			} else {

				plist.add(middleName);
				break; // Break the loop if middle name is valid

			}

		} while (true);

		// Read last name with validation
		do {
			System.out.print("Enter last name: ");
			lastName = scanner.nextLine().trim();

			if (lastName.isEmpty()) {
				System.out.println("Last name cannot be empty. Please enter a valid Last name.");
				continue; // Prompt again
			}

			else if (!lastName.matches("[a-zA-Z]+")) {
				System.out.println("Invalid input. Last name must contain only letters.");
				continue; // Prompt again
			} else if (lastName.contains(" ")) {
				System.out.println("Last name cannot have spaces.");
				continue; // Prompt again
			}

			else {

				plist.add(lastName);
				break; // Break the loop if last name is valid

			}

		} while (true);

		// Read contact number with validation
		do {
			System.out.print("Enter contact number: ");
			contactNumber = scanner.nextLine().trim();
			if (!contactNumber.matches("\\d{10}")) {
				System.out.println("Invalid contact number format. Please enter a 10-digit number.");
			}

			else {

				plist.add(contactNumber);
			}
		} while (!contactNumber.matches("\\d{10}"));

		// Read address (no validation specified)
		
		 
		System.out.print("Enter address: ");
		address = scanner.nextLine().trim();
		 
		plist.add(address); 
		
	 

	}

	/*
	 * Method to store the person details in the database
	 */ public void storeDetails() throws SQLException, ClassNotFoundException {

		String sqlInsert = "INSERT INTO Person ( FirstName, MiddleName,LastName,ContactNumber,Address ) "
				+ "VALUES (?, ?,?,?,?)";
		DataBaseConnectivity.connectDatabase();

		// Create a PreparedStatement object
		try (PreparedStatement preparedStatement = DataBaseConnectivity.getCon().prepareStatement(sqlInsert)) {
			// Set values for each placeholder
			preparedStatement.setString(1, plist.get(0));
			preparedStatement.setString(2, plist.get(1));
			preparedStatement.setString(3, plist.get(2));
			preparedStatement.setString(4, plist.get(3));
			preparedStatement.setString(5, plist.get(4));

			// Execute the prepared statement
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Person details added successfully!");
			} else {
				System.out.println("Failed to add Person details.");
			}

		}

		catch (SQLServerException sse) {

			System.out.println("Some error occured " + sse.getMessage());
		}
		 
	}

}