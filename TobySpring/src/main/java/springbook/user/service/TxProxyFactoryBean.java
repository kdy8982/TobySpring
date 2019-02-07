package springbook.user.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/* �� Ʈ����� ����� �߰����ִ� ���丮 �� */
/* ���Ͻ� ���� ��� �޼���� ������Ʈ�� �ʱ�ȭ�ϱ� ������, �Ϲ����� ������ �� ������ �̿��� �� ���⿡,
 * �������� �����ϴ� ���丮 ���� ����Ѵ�. */
public class TxProxyFactoryBean implements FactoryBean<Object>{
	
	Object target;
	PlatformTransactionManager transactionManager;
	String pattern;
	Class<?> serviceInterface;  // TxProxyFactoryBean�� ����� Bean. 
								// UserService Ÿ�� �̿ܿ��� ����� �� �ֵ���, Class<?>�� �����Ѵ�.

	public void setTarget(Object target) {
		this.target = target;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
	
	
	@Override
	public Object getObject() throws Exception {
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern(pattern);
		
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{serviceInterface}, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}
	
	@Override
	public boolean isSingleton() {
		return false;
	}

}
