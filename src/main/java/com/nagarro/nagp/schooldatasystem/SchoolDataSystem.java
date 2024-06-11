package com.nagarro.nagp.schooldatasystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.nagarro.nagp.support.UtilityMethods;

public class SchoolDataSystem extends UtilityMethods {

	private static final int MAX_ATTEMPTS = 4;
	public static PreparedStatement preparedStatement = null;
	public static Statement stmnt;
	public static ResultSet resultSet;

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);

		try {
			int attempts = 0;

			// Load password from file
			String password = getPasswordFromFile();

			// if password is empty-throw error
			if (password.isEmpty()) {
				System.out.println("Error: Password file has no password.");
				return;
			}

			// Ask for password
			while (true) {
				System.out.print("Please enter the password: ");
				String input = scanner.nextLine();
				if (input.equals(password)) {
					System.out.println("Welcome to School Data Simple Query System.");
					
					showMenu(); // now-show menu options
					
					break;
				} else {
					attempts++;
					int attemptsLeft = MAX_ATTEMPTS - attempts;
					if (attemptsLeft == 0) {
						System.out.println("You have tried too many incorrect attempts. Exiting.");
						return;  // in case of full 4 attempts, exits from the while loop
					}
					System.out.println("Incorrect password. You have " + attemptsLeft + " attempts left.");
				}
			}

			// Implementation of  menu options 
			while (true) {
	            System.out.print("Please enter your choice: ");
	            String input = scanner.nextLine().trim(); // Read input as a string and trim leading and trailing whitespace

	            if (input.isEmpty()) {
	                System.out.println("Invalid input. Choice cannot be blank.");
	                continue; // Prompt again
	            }

	            if (!input.matches("\\d+")) {
	                System.out.println("Invalid input. Choice must be an integer from 1 to 6");
	                continue; // Prompt again
	            }

	            int choice = Integer.parseInt(input); // parse the string choice to int and use in switch

	            switch (choice) {
	                case 1:
	                    addStudentDetails(scanner);
	                    break;
	                case 2:
	                    removeStudentDetails(scanner);
	                    break;
	                case 3:
	                    addTeacherDetails(scanner);
	                    break;
	                case 4:
	                    removeTeacherDetails(scanner);
	                    break;
	                case 5:
	                    queryData(scanner);
	                    break;
	                case 6:
	                    System.out.println("Exiting...Good Bye !!");
	                    return;
	                default:
	                    System.out.println("Invalid choice. Please choose from 1 to 6.");
	            }
	        }
		} finally {
			scanner.close(); // Close the scanner
		}
	}
}
