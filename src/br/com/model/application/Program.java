package br.com.model.application;

import java.util.List;
import java.util.Scanner;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.SellerDao;
import br.com.model.entities.Department;
import br.com.model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);

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
		
		/*System.out.println("\n***** Teste 4: Seller Insert *****");
		//Coloquei qulquer número no Parâmetros do Id, porque não poderia inserir Nulo.
		Seller insertSeller = new Seller(1, "Que tristeza", "paulo@gmail.com", new Date(), 2000.00, department);
		sellerDao.insert(insertSeller);
		System.out.println("Inserted. New Id = " + insertSeller.getId());*/
		
		System.out.println("\n***** Teste 5: Seller Update *****");
		seller = sellerDao.findById(1);
		seller.setName("Comprei 16 Macacos");
		sellerDao.update(seller);
		System.out.println("Update Complete.");
		
		System.out.println("\n***** Teste 6: Seller Delete *****");
		System.out.println("Enter the Id who you want to Delete:");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		
		sc.close();
		
		
	}

}
