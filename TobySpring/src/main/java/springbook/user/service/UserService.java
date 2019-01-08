package springbook.user.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
	}

	public void add(User user) {
		if (user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}

}
