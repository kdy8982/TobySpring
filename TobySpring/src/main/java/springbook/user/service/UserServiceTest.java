package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;

	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(new User("�ڹ���", "bumjin@naver.com", "1234", Level.BASIC, 49, 0),
				new User("����", "myunsung@naver.com", "1234", Level.BASIC, 50, 0),
				new User("�Ž���", "erwins@naver.com", "1234", Level.SILVER, 60, 29),
				new User("�̻�ȣ", "shlee@naver.com", "1234", Level.SILVER, 60, 30),
				new User("���α�", "mkoh@naver.com", "1234", Level.GOLD, 100, 100)
		);
	}

	@Test
	public void isInjectUserServiceBean() {
		assertThat(this.userService, is(notNullValue()));
	}

	
	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getEmail());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getEmail());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getEmail());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}

}
