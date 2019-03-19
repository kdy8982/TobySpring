package springbook.user.sqlservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest {
	
	UpdatableSqlRegistry sqlRegistry;
	
	@Before
	public void setUp() {
		sqlRegistry = new ConcurrentHashMapSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}
	
	
	@Test
	public void find() throws SqlNotFoundException {
		checkFindResult("SQL1","SQL2","SQL3");
	}
	
	private void checkFindResult(String expected1, String expected2, String expected3) throws SqlNotFoundException {
		assertThat(sqlRegistry.findSql("KEY1"), is(expected1));
		assertThat(sqlRegistry.findSql("KEY2"), is(expected2));
		assertThat(sqlRegistry.findSql("KEY3"), is(expected3));
	}
	
	@Test(expected=SqlNotFoundException.class)
	public void unknownKey() throws SqlNotFoundException {
		sqlRegistry.findSql("SQL9999!@#$");
	}
	
	@Test
	public void updateSingle() throws SqlNotFoundException {
		sqlRegistry.updateSql("KEY2","Modified2");
		checkFindResult("SQL1","Modified2","SQL3");
	}
	
	@Test
	public void updateMulti() throws SqlNotFoundException {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		sqlRegistry.updateSql("KEY2","Modified2");
		checkFindResult("Modified1","SQL2","Modified3");
	}
}
