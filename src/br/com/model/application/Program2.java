package br.com.model.application;

import java.util.List;
import java.util.Scanner;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.DepartmentDao;
import br.com.model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		
		System.out.println("***** Test 2: Department findById *****");
		Department department = depDao.findById(4);
		System.out.println(department);
		
		System.out.println("\n***** Test 2: Department findAll *****");
		List<Department> list = depDao.findAll();
		list.forEach(System.out::println);
		
		/*System.out.println("\n***** Test 3: Department Insert *****");
		Department dep = new Department(1, "Casa de Massagem");
		depDao.insert(dep);
		System.out.println("Done. New Id On Database: " + dep.getId());*/
		
		System.out.println("\n***** Test 4: Department Update *****");
		Department dep2 = new Department(9, "Loja de Doido");
		depDao.update(dep2);
		
		System.out.println("\n***** Test 5: Department Delete *****");
		System.out.println("Enter the Id Did you want to Delete:");
		int id = sc.nextInt();
		depDao.deleteById(id);
		
		
		sc.close();
	}

}
