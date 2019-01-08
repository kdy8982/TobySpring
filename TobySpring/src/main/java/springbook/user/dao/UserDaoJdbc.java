package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc  implements UserDao {

	Connection c = null;
	PreparedStatement ps = null;
	ConnectionMaker connectionMaker = null;
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setEmail(rs.getString("email"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			
			return user;
		}
	}; 
	
	

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(User user) {
		this.jdbcTemplate.update("insert into user values (?,?,?,?,?,?,?)", user.getId(), user.getEmail(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
		
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from user");
	}
	
	public int getCount() {
		return this.jdbcTemplate.query( new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("select count(*) from user");
				
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getInt(1);
			}
		});
	}
	
	public User get(String id) {		
		return this.jdbcTemplate.queryForObject("select * from user where id=?", new Object[] {id}, userMapper);
	}
	
	public List<User> getAll() {
			
		return this.jdbcTemplate.query("select * from user order by id", userMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update(
				"update user set email = ?, name = ?, password = ?, level = ?, login = ?, " + 
				"recommend = ? where id = ?" , user.getEmail(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId()
				);
		
	}

}
