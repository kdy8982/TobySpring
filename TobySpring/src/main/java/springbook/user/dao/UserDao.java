package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import springbook.user.domain.User;

public class UserDao {

	Connection c = null;
	PreparedStatement ps = null;
	ConnectionMaker connectionMaker = null;
	
	private DataSource dataSource;
	
	JdbcContext jdbcContext;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public void add(User user) throws SQLException {
		jdbcContext(new JdbcStrategy() {
			
			@Override
			public PreparedStatement makeStatement(Connection c) throws SQLException {
				ps = c.prepareStatement("insert into user values (?,?,?)");
				ps.setString(1, user.getEmail());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		});
	}

	public void deleteAll() throws SQLException {
		jdbcContext(new JdbcStrategy() {
			
			@Override
			public PreparedStatement makeStatement(Connection c) throws SQLException {
				ps = c.prepareStatement("delete from user");
				return ps;
			}
		});
	}
	
	private void jdbcContext(JdbcStrategy callback) throws SQLException { // UserDao외에도 다른 Dao 클래스에서도 사용할 수 있도록, 외부 클래스의 메소드로 분리한다.
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
