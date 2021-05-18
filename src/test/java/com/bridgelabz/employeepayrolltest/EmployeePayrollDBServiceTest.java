package com.bridgelabz.employeepayrolltest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
		employeePayrollService.updateEmployeeSalary("Bill", 300000);
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

	@Test
	public void givenEmployeePayrollData_WhenRetrievedBasedOnStartDate_ShouldReturnResult()
			throws EmployeePayrollServiceException {
		employeePayrollService.readEmployeePayrollData();
		LocalDate startDate = LocalDate.parse("2021-04-07");
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> list = employeePayrollService.getEmployeeDataBetweenTwoDates(startDate, endDate);
		Assert.assertEquals(list.get(0), employeePayrollService.getEmployeePayrollData("Suraj"));
	}
	
	@Test
	public void givenEmployeeData_WhenPerformArithematicOperation_ShouldReturnResult() throws EmployeePayrollServiceException {
		Map<String ,Double> result= employeePayrollService.arithematicOperationByGender("SUM");
		Assert.assertEquals(1200000,result.get("M"),0.0);
	}
}
