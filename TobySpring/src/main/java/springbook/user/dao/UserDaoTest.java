package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		User user = new User("KimDaeYeon", "kdy8982@naver.com", "1234");
		
		// UserDao userDao = new UserDao(new DConnectionMaker()); // 클라이언트 단계에서 생성자로 어떠한 커넥션을 사용할지 주입해줌. 
		// UserDao userDao = new DaoFactory().userDao(); // DaoFactory클래스를 분리하여 , userDao(new DConnectionMaker)를 주입해준다.
		
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
		
		userDao.deleteAll();
		userDao.add(user);
		userDao.deleteAll();
		System.out.println("complete!");
		
	}
}
