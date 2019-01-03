package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws Exception {
		User user = new User("KimDaeYeon", "kdy8982@naver.com", "1234");
		
		// UserDao userDao = new UserDao(new DConnectionMaker()); // Ŭ���̾�Ʈ �ܰ迡�� �����ڷ� ��� Ŀ�ؼ��� ������� ��������. 
		// UserDao userDao = new DaoFactory().userDao(); // DaoFactoryŬ������ �и��Ͽ� , userDao(new DConnectionMaker)�� �������ش�.
		
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
		
		userDao.deleteAll();
		userDao.add(user);
		userDao.deleteAll();
		System.out.println("complete!!");
		
	}
}