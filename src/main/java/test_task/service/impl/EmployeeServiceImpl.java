package test_task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_task.dao.EmployeeDao;
import test_task.model.Employee;
import test_task.service.EmployeeService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeDao employeeDao;

    @Override
    public List<Employee> findAllBySalaryGreaterThatBoss() {
        return employeeDao.findAllWhereSalaryGreaterThatBoss();
    }

    @Override
    public List<Employee> findAllByMaxSalary() {
        return employeeDao.findAllByMaxSalary();
    }

    @Override
    public List<Employee> findAllWithoutBoss() {
        return employeeDao.findAllWithoutBoss();
    }

    @Override
    public Long fireEmployee(String name) {
        Iterable<Employee> employees = employeeDao.findAll();

        // Implement method using Collection

        Iterator<Employee> employeeIterator = employees.iterator();
        Optional<Employee> employeeToRemove = Optional.empty();
        while (employeeIterator.hasNext()){
            Employee employee = employeeIterator.next();
            if (employee.getName().equals(name)){
                employeeToRemove = Optional.of(employee);
                employeeIterator.remove();
                break;
            }
        }
        // I haven't found a way to delete employee using only Collection.
        // saveAll method used for insert or update. It is possible to delete an object with saveAll
        // using "soft delete", but for this you need to change the entity class.
        employeeToRemove.ifPresent(e -> employeeDao.delete(e));


        employeeDao.saveAll(employees);
        return employeeToRemove.isPresent() ? employeeToRemove.get().getId() : 0L;
    }

    @Override
    public Long changeSalary(String name) {
        Iterable<Employee> employees = employeeDao.findAll();

        // Implement method using Collection

        BigDecimal salaryFactor = new BigDecimal(2);
        Optional<Employee> optionalEmployee = Optional.empty();

        for (Employee iterator : employees) {
            if (iterator.getName().equals(name)) {
                optionalEmployee = Optional.of(iterator);
                break;
            }
        }
        optionalEmployee.ifPresent(e -> e.setSalary(e.getSalary().multiply(salaryFactor)));

        employeeDao.saveAll(employees);
        return optionalEmployee.isPresent() ? optionalEmployee.get().getId() : 0L;
    }

    @Override
    public Long hireEmployee(Employee employee) {
        // Implement method using Collection and DAO

        Iterable<Employee> employees = employeeDao.findAll();

        List<Employee> employeeList = StreamSupport
                .stream(employees.spliterator(), true)
                .collect(Collectors.toList());

        employeeList.add(employee);

        employeeDao.saveAll(employeeList);

        return employee.getId();
    }
}
