package springbook.user.service;

import java.util.List;

import springbook.user.domain.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
	
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user);
	
}
