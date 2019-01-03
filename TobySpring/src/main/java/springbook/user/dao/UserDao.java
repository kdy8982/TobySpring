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
	JdbcContext jdbcContext = null;
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public void add(User user) throws SQLException {
		jdbcContext.workWithStatementStrategy(new JdbcStrategy() {
			
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
		jdbcContext.workWithStatementStrategy(new JdbcStrategy() {
			
			@Override
			public PreparedStatement makeStatement(Connection c) throws SQLException {
				ps = c.prepareStatement("delete from user");
				return ps;
			}
		});
	}
	
	
}
