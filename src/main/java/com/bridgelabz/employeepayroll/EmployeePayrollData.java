package com.bridgelabz.employeepayroll;

import java.time.LocalDate;

public class EmployeePayrollData {

	private int employeeID;
	private String employeeName;
	private double employeeSalary;
	private LocalDate employeeJoiningDate;

	public EmployeePayrollData(int employeeID, String employeeName, double employeeSalary,
			LocalDate employeeJoiningDate) {
		super();
		this.employeeID = employeeID;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
		this.employeeJoiningDate = employeeJoiningDate;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public double getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(double employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

	public LocalDate getEmployeeJoiningDate() {
		return employeeJoiningDate;
	}

	public void setEmployeeJoiningDate(LocalDate employeeJoiningDate) {
		this.employeeJoiningDate = employeeJoiningDate;
	}

	@Override
	public String toString() {
		return "EmployeePayroll [employeeID=" + employeeID + ", employeeName=" + employeeName + ", employeeSalary="
				+ employeeSalary + ", employeeJoiningDate=" + employeeJoiningDate + "]";
	}

}
