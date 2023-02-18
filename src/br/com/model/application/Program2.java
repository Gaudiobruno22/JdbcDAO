package br.com.model.application;

import java.util.List;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.DepartmentDao;
import br.com.model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		
		System.out.println("***** Test 2: Department findById *****");
		Department department = depDao.findById(4);
		System.out.println(department);
		
		System.out.println("\n***** Test 2: Department findAll *****");
		List<Department> list = depDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n***** Test 3: Department Insert *****");
		Department dep = new Department(1, "Casa de Massagem");
		depDao.insert(dep);
		System.out.println("Done. New Id: " + dep.getId());

	}

}
