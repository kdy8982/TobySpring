package springbook.learningtest.jdk.proxy.review;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

public class HelloTest {

	@Test
	public void sayHello() {
		Hello hello = new HelloTarget();
		hello.sayHello("대연");

		assertThat(hello.sayHello("대연"), is("Hello 대연"));
	}

	@Test
	public void sayHi() {
		Hello hello = new HelloTarget();
		hello.sayHi("대연");

		assertThat(hello.sayHi("대연"), is("Hi 대연"));
	}

	@Test
	public void sayThankyou() {
		Hello hello = new HelloTarget();
		hello.sayThankyou("대연");

		assertThat(hello.sayThankyou("대연"), is("Thank you 대연"));
	}

	@Test
	public void decoratedHello() {
		Hello hello = new decoratedHello(new HelloTarget());
		hello.sayHello("대연");

		assertThat(hello.sayHello("대연"), is("HELLO 대연"));
		assertThat(hello.sayHi("대연"), is("HI 대연"));
		assertThat(hello.sayThankyou("대연"), is("THANK YOU 대연"));
	}

	@Test
	public void proxiedHello() {
		
		Hello hello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class }, new helloUpperHandler(new HelloTarget()));
	
		assertThat(hello.sayHello("대연"), is("HELLO 대연"));
		assertThat(hello.sayHi("대연"), is("HI 대연"));
		assertThat(hello.sayThankyou("대연"), is("THANK YOU 대연"));
	}

	static class helloUpperHandler implements InvocationHandler {
		Object target;

		public helloUpperHandler(Hello target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object ret = method.invoke(target, args);

			if (ret instanceof String && method.getName().startsWith("say")) {
				return ((String) ret).toUpperCase();
			} else {
				return ret;
			}
		}
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello hello = (Hello) pfBean.getObject();

		assertThat(hello.sayHello("대연"), is("HELLO 대연"));
		assertThat(hello.sayHi("대연"), is("HI 대연"));
		assertThat(hello.sayThankyou("대연"), is("THANK YOU 대연"));
	}
	
	static class UppercaseAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String) invocation.proceed();
			return ret.toUpperCase();
		}
		
	}
	
	
}
