package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;


public class JdbcContext {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public void workWithStatementStrategy(JdbcStrategy callback) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			ps = callback.makeStatement(c);
			ps.execute();
		} 
		catch (SQLException e) {
			throw e;
		}
		finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			
			if(c != null) {
				try {
					c.close();
				}
				catch(SQLException e) {
					throw e;
				}
			}
		}
	
		
	}
}
