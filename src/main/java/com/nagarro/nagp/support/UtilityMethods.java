package com.nagarro.nagp.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.stream.Stream;

import com.nagarro.nagp.users.Student;
import com.nagarro.nagp.users.Teacher;

public class UtilityMethods extends DataBaseConnectivity {

	private static final String PASSWORD_FILE_PATH = System.getProperty("user.home")
			+ "/OneDrive - Nagarro/Documents/Desktop/password.txt";

	private static final String QUERY_REGEX = "(?i)^SELECT (\\*|COUNT\\(\\*\\))? FROM (Person|Student|Teacher)(\\s+WHERE\\s+(RollNumber|employeeID)\\s*=\\s*\\d+)?$";

	private static Statement stmnt;
	private static ResultSet resultSet;
	private static ResultSet resultSet1;
	private static ResultSet resultSet2;

	protected static String getPasswordFromFile() {
		try {
			Scanner fileScanner = new Scanner(new File(PASSWORD_FILE_PATH));
			if (fileScanner.hasNextLine()) {
				return fileScanner.nextLine().trim();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Password file not found.");
		}
		return "";
	}

	/*
	 * To show menu options
	 */
	protected static void showMenu() {
		System.out.println("Select from the below Menu options:");
		System.out.println("1. Add Student Details");
		System.out.println("2. Remove Student Details");
		System.out.println("3. Add Teacher Details");
		System.out.println("4. Remove Teacher Details");
		System.out.println("5. Query Data");
		System.out.println("6. Exit");
	}

	/*
	 * To add student details
	 */ protected static void addStudentDetails(Scanner scanner) {
		System.out.println("Adding Student Details...");

		Student student = new Student();

		try {
			student.readDetails(scanner);
			student.storeDetails();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/*
	 * To remove student details based on the ID
	 */
	protected static void removeStudentDetails(Scanner scanner) {
		System.out.println("Removing Student Details...");
		String studentID;
		do {
			System.out.print("Enter Student ID : ");
			studentID = scanner.nextLine().trim();

			if (studentID.isEmpty()) {
				System.out.println("Student ID cannot be empty. Please enter a valid ID.");
				continue; // Prompt again
			}

			try {
				int studentIntID = Integer.parseInt(studentID);
				if (studentIntID < 0) {
					System.out.println("Student ID cannot be negative. Please enter a non-negative Student ID.");
					continue; // Prompt again
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Student ID must be an integer.");
				continue; // Prompt again
			}

			break; // Break the loop if student ID is valid

		} while (true);

		String sqlInsert = "DELETE FROM Student where RollNumber IN " + " (?)";
		DataBaseConnectivity.connectDatabase();

		// Create a PreparedStatement object
		try (PreparedStatement preparedStatement = DataBaseConnectivity.getCon().prepareStatement(sqlInsert)) {
			int studentIntID = Integer.parseInt(studentID);

			preparedStatement.setInt(1, studentIntID);

			// Execute the prepared statement
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Student details removed successfully!");
			} else {
				System.out.println("Student can not be removed as : " + studentIntID + " doesnt exists.");
			}
		} catch (SQLException e1) {
			System.out.println("Some SQL exception occured ");
			e1.printStackTrace();

		}
	}

	/*
	 * To add teacher details
	 */
	protected static void addTeacherDetails(Scanner scanner) {
		System.out.println("Adding Teacher Details...");

		Teacher teacher = new Teacher();
		try {
			teacher.readDetails(scanner);
			teacher.storeDetails();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * To remove student details based on employeeID
	 */
	protected static void removeTeacherDetails(Scanner scanner) {
		System.out.println("Removing Teacher Details...");
		// Read employeeID with validation

		String employeeID;
		do {
			System.out.print("Enter Teacher EmployeeID: ");
			employeeID = scanner.nextLine().trim();

			if (employeeID.isEmpty()) {
				System.out.println("Employee cannot be empty. Please enter a valid Employee ID.");
				continue; // Prompt again
			}

			try {
				int employeeIDInt = Integer.parseInt(employeeID);
				if (employeeIDInt < 0) {
					System.out.println("EmployeeID cannot be negative. Please enter a non-negative EmployeeID.");
					continue; // Prompt again
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Employee ID must be an integer.");
				continue; // Prompt again
			}

			break; // Break the loop if roll number is valid

		} while (true);

		String sqlInsert = "DELETE FROM Teacher where employeeID IN " + " (?)";

		// Create a PreparedStatement object
		DataBaseConnectivity.connectDatabase();

		try (PreparedStatement preparedStatement = DataBaseConnectivity.getCon().prepareStatement(sqlInsert)) {
			int employeeIDInt = Integer.parseInt(employeeID);

			preparedStatement.setInt(1, employeeIDInt);

			// Execute the prepared statement
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Teacher details removed successfully!");
			} else {
				System.out.println("Failed to remove Teacher details with ID " + employeeIDInt);
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}

	/*
	 * To query the data from the database system from person,student or teacher
	 * tables
	 */
	protected static void queryData(Scanner scanner) {
		System.out.println("Enter your query (e.g., SELECT * FROM Person or SELECT * FROM student where ID=123");
		String query = scanner.nextLine().trim().toLowerCase();
		if (!query.matches(QUERY_REGEX)) {
			System.out.println(
					"Invalid query format.Please enter your query in the format- SELECT * FROM Person/Student/Teacher or SELECT * FROM student/Teacher where ID=123 or SELECT count(*) FROM student/Teacher only");
			return;
		}
		System.out.println("Executing query request.Please wait... ");

		try {
			DataBaseConnectivity.connectDatabase();
			stmnt = DataBaseConnectivity.getCon().createStatement();
			String query1 = "select * from student";
			String query2 = "select * from teacher";

			if (query.equalsIgnoreCase("SELECT * FROM Person")) {
				resultSet1 = stmnt.executeQuery(query1);
				processStudentResultSet(resultSet1);
				resultSet2 = stmnt.executeQuery(query2);
				processTeacherResultSet(resultSet2);

			} else if (query.equalsIgnoreCase("SELECT * FROM Teacher")) {
				resultSet = stmnt.executeQuery(query2);

				processTeacherResultSet(resultSet);
			} else if (query.equalsIgnoreCase("SELECT * FROM Student")) {
				resultSet = stmnt.executeQuery(query1);

				processStudentResultSet(resultSet);
			} else if (query.contains("select * from student where rollnumber=")) {

				String idValue = parseQueryID(query);

				String actualQuery = "SELECT * FROM Student WHERE RollNumber = " + idValue;

				resultSet = stmnt.executeQuery(actualQuery);
				processStudentResultSet(resultSet);

			}

			else if (query.contains("select * from teacher where employeeid=")) {

				String idValue = parseQueryID(query);

				String actualQuery = "SELECT * FROM teacher WHERE employeeID = " + idValue;

				resultSet = stmnt.executeQuery(actualQuery);
				processTeacherResultSet(resultSet);

			}

			else if (query.contains("select count(*) from teacher")) {

				String actualQuery = "select count(*) from teacher";

				resultSet = stmnt.executeQuery(actualQuery);
				while (resultSet.next()) {

					System.out.println("No of teacher records are : " + resultSet.getString(1));
				}
			}
			
			else if (query.contains("select count(*) from student")) {

				String actualQuery = "select count(*) from student";

				resultSet = stmnt.executeQuery(actualQuery);
				while (resultSet.next()) {

					System.out.println("No of student records are : " + resultSet.getString(1));
				}
			}

		}

		catch (

		SQLException e) {
			System.out.println("An exception occured " + e.getMessage());

		}

	}

	/*
	 * method to get the ID value from the input query
	 */ static String parseQueryID(String query) {

		String[] parts = query.split("\\s+where\\s+", 2);
		String idClause = null;
		String[] idParts = null;
		if (parts.length > 1) {
			idClause = parts[1];
		} // If the ID clause exists, extract the ID value
		if (idClause != null) {
			idParts = idClause.split("\\s*=\\s*", 2);
			if (idParts.length > 1) {
				return idParts[1];

			}
		}
		return idParts[1];
	}

	/*
	 * To process Teacher table data using Stream
	 */
	private static void processTeacherResultSet(ResultSet resultSet) throws SQLException {
		Stream.generate(() -> {
			try {
				if (resultSet != null && resultSet.next()) {
					String id = resultSet.getString("employeeId");
					String salary = resultSet.getString("Salary");
					String bonus = resultSet.getString("Bonus");
					return String.format("Teacher employeeID: %s, Salary: %s, Bonus: %s", id, salary, bonus);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}).takeWhile(msg -> msg != null).forEach(System.out::println);
	}

	/*
	 * To process Student table data using Stream
	 */
	private static void processStudentResultSet(ResultSet resultSet) throws SQLException {

		Stream.generate(() -> {

			try {
				if (resultSet != null && resultSet.next()) {
					String id = resultSet.getString("ID");
					String rollNumber = resultSet.getString("RollNumber");
					String marks = resultSet.getString("Marks");
					return String.format("Student ID: %s, Roll Number: %s, Marks: %s", id, rollNumber, marks);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}).takeWhile(msg -> msg != null).forEach(System.out::println);
	}

}
