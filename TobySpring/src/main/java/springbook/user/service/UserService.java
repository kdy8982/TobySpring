package springbook.user.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {

	UserDao userDao;
	UserLevelUpgradePolicy upgradePolicy;
	PlatformTransactionManager transactionManager;
	private DataSource dataSource;


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy upgradePolicy) {
		this.upgradePolicy = upgradePolicy;
	}

	public void setTransactionManager (PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	protected void upgradeLevels() throws Exception {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			List<User> users = userDao.getAll();

			for (User user : users) {
				if (canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			
			this.transactionManager.commit(status);
			
		} catch (Exception e) {
			this.transactionManager.rollback(status);
			throw e;
		} 
	}

	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();

		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= 50);
		case SILVER:
			return (user.getRecommend() >= 30);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeMail(user);
	}

	private void sendUpgradeMail(User user) {
		Properties props = new Properties();
		props.put("mail.stmp.host", "mail.ksug.org");
		Session s = Session.getInstance(props, null);
		
		MimeMessage message = new MimeMessage(s);

		try {
			message.setFrom(new InternetAddress("useradmin@ksug.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			
			message.setSubject("Upgrade �ȳ�");
			message.setText("����ڴ��� ����� " + user.getLevel().name() + "�� ���׷��̵� �Ǿ����ϴ�.");
			
			Transport.send(message);
			
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void add(User user) {
		if (user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}

}
