package springbook.user.sqlservice;

import springbook.user.sqlservice.updatable.AbstractUpdatableSqlRegistryTest;
import springbook.user.sqlservice.updatable.UpdatableSqlRegistry;


public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
	
}
