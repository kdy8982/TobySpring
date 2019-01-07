package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

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
public class UserServiceTest extends UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;

	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(new User("박범진", "bumjin@naver.com", "1234", Level.BASIC, 49, 0),
				new User("강명성", "myunsung@naver.com", "1234", Level.BASIC, 50, 0),
				new User("신승한", "erwins@naver.com", "1234", Level.SILVER, 60, 29),
				new User("이상호", "shlee@naver.com", "1234", Level.SILVER, 60, 30),
				new User("오민규", "mkoh@naver.com", "1234", Level.GOLD, 100, 100)
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
	
	
	static class TestUserService extends UserService {
		private String email;
		
		private TestUserService(String email) {
			this.email = email;
		}
		
		@Override
		protected void upgradeLevel(User user) {
			if(user.getEmail().equals(this.email)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
	

	
	private void checkLevelUpgrade(User user, Boolean upgraded) {
		User userUpdate = userDao.get(user.getEmail());
		
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void upgradeAllOrNothing() {
		UserService testUserService = new TestUserService(users.get(3).getEmail());
		testUserService.setUserDao(this.userDao);
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}
		catch(TestUserServiceException e) {
		}
		
		checkLevelUpgrade(users.get(1) , false);
	}
	
	
}
