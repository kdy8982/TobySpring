package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")

public class UserDaoTest {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before
	public void setUp() {
//		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
//		dao = applicationContext.getBean("userDao",UserDao.class);
		
		this.dao=this.applicationContext.getBean("userDao", UserDao.class);
		
		this.user1 = new User("김대연", "kdy8982@naver.com", "1234");
		this.user2 = new User("스프링", "spring@naver.com", "1234");
		this.user3 = new User("마이바티스", "mybatis@naver.com", "1234");
	}
	
	@Test
	public void add() throws SQLException {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		dao.getCount();
		assertThat(dao.getCount(), is(3));
	}
	
	@Test
	public void getAllTest() throws SQLException {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		ArrayList<User> list = (ArrayList<User>) dao.getAll();
		
		assertThat(list.size(), is(3));
			
	}
	
	
}
