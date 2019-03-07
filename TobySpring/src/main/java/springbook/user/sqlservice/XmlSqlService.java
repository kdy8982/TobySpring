package springbook.user.sqlservice;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.learningtest.jdk.jaxb.SqlType;
import springbook.learningtest.jdk.jaxb.Sqlmap;
import springbook.user.dao.UserDao;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader{
	// --------- SqlProvider ------------
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
	
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	@PostConstruct  // 일반적인 메서드(생성자가 아닌)를 추가하는 스프링 애노테이션. 이 클래스의 오브젝트 생성과 DI가 끝난 뒤에, 이 메서드를 호출한다. 
	public void loadSql() { // 이렇게 만든 이유는 생성자에서 예외가 발생하는 로직이 있으면 바람직하지 않기 때문에, 별도의 초기화 메서드를 추가함. 
		this.sqlReader.read(this.sqlRegistry);
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		}
		else {
			return sql;
		}
	}
	
	// --------- SqlRegistry ------------	
	// : 읽어온 SQL을 MAP에 등록하는 역할 
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		
		if(sql == null) {
			throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		} else {
			return sql;
		}
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
		
	}
	
	
	// --------- SqlReader ------------
	// : 외부의 특정 경로에 잇는 SQL XML파일을 읽어오는 역할.  
	private String sqlmapFile;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	@Override
	public void read(SqlRegistry sqlRegistry) { // 생성자에서 예외가 발생하는 로직이 있으면 바람직하지 않기 때문에, 별도의 초기화 메서드를 추가함. 
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is =UserDao.class.getResourceAsStream(sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e); // 복구불가능한 런타임 예외는, 불필요한 throws를 피하기 위해, catch하여 throw를 던지게끔 만든다.
		}
	}

}
