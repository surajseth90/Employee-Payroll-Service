package com.bridgelabz.employeepayroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

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

	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws EmployeePayrollServiceException {
		String sql = String.format("SELECT * FROM employee_payroll WHERE name='%s'", name);
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
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
					EmployeePayrollServiceException.EmployeePayrollExceptionType.UPDATION_ISSUE,
					"Unable to get data from database");
		}
	}

}
