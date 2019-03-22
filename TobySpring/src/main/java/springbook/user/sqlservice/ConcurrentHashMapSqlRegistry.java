package springbook.user.sqlservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import springbook.user.sqlservice.updatable.SqlUpdateFailureException;
import springbook.user.sqlservice.updatable.UpdatableSqlRegistry;


public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

	private Map<String, String> sqlMap = new ConcurrentHashMap<String, String>();

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if (sql == null)
			throw new SqlNotFoundException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		else
			return sql;
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		if(sqlMap.get(key) == null)
			throw new SqlUpdateFailureException(key + "�� �ش��ϴ� SQL�� ã�� �� �����ϴ�");
		else 
			sqlMap.put(key, sql);
	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for(Map.Entry<String, String> entry : sqlmap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}

	}

}