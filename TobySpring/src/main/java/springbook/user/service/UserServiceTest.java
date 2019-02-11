package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest extends UserServiceImpl {

	@Autowired
	UserDao userDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	@Autowired
	ApplicationContext context;

	@Autowired
	UserService userService;

	@Autowired
	UserService testUserService;

	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(new User("kdy12", "��뿬", "kdy892@naver.com", "12345", Level.BASIC, 49, 0),
				new User("joytech", "����", "myunsung@naver.com", "12345", Level.BASIC, 50, 0),
				new User("ssh12", "�Ž���", "erwins@naver.com", "12345", Level.SILVER, 60, 29),
				new User("madnite1", "�̻�ȣ", "shlee@naver.com", "12345", Level.SILVER, 60, 30),
				new User("ommk12", "���α�", "mkoh@naver.com", "12345", Level.GOLD, 100, 100));
	}

	@Test
	public void isInjectUserServiceBean() {
		assertThat(this.userService, is(notNullValue()));
	}

	@Test
	public void upgradeLevels() { // Ʈ����ǰ� ������ �׽�Ʈ(DB������ ���� �ʴ´�). 
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao); // �� ������Ʈ�� ���� DI���ش�.

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
			System.out.println(userUpdate.getLevel() + ", " + user.getLevel());

			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
		//		UserServiceImpl testUserServiceImpl = new TestUserServiceImpl();
		//		testUserServiceImpl.setUserDao(userDao);
		//		testUserServiceImpl.setMailSender(mailSender);

		/* Ʈ����� ���Ͻ� ���丮 �� �׽�Ʈ �κ� */
		//		TxProxyFactoryBean txProxyFactoryBean = (TxProxyFactoryBean) context.getBean("&userService" , TxProxyFactoryBean.class); // Factory���� ������ִ� Object�� '�ƴ�', ��ü�� �ҷ��´�.
		//		txProxyFactoryBean.setTarget(testUserServiceImpl); // �̹� test�� �ٽ��� �Ǵ� �κ�. �� �κ� ������ @DirtiesContext�� �ٿ���, TxProxyFactoryBean�� ���� �������.
		//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();

		/* �������� �̿���, ���Ͻ� ���丮�� �׽�Ʈ �κ� */
		//		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		//		pfBean.setTarget(testUserServiceImpl); // Ʈ����� ������ ���� �׽�Ʈ�� UserServiceImpl�� �ٲ�ġ��.
		//		pfBean.addAdvice(new TransactionAdvice(this.transactionManager));
		//		UserService txUserService = (UserService) pfBean.getObject();

		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		checkLevelUpgrade(users.get(1), false);
	}

	static class TransactionAdvice implements MethodInterceptor {

		private PlatformTransactionManager transactionManager;

		public TransactionAdvice(PlatformTransactionManager transactionManager) {
			this.transactionManager = transactionManager;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

			try {
				Object ret = invocation.proceed();
				this.transactionManager.commit(status);
				return ret;
			} catch (InvocationTargetException e) {
				this.transactionManager.rollback(status);
				throw e.getTargetException();
			}
		}
	}

	static class TestUserServiceImpl extends UserServiceImpl {
		private String id = "madnite1";

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
		public List<User> getAll() { // DB���� ���� ����� select�ϴ� �Ͱ� ������ ������ �ϴ� �޼���. 
			return this.users;
		}

		@Override
		public void update(User user) { // DB�� update�� User Object�� ����� �׽�Ʈ�ϱ� ����  �������ִ� �޼���.   
			updated.add(user);
		}

	}
	
	@Test
	public void advisorAutoProxyCreator (){
		assertThat(testUserService, is (java.lang.reflect.Proxy.class));
	}

}
