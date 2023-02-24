package br.com.model.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.model.dao.SellerDao;
import br.com.model.entities.Department;
import br.com.model.entities.Seller;
import db.DB;
import db.DbException;

public class SellerDaoJDBC implements SellerDao{
	
	private static Connection conn;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Department dep = new Department();
	Seller sel = new Seller();
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "INSERT INTO seller\r\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)\r\n"
					+ "VALUES\r\n"
					+ "(?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, 1500.00);
			ps.setInt(5, obj.getDepartment().getId());
			
			int rows = ps.executeUpdate();
			if(rows > 0) {
				 rs = ps.getGeneratedKeys();
				 if(rs.next()) {
					 int id = rs.getInt(1);
					 obj.setId(id);
				 }
			}
			else{
				throw new DbException("Unexpected Error! 0 Rows Inserted.");
			}			
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}		
	}

	@Override
	public void update(Seller obj){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "UPDATE seller\r\n"
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\r\n"
					+ "WHERE Id = ?";
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, 1500.00);
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			ps.executeUpdate();
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}	
	}

	@Override
	public void deleteById(Integer id) {
		
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM seller\r\n"
					      + "WHERE Id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			int rowsAffected = ps.executeUpdate();
			
		
			while(rowsAffected == 0) {
				System.out.println("ID Not Exists! Do you Want to Try Again? Digit 'Y' for yes, or 'N' for Exit Program.");
				char choice = sc.next().toUpperCase().charAt(0);				
				if(choice == 'Y') {					
					System.out.println("Enter the Id who you want to Delete:");
					id = sc.nextInt();
					ps = conn.prepareStatement(sql);
					ps.setInt(1, id);
					rowsAffected = ps.executeUpdate();
					if(rowsAffected > 0) {
						System.out.println("Delete Completed.");
					}
				}
				else {
					System.out.println("Program Terminated");
					return;
				}
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					                    + " FROM seller INNER JOIN department "
					                    + "ON seller.DepartmentId = department.Id "
					                    + "WHERE seller.Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				dep.instantiateDepartment(rs);
				sel.instantiateSeller(rs, dep);				
				return sel;
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);			
		}		
		return null;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT seller.*,department.Name as DepName\r\n"
					     + "FROM seller INNER JOIN department\r\n"
					     + "ON seller.DepartmentId = department.Id\r\n"
					     + "ORDER BY Name";
			
			ps = conn.prepareStatement(sql);			
			rs = ps.executeQuery();					
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {			
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//Para não gerar Dois Objetos de Departamento, Utilizo o Map para controlar e 
				//Verificar se o Departamento Retornado já foi Instanciado ao menos uma Vez.
				
				if(dep == null) {
					dep = dep.instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
				sel.instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);			
		}		
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT seller.*,department.Name as DepName\r\n"
					     + "FROM seller INNER JOIN department\r\n"
					     + "ON seller.DepartmentId = department.Id\r\n"
					     + "WHERE DepartmentId = ?\r\n"
					     + "ORDER BY Name";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, department.getId());			
			rs = ps.executeQuery();					
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {			
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//Para não gerar Dois Objetos de Departamento, Utilizo o Map para controlar e 
				//Verificar se o Departamento Retornado já foi Instanciado ao menos uma Vez.
				
				if(dep == null) {
					dep = dep.instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
			    sel.instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);			
		}		
	}
}
