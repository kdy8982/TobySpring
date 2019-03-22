package springbook.user.sqlservice.updatable;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import springbook.user.sqlservice.SqlNotFoundException;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	EmbeddedDatabase db; 
	
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL).addScript(
						"classpath:springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
		
		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new
				EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);
		
		return embeddedDbSqlRegistry;
	}

	@Test
	public void transactionUpdate() throws SqlNotFoundException {
		checkFindResult("SQL1", "SQL2", "SQL3");
		
		Map<String,String> sqlmap = new HashMap<String, String> ();
		sqlmap.put("KEY1", "Modified");
		sqlmap.put("KEY9999!@#$", "Modified9999");
		
		try {
			sqlRegistry.updateSql(sqlmap);
			fail("익셉션이 나야만 하는데, 나지 않았어요.");
		} catch(SqlUpdateFailureException e) {
			checkFindResult("SQL1", "SQL2", "SQL3");
		}
	}
	
	
	@After
	public void tearDown() {
		db.shutdown();
	}
	

	
}
