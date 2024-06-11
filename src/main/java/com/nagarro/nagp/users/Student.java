package com.nagarro.nagp.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nagarro.nagp.support.DataBaseConnectivity;

public class Student extends Person {
	private String rollNumber;
	private String marks;

	List<String> slist = new ArrayList<>(); // create an array list to store student details

	@Override
	public void readDetails(Scanner scanner) {

		super.readDetails(scanner);
		/*
		 * Roll no input and validations
		 */
		do {
			System.out.print("Enter Roll No: ");
			rollNumber = scanner.nextLine().trim();
			try {
				if (rollNumber.isEmpty()) {
					System.out.println("Roll Number cannot be empty. Please enter a valid ROll Number.");
					continue; // Prompt again
				} else if (Integer.parseInt(rollNumber) < 0) {

					System.out.println("Roll Number cannot be negative. Please enter a non-negative Roll Number.");
					continue; // Prompt again
				}
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Roll Number must be an integer.");
				continue; // Prompt again
			}

			// now check whether the entered roll no already exists or not
			String sqlQuery = "SELECT RollNumber FROM Student WHERE RollNumber = ?";

			DataBaseConnectivity.connectDatabase();

			try (PreparedStatement checkStatement = DataBaseConnectivity.getCon().prepareStatement(sqlQuery)) {
				checkStatement.setString(1, this.rollNumber);
				ResultSet resultSet = checkStatement.executeQuery();

				if (resultSet != null && resultSet.next()) {
					// Roll number already exists
					System.out.println(
							"Student roll number already exists in the database. Please enter another roll number.");
					continue; // Prompt again
				}

				else {

					slist.add(rollNumber);

					break; // Break the loop if roll number is valid
				}
			} catch (SQLException e) {
				System.out.println("Error occurred while checking roll number existence: " + e.getMessage());
				return; // Exit method
			}

		} while (true);

		/*
		 * Marks input and validations
		 */ do {
			System.out.print("Enter Marks: ");
			try {
				marks = scanner.nextLine().trim();
				if (marks.isEmpty()) {
					System.out.println("Marks cannot be empty. Please enter valid marks");
					continue; // Prompt again

				} else if (Integer.parseInt(marks) < 0) {
					System.out.println("Marks cannot be negative. Please enter the non-negative marks.");
					continue; // Prompt again
				}

				else {
					slist.add(marks);
					break; // Break the loop if roll number is valid

				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Marks must be an integer.");
				continue; // Prompt again
			}

		} while (true);

	}

	/*
	 * to store student details in the database
	 */ @Override
	public void storeDetails() throws SQLException, ClassNotFoundException {
		super.storeDetails();

		String sqlInsert = "INSERT INTO Student (RollNumber, Marks) VALUES (?, ?)";

		DataBaseConnectivity.connectDatabase();

		// Roll number doesn't exist, proceed with insertion
		try (PreparedStatement insertStatement = DataBaseConnectivity.getCon().prepareStatement(sqlInsert)) {
			insertStatement.setString(1, slist.get(0));
			insertStatement.setNString(2, slist.get(1));

			int rowsAffected = insertStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Student details added successfully!");
			} else {
				System.out.println("Failed to add student details.");
			}
		} catch (SQLException e) {
			System.out.println("Error occurred while inserting records: " + e.getMessage());
		}

		finally {

			DataBaseConnectivity.getCon().close();
		}
	}
}
