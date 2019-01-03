package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDao {

	Connection c = null;
	PreparedStatement ps = null;
	ConnectionMaker connectionMaker = null;

	public UserDao() {
	}
	
	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {

		try  {
			c = connectionMaker.makeConnection();
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

	public void deleteAll() throws ClassNotFoundException, SQLException {
		try {
			c = connectionMaker.makeConnection();

			ps = c.prepareStatement("delete from user");
			ps.execute();
		} 
		catch (Exception e) {
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
