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
		Hello hello = new HelloTarget(); // 타깃은 인터페이스로 접근하는 습관을 들이자. 
		assertThat (hello.sayHello("Toby"), is ("Hello Toby"));
		assertThat (hello.sayHi("Toby"), is ("Hi Toby"));
		assertThat (hello.sayThankyou("Toby"), is ("Thank You Toby"));
		
		
		Hello proxiedHello = new HelloUppercase(new HelloTarget()); // 클라이언트에서 위임할 타겟클래스를 지정하여 파라미터로 넣어준다.(일반 프록시 적용.. 다이내믹 프록시 적용x)
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
		
	}

	/* 다이나믹 프록시로 데코레이터 구현. 다이나믹 프록시로 구현하면, 개발자가 위임할 타겟 인터페이스 메서드들을 모두 일일히 구현하지 않아도 된다. */
	@Test
	public void dynamicProxy() {
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {Hello.class}, new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
	}
	
	
	/* 스프링이 제공하는 프록시팩토리빈 */
	@Test
	public void proxyFactoryBean() { 
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget()); // 타겟과..
		pfBean.addAdvice(new UppercaseAdvice()); // 어드바이스(부가기능)을 스프링이 제공하는, 프록시팩토리빈에 설정해준다.
		
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
		// assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));  // 포인트컷에 "sayH*" 만 추가했기 때문에, sayThankyou 메서드 테스트는 실패한다.
		
		
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

