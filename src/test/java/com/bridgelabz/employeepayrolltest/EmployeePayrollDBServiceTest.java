package com.bridgelabz.employeepayrolltest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import com.bridgelabz.employeepayroll.EmployeePayrollData;
import com.bridgelabz.employeepayroll.EmployeePayrollService;
import com.bridgelabz.employeepayroll.EmployeePayrollServiceException;

public class EmployeePayrollDBServiceTest {

	@Test
	public static void givenEmployeePayrollDB_WhenRetrieve_ShouldReturnSize() throws EmployeePayrollServiceException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData;
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		Assert.assertEquals(3, employeePayrollData.size());
	}

}
