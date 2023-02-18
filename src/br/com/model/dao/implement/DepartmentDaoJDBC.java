package br.com.model.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.model.dao.DepartmentDao;
import br.com.model.entities.Department;
import db.DB;
import db.DbException;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	SellerDaoJDBC dep = new SellerDaoJDBC(conn);
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "INSERT INTO department (Name)\r\n"
					+ "VALUES (?)";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			int rowsAffected = ps.executeUpdate();
			if(rowsAffected > 0 ) {
				rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Error During Insert. No Rows Affected.");
			}
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
	public void update(Department obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = "update department\r\n"
					+ "   set Name = ?\r\n"
					+ " where Id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			int rows = ps.executeUpdate();
			
			if(rows ==0) {
				throw new DbException("\nUnexpected Error. No Rows Affected!");	
			}
			else {
				System.out.println("\nDone! Rows Affected: " + rows);
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
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "delete from department where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new DbException("Unexpected Error. No Rows Delected.");
			}
			else {
				System.out.println("\nDelete Completed. " + rows + " Row Affected.");
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
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from department d where d.Id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {			
				Department dpto = new Department();
				dpto = getInstantiate(rs);
				return dpto;
			}
			return null;
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
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from department d order by Name";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			List<Department> list = new ArrayList<>();
			while(rs.next()) {
				Department obj = getInstantiate(rs);
				list.add(obj);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}	
	}
	
    private Department getInstantiate(ResultSet rs) throws SQLException{
    	return dep.instantiateDepartment(rs);
    }
}