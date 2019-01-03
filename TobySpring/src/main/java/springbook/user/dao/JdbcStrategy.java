package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcStrategy {

	PreparedStatement makeStatement(Connection c) throws SQLException;
	
}
