package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDao {

	Connection c;
	PreparedStatement ps;
	ConnectionMaker connectionMaker;

	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {

		// ConnectionMaker connectionMaker = new DConnectionMaker(); �����ڷ� ���Թ޾ұ� ������, ���̻� context�ڵ忡�� ���� Ŭ������ ������� �ʾƵ� ��.
		c = connectionMaker.makeConnection();
		ps = c.prepareStatement("insert into user values (?,?,?)");
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		ps.execute();

		ps.close();
		c.close();

	}

	public void deleteAll() throws ClassNotFoundException, SQLException {
		// ConnectionMaker connectionMaker = new DConnectionMaker(); �����ڷ� ���Թ޾ұ� ������, ���̻� context�ڵ忡�� ���� Ŭ������ ������� �ʾƵ� ��.
		c = connectionMaker.makeConnection();

		ps = c.prepareStatement("delete from user");
		ps.execute();

		ps.close();
		c.close();
	}

}
