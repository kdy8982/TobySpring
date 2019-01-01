package springbook.user.dao;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.connectionMaker = new DConnectionMaker();
		
		return userDao;
	}
}
