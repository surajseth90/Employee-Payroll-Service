package com.bridgelabz.employeepayroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement preparedStatement;

	public EmployeePayrollDBService() {

	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false";
		String userName = "root";
		String password = "Surajmysql@90";
		Connection connectionn;
		System.out.println("Connecting to database: " + jdbcURL);
		connectionn = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is successfull!! " + connectionn);
		return connectionn;
	}

	public List<EmployeePayrollData> readData() throws EmployeePayrollServiceException {
		String sql = "SELECT * FROM  employee_payroll;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate joiningDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, joiningDate));
			}
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.EMPLOYEE_DATA_RETRIEVE_ISSUE,
					"Unable to retrieve data from the data base!!");
		}
		return employeePayrollList;
	}

	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws EmployeePayrollServiceException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		if (this.preparedStatement == null) {
			this.preparedStatementForRetrieveDataUsingName();
		}
		try {
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String Employeename = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, Employeename, salary, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}
	}

	private void preparedStatementForRetrieveDataUsingName() throws EmployeePayrollServiceException {
		try (Connection connection = this.getConnection()) {
			String sql = "SELECT * FROM employee_payroll WHERE name=?";
			this.preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}
	}

	public int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollServiceException {
		String sql = String.format("UPDATE employee_payroll SET salary=%.2f WHERE name='%s'", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql);
			return rowsAffected;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.UPDATION_ISSUE,
					"Unable To update data in database");
		}
	}

	public int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary)
			throws EmployeePayrollServiceException {
		if (this.preparedStatement == null) {
			this.prepareStatementForEmployeePayroll();
		}
		try {
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}
	}

	private void prepareStatementForEmployeePayroll() throws EmployeePayrollServiceException {
		try (Connection connection = this.getConnection()) {
			String sql = "UPDATE employee_payroll SET salary=? WHERE name=?";
			this.preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}

	}

}
