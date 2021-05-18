package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {

	EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
	List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollServiceException {
		return this.employeePayrollDBService.readData();
	}

	public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollServiceException {
		int result = new EmployeePayrollDBService().updateEmployeeDataUsingStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setEmployeeSalary(salary);
	}

	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary)
			throws EmployeePayrollServiceException {
		int result = new EmployeePayrollDBService().updateEmployeePayrollDataUsingPreparedStatement(name, salary);
		System.out.println(result);
		if (result == 0)
			return;

		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setEmployeeSalary(salary);
	}

	public EmployeePayrollData getEmployeePayrollData(String name) throws EmployeePayrollServiceException {
		employeePayrollList = employeePayrollDBService.readData();
		return this.employeePayrollList.stream()
				.filter(employeePayrollObject -> employeePayrollObject.getEmployeeName().equals(name)).findFirst()
				.orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollServiceException {
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService()
				.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	public List<EmployeePayrollData> getEmployeeDataBetweenTwoDates(LocalDate startDate, LocalDate endDate)
			throws EmployeePayrollServiceException {
		employeePayrollList = employeePayrollDBService.getEmployeeDataBetweenTwoDates(startDate, endDate);
		return employeePayrollList;
	}

	public Map<String, Double> arithematicOperationByGender(String operation) throws EmployeePayrollServiceException {
		return this.employeePayrollDBService.arithematicOperationsOnEmployeePayroll(operation);
	}

	public EmployeePayrollData addNewEmployeeToPayrollDatabase(String name, String gender, double salary, LocalDate start)
			throws EmployeePayrollServiceException {
		return this.employeePayrollDBService.addEmployee(name, gender, salary, start);

	}

}
