package springbook.user.sqlservice;

/**
 * @author USER
 * 
 * �Ź� �������踦 �������ֱ� ������, 
 * �����ε� ���� ������Ʈ�� �ٲ� ���ɼ��� ���� �� ����ϴ�,
 * �ܺο��� DI���� ���� �� �ڵ����� ����ǰԲ� ���� �������ִ�,
 * "����Ʈ ��������"
 * 
 */
public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		setSqlReader(new JaxbXmlSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}
