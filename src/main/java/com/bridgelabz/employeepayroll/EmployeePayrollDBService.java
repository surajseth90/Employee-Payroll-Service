package com.bridgelabz.employeepayroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

	private PreparedStatement preparedStatement;

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
		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate joiningDate = result.getDate("start").toLocalDate();
				employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, joiningDate));
			}
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.EMPLOYEE_DATA_RETRIEVE_ISSUE,
					"Unable to retrieve data from the data base!!");
		}
		return employeePayrollDataList;
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
			e.printStackTrace();
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}
	}

	private void preparedStatementForRetrieveDataUsingName() throws EmployeePayrollServiceException {
		try {
			Connection connection = this.getConnection();
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
			this.prepareStatementForUpdateSalaryInEmployeePayroll();
		}
		try {
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}
	}

	private void prepareStatementForUpdateSalaryInEmployeePayroll() throws EmployeePayrollServiceException {
		try {
			Connection connection = this.getConnection();
			String sql = "UPDATE employee_payroll SET salary=? WHERE name=?";
			this.preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.PREPARE_STATEMENT_ISSUE,
					"Unable to get data by prepared statement");
		}

	}

	public List<EmployeePayrollData> getEmployeeDataBetweenTwoDates(LocalDate startDate, LocalDate endDate)
			throws EmployeePayrollServiceException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		String sql = String.format(
				"SELECT * FROM employee_payroll WHERE start BETWEEN cast('%s' as date) and cast('%s' as date);",
				startDate.toString(), endDate.toString());
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
					EmployeePayrollServiceException.EmployeePayrollExceptionType.EMPLOYEE_DATA_RETRIEVE_ISSUE,
					"Unable to get data");
		}

	}

	public Map<String, Double> arithematicOperationsOnEmployeePayroll(String operation)
			throws EmployeePayrollServiceException {
		Map<String, Double> arithematicOpertionMap = new HashMap<>();
		String sql = String.format("SELECT Gender, %s(salary) FROM employee_payroll GROUP BY Gender;", operation);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("Gender");
				Double salary = resultSet.getDouble(2);
				arithematicOpertionMap.put(gender, salary);
			}
			return arithematicOpertionMap;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.ARITHMETIC_OPERATION_ISSUE,
					"Unable to perform arithematic operation");
		}

	}

	public EmployeePayrollData addEmployee(String name, String gender, double salary, LocalDate start)
			throws EmployeePayrollServiceException {
		int employeeId = -1;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format(
				"INSERT INTO employee_payroll(name,Gender,salary,start) " + "VALUES ( '%s', '%s', %s, '%s' )", name,
				gender, salary, Date.valueOf(start));
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(employeeId, name, gender, salary, start);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.ADDING_NEW_EMPLOYEE_ISSUE,
					"Unable to Add new Employee!!");
		}
		return employeePayrollData;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate start)
			throws EmployeePayrollServiceException {
		int employeeID = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollServiceException(
					EmployeePayrollServiceException.EmployeePayrollExceptionType.ADDING_NEW_EMPLOYEE_ISSUE,
					"Unable to Add new Employee!!");
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee_payroll(name,Gender,salary,start) " + "VALUES ( '%s', '%s', %s, '%s' )", name,
					gender, salary, Date.valueOf(start));
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeID = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try (Statement statement = connection.createStatement()) {
			double deduction = salary * 0.2;
			double taxablePay = salary - deduction;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format("INSERT INTO payroll_details "
					+ "(id, basicPay, deduction, taxableAmount, tax , netPay) VALUES "
					+ "(%s, %s, %s, %s, %s, %s)", employeeID, salary, deduction, taxablePay, tax, netPay);
			int rowsAffected = statement.executeUpdate(sql);
			if (rowsAffected == 1) {
				employeePayrollData = new EmployeePayrollData(employeeID, name, gender, salary, start);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return employeePayrollData;
	}
}
