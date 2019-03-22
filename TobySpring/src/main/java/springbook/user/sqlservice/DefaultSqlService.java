package springbook.user.sqlservice;

/**
 * @author USER
 * 
 * 매번 의존관계를 설정해주기 귀찮고, 
 * 앞으로도 의존 오브젝트가 바뀔 가능성이 적을 때 사용하는,
 * 외부에서 DI받지 않을 때 자동으로 적용되게끔 직접 주입해주는,
 * "디폴트 의존관계"
 * 
 */
public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		setSqlReader(new JaxbXmlSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}
