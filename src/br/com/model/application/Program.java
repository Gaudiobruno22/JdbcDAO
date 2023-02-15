package br.com.model.application;

import br.com.model.dao.DaoFactory;
import br.com.model.dao.DepartmentDao;
import br.com.model.dao.SellerDao;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SellerDao sellerDao = DaoFactory.createSellerDao();
		
	}

}
