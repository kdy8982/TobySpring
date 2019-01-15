package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceImplTest extends UserServiceImpl {

	@Autowired
	UserDao userDao;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(new User("kdy12", "김대연", "kdy892@naver.com", "12345", Level.BASIC, 49, 0),
				new User("joytech", "강명성", "myunsung@naver.com", "12345", Level.BASIC, 50, 0),
				new User("ssh12", "신승한", "erwins@naver.com", "12345", Level.SILVER, 60, 29),
				new User("madnite1", "이상호", "shlee@naver.com", "12345", Level.SILVER, 60, 30),
				new User("ommk12", "오민규", "mkoh@naver.com", "12345", Level.GOLD, 100, 100));
	}

	@Test
	public void isInjectUserServiceBean() {
		assertThat(this.userService, is(notNullValue()));
	}

	@Test
	public void upgradeLevels() {  // 트랜잭션과 무관한 테스트(DB에까지 닿지 않는다). 
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao); // 목 오브젝트를 직접 DI해준다.

		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytech", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

		List<String> request = mockMailSender.getRequest();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));

	}

	private void checkUserAndLevel(User updated, String excpectId, Level expectLevel) {
		assertThat(updated.getId(), is(excpectId));
		assertThat(updated.getLevel(), is(expectLevel));
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
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

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}

	static class TestUserServiceException extends RuntimeException {
	}

	private void checkLevelUpgrade(User user, Boolean upgraded) {
		User userUpdate = userDao.get(user.getId()); 

		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserServiceImpl testUserServiceImpl = new TestUserService(users.get(3).getId());
		testUserServiceImpl.setUserDao(userDao); 
		testUserServiceImpl.setMailSender(mailSender);

		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserServiceImpl);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");
		
		UserService txUserService = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {UserService.class}, txHandler);

		userDao.deleteAll(); 
		for (User user : users) 
			userDao.add(user);  

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		checkLevelUpgrade(users.get(1), false); 
	}

	static class TestUserService extends UserServiceImpl {
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}

		@Override
		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}

	}

	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();

		public List<String> getRequest() {
			return requests;
		}

		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage... simpleMessages) throws MailException {

		}

	}

	static class MockUserDao implements UserDao {

		private List<User> users;
		private List<User> updated = new ArrayList();

		private MockUserDao(List<User> users) {
			this.users = users;
		}

		public List<User> getUpdated() {
			return this.updated;
		}

		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String email) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<User> getAll() { // DB에서 유져 목록을 select하는 것과 동일한 역할을 하는 메서드. 
			return this.users;
		}

		@Override
		public void update(User user) { // DB로 update된 User Object의 목록을 테스트하기 위해  리턴해주는 메서드.   
			updated.add(user);
		}

	}
}
