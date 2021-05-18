package com.bridgelabz.employeepayroll;

import java.util.List;

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
	
	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) throws EmployeePayrollServiceException {
		int result = new EmployeePayrollDBService().updateEmployeePayrollDataUsingPreparedStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setEmployeeSalary(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
						.filter(employeePayrollObject -> employeePayrollObject.getEmployeeName().equals(name))
						.findFirst()
						.orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollServiceException {
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService().getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(1).equals(getEmployeePayrollData(name));
	}		
}
