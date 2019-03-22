package springbook.user.sqlservice;

import javax.annotation.PostConstruct;

/**
 * @author USER
 * 
 * 전체적이고 간략한 로직만을 담은 클래스
 * (구체적인 방법은 각 인터페이스의 구현 클래스에서 정의)
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

	/* 1. 파일을 읽어온다 */
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}

	/* 2. 읽은파일을 가지고서 , SQL을 찾는다 */
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

}
