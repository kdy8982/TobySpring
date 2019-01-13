package springbook.learningtest.jdk;

import org.junit.Test;
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
}

