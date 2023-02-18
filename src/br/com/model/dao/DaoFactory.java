package br.com.model.dao;

import br.com.model.dao.implement.DepartmentDaoJDBC;
import br.com.model.dao.implement.SellerDaoJDBC;
import db.DB;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}

}
