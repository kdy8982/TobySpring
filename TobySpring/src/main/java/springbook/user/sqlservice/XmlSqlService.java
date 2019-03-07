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
	
	@PostConstruct  // �Ϲ����� �޼���(�����ڰ� �ƴ�)�� �߰��ϴ� ������ �ֳ����̼�. �� Ŭ������ ������Ʈ ������ DI�� ���� �ڿ�, �� �޼��带 ȣ���Ѵ�. 
	public void loadSql() { // �̷��� ���� ������ �����ڿ��� ���ܰ� �߻��ϴ� ������ ������ �ٶ������� �ʱ� ������, ������ �ʱ�ȭ �޼��带 �߰���. 
		this.sqlReader.read(this.sqlRegistry);
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlRetrievalFailureException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		}
		else {
			return sql;
		}
	}
	
	// --------- SqlRegistry ------------	
	// : �о�� SQL�� MAP�� ����ϴ� ���� 
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		
		if(sql == null) {
			throw new SqlNotFoundException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		} else {
			return sql;
		}
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
		
	}
	
	
	// --------- SqlReader ------------
	// : �ܺ��� Ư�� ��ο� �մ� SQL XML������ �о���� ����.  
	private String sqlmapFile;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	@Override
	public void read(SqlRegistry sqlRegistry) { // �����ڿ��� ���ܰ� �߻��ϴ� ������ ������ �ٶ������� �ʱ� ������, ������ �ʱ�ȭ �޼��带 �߰���. 
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
			throw new RuntimeException(e); // �����Ұ����� ��Ÿ�� ���ܴ�, ���ʿ��� throws�� ���ϱ� ����, catch�Ͽ� throw�� �����Բ� �����.
		}
	}

}
