package br.com.model.application;

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
	}

}
