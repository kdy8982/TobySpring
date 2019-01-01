package springbook.user.dao;

import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		User user = new User("KimDaeYeon", "kdy8982@naver.com", "1234");
		
		UserDao userDao = new UserDao();
		userDao.deleteAll();
		userDao.add(user);
		userDao.deleteAll();
		System.out.println("complete!");
		
	}
}
