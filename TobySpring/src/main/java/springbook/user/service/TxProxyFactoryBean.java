package springbook.user.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/* 빈에 트랜잭션 기능을 추가해주는 팩토리 빈 */
/* 프록시 같은 경우 메서드로 오브젝트를 초기화하기 때문에, 일반적인 스프링 빈 설정을 이용할 수 없기에,
 * 스프링이 제공하는 팩토리 빈을 사용한다. */
public class TxProxyFactoryBean implements FactoryBean<Object>{
	
	Object target;
	PlatformTransactionManager transactionManager;
	String pattern;
	Class<?> serviceInterface;  // TxProxyFactoryBean이 만드는 Bean. 
								// UserService 타깃 이외에도 사용할 수 있도록, Class<?>로 선언한다.

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
