package com.bridgelabz.employeepayrolltest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bridgelabz.employeepayroll.EmployeePayrollData;
import com.bridgelabz.employeepayroll.EmployeePayrollService;
import com.bridgelabz.employeepayroll.EmployeePayrollServiceException;

public class EmployeePayrollDBServiceTest {
	EmployeePayrollService employeePayrollService = new EmployeePayrollService();

	@Test
	public void givenEmployeePayrollDB_WhenRetrieve_ShouldReturnSize() throws EmployeePayrollServiceException {
		List<EmployeePayrollData> employeePayrollData;
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryOfEmployee_WhenUpdated_ShouldSyncWithDatabase() throws EmployeePayrollServiceException {
		employeePayrollService.updateEmployeeSalary("Bill", 300000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Bill");
		Assert.assertTrue(result);
	}

	@Test
	public void givenNewSalaryOfEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDatabase()
			throws EmployeePayrollServiceException {
		employeePayrollService.updateEmployeeSalaryUsingPrepareStatement("Bill", 300000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Bill");
		Assert.assertTrue(result);
	}

}
