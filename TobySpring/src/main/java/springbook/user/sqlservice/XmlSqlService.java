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

public class XmlSqlService implements SqlService{

	private Map<String, String> sqlMap = new HashMap<String, String>();
	private String sqlmapFile;
	
	@PostConstruct  // �Ϲ����� �޼���(�����ڰ� �ƴ�)�� �߰��ϴ� ������ �ֳ����̼�. �� Ŭ������ ������Ʈ ������ DI�� ���� �ڿ�, �� �޼��带 ȣ���Ѵ�. 
	public void loadSql() { // �����ڿ��� ���ܰ� �߻��ϴ� ������ ������ �ٶ������� �ʱ� ������, ������ �ʱ�ȭ �޼��带 �߰���. 
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is =UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e); // �����Ұ����� ��Ÿ�� ���ܴ�, ���ʿ��� throws�� ���ϱ� ����, catch�Ͽ� throw�� �����Բ� �����.
		}
	}
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
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

}
