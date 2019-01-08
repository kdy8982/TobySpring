package springbook.user.dao;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.Level;
import springbook.user.domain.User;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/applicationContext.xml")

public class UserDaoTest {

	private ApplicationContext applicationContext;

	private UserDaoJdbc dao;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		dao = applicationContext.getBean("userDao", UserDaoJdbc.class);

		this.user1 = new User("kdy12","김대연", "kdy8982@naver.com", "1234", Level.BASIC, 1, 0);
		this.user2 = new User("spr12","스프링", "spring@naver.com", "1234", Level.SILVER, 55, 10);
		this.user3 = new User("myb12", "마이바티스", "mybatis@naver.com", "1234", Level.GOLD, 100, 40);
	}

	@Test
	public void add() throws SQLException {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		dao.getCount();
		assertThat(dao.getCount(), org.hamcrest.CoreMatchers.is(3));
	}

	@Test
	public void getAllTest() throws SQLException {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		ArrayList<User> list = (ArrayList<User>) dao.getAll();

		assertThat(list.size(), org.hamcrest.CoreMatchers.is(3));
	}

	public void checkSameUser(User user1, User user2) {
		assertThat(user1.getEmail(), org.hamcrest.CoreMatchers.is(user2.getEmail()));
		assertThat(user1.getName(), org.hamcrest.CoreMatchers.is(user2.getName()));
		assertThat(user1.getPassword(), org.hamcrest.CoreMatchers.is(user2.getPassword()));
		assertThat(user1.getLevel(), org.hamcrest.CoreMatchers.is(user2.getLevel()));
		assertThat(user1.getLogin(), org.hamcrest.CoreMatchers.is(user2.getLogin()));
		assertThat(user1.getEmail(), org.hamcrest.CoreMatchers.is(user2.getEmail()));

	}

	@Test
	public void addAndGet() {
		User userget1 = dao.get(user1.getEmail());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getEmail());
		checkSameUser(userget2, user2);
		
	}

	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		
		user1.setName("noo");
		user1.setPassword("nooo");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);
		
		User user1update = dao.get(user1.getEmail());
		checkSameUser(user1update, user1);
	}
	
	
	@Test
	public void dupilcateId() {
		dao.add(user1);
		dao.add(user1);
	}
}
