package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

public interface UserDao {

	void add(User user);
	void deleteAll();
	int getCount();
	User get(String email);
	List<User> getAll();
	void update(User user);
	
}
