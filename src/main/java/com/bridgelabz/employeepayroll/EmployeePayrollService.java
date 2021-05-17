package com.bridgelabz.employeepayroll;

import java.util.List;

public class EmployeePayrollService {

	EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();

	public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollServiceException {
		return this.employeePayrollDBService.readData();
	}

}
