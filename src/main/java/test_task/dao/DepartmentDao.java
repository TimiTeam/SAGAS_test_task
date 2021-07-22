package test_task.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test_task.model.Department;

import java.util.List;

@Repository
public interface DepartmentDao extends CrudRepository<Department, Long> {
    // Get a list of department IDS where the number of employees doesn't exceed 3 people
    @Query(
            value = "SELECT d.ID FROM DEPARTMENT d " +
                    "WHERE (" +
                    "SELECT COUNT(*) FROM EMPLOYEE em " +
                    "WHERE em.DEPARTMENT_ID = d.ID) <= 3",
            nativeQuery = true)
    List<Long> findAllWhereDepartmentDoesntExceedThreePeople();

    // Get a list of departments IDs with the maximum total salary of employees
    @Query(
            value = "SELECT dep.ID FROM DEPARTMENT dep " +
                    "WHERE (SELECT SUM(emp.SALARY) FROM EMPLOYEE emp " +
                    "WHERE emp.DEPARTMENT_ID = dep.ID) >= ALL " +
                    "(SELECT SUM(e.SALARY) FROM DEPARTMENT d " +
                    "JOIN EMPLOYEE e ON e.DEPARTMENT_ID = d.ID " +
                    "WHERE d.ID != dep.ID )",
            nativeQuery = true)
    List<Long> findAllByMaxTotalSalary();
}
