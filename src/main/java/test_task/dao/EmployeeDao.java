package test_task.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test_task.model.Employee;

import java.util.List;

@Repository
public interface EmployeeDao extends CrudRepository<Employee, Long> {


    // Get a list of employees receiving a salary greater than that of the boss
    @Query(
            value = "SELECT e.* FROM EMPLOYEE e " +
                    "JOIN EMPLOYEE boss ON boss.ID = e.BOSS_ID " +
                    "WHERE e.SALARY > boss.SALARY",
            nativeQuery = true)
    List<Employee> findAllWhereSalaryGreaterThatBoss();


    // Get a list of employees receiving the maximum salary in their department
    @Query(
            value = "SELECT e.* FROM EMPLOYEE e " +
                    "WHERE e.SALARY IN (SELECT MAX(em.SALARY) FROM EMPLOYEE em " +
                    "WHERE em.DEPARTMENT_ID = e.DEPARTMENT_ID)",
            nativeQuery = true)
    List<Employee> findAllByMaxSalary();


    // Get a list of employees who do not have boss in the same department
    @Query(
            value = "SELECT e.* FROM EMPLOYEE e " +
                    "JOIN EMPLOYEE boss ON BOSS.ID = e.BOSS_ID " +
                    "WHERE e.DEPARTMENT_ID != BOSS.DEPARTMENT_ID",
            nativeQuery = true)
    List<Employee> findAllWithoutBoss();
}
