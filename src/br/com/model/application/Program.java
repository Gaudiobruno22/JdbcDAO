package br.com.model.application;

import java.util.Date;
import java.util.List;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.SellerDao;
import br.com.model.entities.Department;
import br.com.model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("***** Teste 1: Seller findById *****");
		Seller seller = sellerDao.findById(6);	
		System.out.println(seller);
		
		System.out.println("\n***** Teste 2: Seller findByDepartment *****");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		list.forEach(System.out::println);
		
		System.out.println("\n***** Teste 3: Seller findAll *****");
	    list = sellerDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n***** Teste 4: Seller Insert *****");
		Seller insertSeller = new Seller(8, "Paulo Toba", "paulo@gmail.com", new Date(), 2000.00, department);
		sellerDao.insert(insertSeller);
		System.out.println("Inserted. New Id = " + insertSeller.getId());
	}

}
