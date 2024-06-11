package com.nagarro.nagp.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nagarro.nagp.support.DataBaseConnectivity;

public class Teacher extends Person {
	private String employeeId;
	private String salary;
	private String bonus;

	List<String> tlist = new ArrayList<>(); // create an array list to store teacher details

	@Override
	public void readDetails(Scanner scanner) {
		super.readDetails(scanner);
		do {
			System.out.print("Enter Employee ID: "); // employee ID can have numbers and characetrs also but should not
														// be atelast empty
			employeeId = scanner.nextLine().trim();

			try {
				if (employeeId.isEmpty()) {
					System.out.println("Employee ID  cannot be empty. Please enter a valid Employee ID.");
					continue;
				}

				else if (Integer.parseInt(this.employeeId) < 0) {
					System.out
							.println("Employee ID  cannot be negative. Please enter a valid non-negative Employee ID.");
					continue;
				} else {

					//tlist.add(employeeId);
				}

			}

			catch (NumberFormatException nfe) {

				System.out.println("Invalid EmployeeID,please enter a valid alphanumeric,non-negative,non-blank ID.");
				continue;
			}

			// now check whether the entered emplyeeID already exists or not

			String sqlQuery = "SELECT employeeID FROM Teacher WHERE employeeID = ?";

			DataBaseConnectivity.connectDatabase();

			try (PreparedStatement checkStatement = DataBaseConnectivity.getCon().prepareStatement(sqlQuery)) {
				checkStatement.setString(1, this.employeeId);
				ResultSet resultSet = checkStatement.executeQuery();
 				if (resultSet != null && resultSet.next()) {
					// employeeId already exists
					System.out.println("Employee ID already exists in the database. Please enter another employeeId.");
					continue; // Prompt again
				}

				else {
					tlist.add(employeeId);

					break; // break the loop if employee ID is valid

				}
			} catch (SQLException e) {
				System.out.println("Error occurred while checking employeeId existence: " + e.getMessage());
				return; // Exit method
			}

		} while (true);

		// Read salary with validation
		do {
			System.out.print("Enter salary: ");
			salary = scanner.nextLine().trim();

			// Check for empty value
			if (salary.isEmpty()) {
				System.out.println("Salary cannot be empty. Please enter a valid salary.");
				continue;
			}

			// Check for special characters
			else if (!salary.matches("^\\d*\\.?\\d*$")) {
				System.out.println("Invalid input. Salary must be a valid numeric value.");
				continue;
			}

			float enteredSalary = Float.parseFloat(salary);

			if (enteredSalary <= 0) {
				System.out.println("Salary must be greater than zero. Please enter a valid salary.");
				continue;
			} else {

				tlist.add(salary);
				break; // Exit the loop if the input is valid

			}

		} while (true);

		// Read Bonus with validation
		do {
			System.out.print("Enter Bonus: ");
			bonus = scanner.nextLine().trim();

			// Check for empty value
			if (bonus.isEmpty()) {
				System.out.println("Bonus cannot be empty. Please enter a valid bonus.");
				continue;
			}

			// Check for special characters
			else if (!bonus.matches("^\\d*\\.?\\d*$")) {
				System.out.println("Invalid input. Bonus must be a valid numeric value.");
				continue;
			}

			float enteredBonus = Float.parseFloat(bonus);

			if (enteredBonus <= 0) {
				System.out.println("Bonus must be greater than zero. Please enter a valid bonus.");
				continue;
			} else {
				tlist.add(bonus);
				break; // Exit the loop if the input is valid

			}

		} while (true);

 	}

	/*
	 * Method to store the teacher details in the database
	 */
	@Override
	public void storeDetails() throws SQLException, ClassNotFoundException {
		super.storeDetails();

		String sqlInsert = "INSERT INTO Teacher ( employeeId, Salary,Bonus) " + "VALUES (?,?,?)";
		DataBaseConnectivity.connectDatabase();

		// Create a PreparedStatement object
		try (PreparedStatement preparedStatement = DataBaseConnectivity.getCon().prepareStatement(sqlInsert)) {
			// Set values for each placeholder
			preparedStatement.setString(1, tlist.get(0));
			preparedStatement.setFloat(2, Float.parseFloat(tlist.get(1)));
			preparedStatement.setFloat(3, Float.parseFloat(tlist.get(2)));

			// Execute the prepared statement
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Teacher details added successfully!");
			} else {
				System.out.println("Failed to add Teacher details.");
			}

		} catch (SQLException e) {
			System.out.println("Error occurred while inserting records: " + e.getMessage());
		}
	}
}
