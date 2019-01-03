package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import springbook.user.domain.User;

public class UserDao {

	Connection c = null;
	PreparedStatement ps = null;
	ConnectionMaker connectionMaker = null;
	
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {

		try  {
			c = dataSource.getConnection();
			ps = c.prepareStatement("insert into user values (?,?,?)");
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			ps.execute();
			
		}
		catch(Exception e) {
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

	public void deleteAll() throws SQLException {
		JdbcStrategy deleteStrategy = new JdbcStrategy() {
			
			@Override
			public PreparedStatement makeStatement(Connection c) throws SQLException {
				return c.prepareStatement("delete from user");
			}
		};
		
		jdbcContext(deleteStrategy);
	}
	
	private void jdbcContext(JdbcStrategy callback) throws SQLException {
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
