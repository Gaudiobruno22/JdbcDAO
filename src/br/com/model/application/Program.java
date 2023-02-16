package br.com.model.application;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.SellerDao;
import br.com.model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SellerDao sellerDao = DaoFactory.createSellerDao();
		Seller seller = sellerDao.findById(6);
		
		System.out.println(seller);
	}

}
