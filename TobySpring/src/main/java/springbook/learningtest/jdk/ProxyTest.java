package springbook.learningtest.jdk;

import org.junit.Test;
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
}

