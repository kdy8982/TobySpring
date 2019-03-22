package springbook.user.sqlservice;

import javax.annotation.PostConstruct;

/**
 * @author USER
 * 
 * ��ü���̰� ������ �������� ���� Ŭ����
 * (��ü���� ����� �� �������̽��� ���� Ŭ�������� ����)
 */
public class BaseSqlService implements SqlService {

	SqlReader sqlReader;
	SqlRegistry sqlRegistry;

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	/* 1. ������ �о�´� */
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}

	/* 2. ���������� ������ , SQL�� ã�´� */
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

}
