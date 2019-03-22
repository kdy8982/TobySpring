package springbook.user.sqlservice.updatable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.user.sqlservice.ConcurrentHashMapSqlRegistry;
import springbook.user.sqlservice.SqlNotFoundException;

/**
 * @author 김대연
 * Test용 추상클래스 
 * Junit이 바로 실행되지 않음. 
 * 이 클래스를 상속받는 클래스에서 추상메서드를 구현해야 실행이 가능. 
 */
public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;
	
	
	@Before
	public void setUp(){
		sqlRegistry = createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}

	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();
	
	/* 추상클래스를 구현하하는 자식클래스에서 checkFindResult 메서드를 추가하고 싶을 수도 있을 테니, private에서 protected로 바꾼다.*/
	protected void checkFindResult(String expected1, String expected2, String expected3) throws SqlNotFoundException {
		assertThat(sqlRegistry.findSql("KEY1"), is(expected1));
		assertThat(sqlRegistry.findSql("KEY2"), is(expected2));
		assertThat(sqlRegistry.findSql("KEY3"), is(expected3));
	}
	
	@Test
	public void find() throws SqlNotFoundException {
		checkFindResult("SQL1","SQL2","SQL3");
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
		
		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modified1","SQL2","Modified3");
	}
	
}
