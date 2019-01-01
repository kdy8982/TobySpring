package springbook.user.dao;

import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		User user = new User("KimDaeYeon", "kdy8982@naver.com", "1234");
		
		UserDao userDao = new UserDao(new DConnectionMaker()); // 클라이언트 단계에서 생성자로 어떠한 커넥션을 사용할지 주입해줌. 
		
		userDao.deleteAll();
		userDao.add(user);
		userDao.deleteAll();
		System.out.println("complete!");
		
	}
}
