package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDao {

	Connection c;
	PreparedStatement ps;

	public void add(User user) throws ClassNotFoundException, SQLException {

		c = getConnection();
		ps = c.prepareStatement("insert into user values (?,?,?)");
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		ps.execute();

		ps.close();
		c.close();

	}

	public void deleteAll() throws ClassNotFoundException, SQLException {
		c = getConnection();
		ps = c.prepareStatement("delete from user");
		ps.execute();

		ps.close();
		c.close();
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "1234");
		return c;
	}

}
