package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import springbook.user.domain.User;

public class UserDao {

	Connection c = null;
	PreparedStatement ps = null;
	ConnectionMaker connectionMaker = null;
	
	private JdbcTemplate jdbcTemplate;
	

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(User user) throws SQLException {
		
		this.jdbcTemplate.update("insert into user values (?,?,?)", user.getEmail(), user.getName(), user.getPassword());
		
	}

	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("delete from user");
	}
		
	
}
