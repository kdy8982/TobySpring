package springbook.user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
	
	PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition()); // 트랜잭션 전파 수준 ; 이미 존재하고 있는 트랜잭션이 있다면, 해당 트랜잭션으로 붙는다.
		
		try {
			Object ret = invocation.proceed();
			this.transactionManager.commit(status);
			return ret;
		}
		catch(RuntimeException e) {
			this.transactionManager.rollback(status);

			throw e;
		}
	}

}
