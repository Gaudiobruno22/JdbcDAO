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

import br.com.model.dao.SellerDao;
import br.com.model.entities.Department;
import br.com.model.entities.Seller;
import db.DB;
import db.DbException;

public class SellerDaoJDBC implements SellerDao{
	
	private static Connection conn;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
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
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
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
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);				
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setDepartment(dep);
		return sel;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName("DepName");
		return dep;
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
				
				//Para n�o gerar Dois Objetos de Departamento, Utilizo o Map para controlar e 
				//Verificar se o Departamento Retornado j� foi Instanciado ao menos uma Vez.
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
				Seller sel = instantiateSeller(rs, dep);
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
				
				//Para n�o gerar Dois Objetos de Departamento, Utilizo o Map para controlar e 
				//Verificar se o Departamento Retornado j� foi Instanciado ao menos uma Vez.
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
				Seller sel = instantiateSeller(rs, dep);
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
