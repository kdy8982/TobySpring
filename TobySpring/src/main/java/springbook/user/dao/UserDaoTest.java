package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")

public class UserDaoTest {

	@Autowired
	private UserDao dao;
	
	@Autowired
	DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		//ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		//dao = applicationContext.getBean("userDao", UserDaoJdbc.class);

		this.user1 = new User("kdy12","kdy", "kdy8982@naver.com", "1234", Level.BASIC, 1, 0);
		this.user2 = new User("spr12","spring", "spring@naver.com", "1234", Level.SILVER, 55, 10);
		this.user3 = new User("myb12", "mybatis", "mybatis@naver.com", "1234", Level.GOLD, 100, 40);
	}
	
	@Test
	public void checkInject() {
		assertThat(dao, is(notNullValue()));
	}
	
	@Test
	public void checkSetupUser(){
		assertThat(this.user1.getName(), is("kdy"));
		assertThat(this.user2.getName(), is("spring"));
		assertThat(this.user3.getName(), is("mybatis"));
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
		assertThat(user1.getEmail(), is(user2.getEmail()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getEmail(), is(user2.getEmail()));

	}

	@Test
	public void addAndGet() {
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		
		User userget1 = dao.get(user1.getId());
		System.out.println(userget1.getEmail());
		checkSameUser(userget1, user1);
		
		
		User userget2 = dao.get(user2.getId());
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
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1update, user1);
	}
	
	
	@Test(expected=org.springframework.dao.DuplicateKeyException.class)
	public void dupilcateId() {
		dao.add(user1);
		dao.add(user1);
	}
}
