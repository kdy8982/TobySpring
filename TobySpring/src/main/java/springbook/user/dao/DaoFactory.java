package springbook.user.dao;

public class DaoFactory {

	
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.connectionMaker = new DConnectionMaker();
		
		return userDao;
	}
}
