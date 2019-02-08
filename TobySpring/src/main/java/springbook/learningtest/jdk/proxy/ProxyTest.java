package springbook.learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;


public class ProxyTest {

	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget(); // Ÿ���� �������̽��� �����ϴ� ������ ������. 
		assertThat (hello.sayHello("Toby"), is ("Hello Toby"));
		assertThat (hello.sayHi("Toby"), is ("Hi Toby"));
		assertThat (hello.sayThankyou("Toby"), is ("Thank You Toby"));
		
		
		Hello proxiedHello = new HelloUppercase(new HelloTarget()); // Ŭ���̾�Ʈ���� ������ Ÿ��Ŭ������ �����Ͽ� �Ķ���ͷ� �־��ش�.(�Ϲ� ���Ͻ� ����.. ���̳��� ���Ͻ� ����x)
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
		
	}

	/* ���̳��� ���Ͻ÷� ���ڷ����� ����. ���̳��� ���Ͻ÷� �����ϸ�, �����ڰ� ������ Ÿ�� �������̽� �޼������ ��� ������ �������� �ʾƵ� �ȴ�. */
	@Test
	public void dynamicProxy() {
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {Hello.class}, new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
	}
	
	
	/* �������� �����ϴ� ���Ͻ����丮�� */
	@Test
	public void proxyFactoryBean() { 
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget()); // Ÿ�ٰ�..
		pfBean.addAdvice(new UppercaseAdvice()); // �����̽�(�ΰ����)�� �������� �����ϴ�, ���Ͻ����丮�� �������ش�.
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
	}
	
	@Test
	public void pointcutAdvisor () {
		
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		// assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));  // ����Ʈ�ƿ� "sayH*" �� �߰��߱� ������, sayThankyou �޼��� �׽�Ʈ�� �����Ѵ�.
		
		
	}
	
	static class UppercaseAdvice implements MethodInterceptor { 
		
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String) invocation.proceed();
			return ret.toUpperCase();
		}
	}
	
	
	@Test
	public void classNamePointcutAdvisor() {
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			
			@Override
			public ClassFilter getClassFilter() {
				return new ClassFilter() {

					@Override
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
						
					}
				};
			}
		};
		
		classMethodPointcut.setMappedName("sayH");
		
		checkAdviced(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget{};
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
		class HelloToby extends HelloTarget{};
		checkAdviced(new HelloToby(), classMethodPointcut, true);
	}

	private void checkAdviced(HelloTarget target, Pointcut pointcut, boolean advice) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if(advice) {
			assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
			assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
			assertThat(proxiedHello.sayThankyou("Toby"), is("Thank You Toby"));
		} else {
			assertThat(proxiedHello.sayHello("Toby"),is("Hello Toby"));
			assertThat(proxiedHello.sayHi("Toby"), is("Hi Toby"));
			assertThat(proxiedHello.sayThankyou("Toby"), is("Thank You Toby"));
		}
	}
	
}

