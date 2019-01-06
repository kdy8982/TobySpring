package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import springbook.user.domain.User;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/applicationContext.xml")

public class UserDaoTest {

	private ApplicationContext applicationContext;

	private UserDao dao;
	private DataSource dataSource;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		dao = applicationContext.getBean("userDao", UserDao.class);
		dataSource = applicationContext.getBean("dataSource", javax.sql.DataSource.class);
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
