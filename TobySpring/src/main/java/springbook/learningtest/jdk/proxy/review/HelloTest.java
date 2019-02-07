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
		hello.sayHello("�뿬");

		assertThat(hello.sayHello("�뿬"), is("Hello �뿬"));
	}

	@Test
	public void sayHi() {
		Hello hello = new HelloTarget();
		hello.sayHi("�뿬");

		assertThat(hello.sayHi("�뿬"), is("Hi �뿬"));
	}

	@Test
	public void sayThankyou() {
		Hello hello = new HelloTarget();
		hello.sayThankyou("�뿬");

		assertThat(hello.sayThankyou("�뿬"), is("Thank you �뿬"));
	}

	@Test
	public void decoratedHello() {
		Hello hello = new decoratedHello(new HelloTarget());
		hello.sayHello("�뿬");

		assertThat(hello.sayHello("�뿬"), is("HELLO �뿬"));
		assertThat(hello.sayHi("�뿬"), is("HI �뿬"));
		assertThat(hello.sayThankyou("�뿬"), is("THANK YOU �뿬"));
	}

	@Test
	public void proxiedHello() {
		
		Hello hello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class }, new helloUpperHandler(new HelloTarget()));
	
		assertThat(hello.sayHello("�뿬"), is("HELLO �뿬"));
		assertThat(hello.sayHi("�뿬"), is("HI �뿬"));
		assertThat(hello.sayThankyou("�뿬"), is("THANK YOU �뿬"));
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

		assertThat(hello.sayHello("�뿬"), is("HELLO �뿬"));
		assertThat(hello.sayHi("�뿬"), is("HI �뿬"));
		assertThat(hello.sayThankyou("�뿬"), is("THANK YOU �뿬"));
	}
	
	static class UppercaseAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String) invocation.proceed();
			return ret.toUpperCase();
		}
		
	}
	
	
}
