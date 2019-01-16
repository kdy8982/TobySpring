package springbook.learningtest.jdk.proxy;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

public class ReflectionTest {
	
	
	/* java.lang.reflection Ŭ���� ��� ��� (���̳��� ���Ͻø� ���� ��, ���÷��� ����� ����Ѵ�) */
	@Test
	public void inovkeMethod () throws Exception {
		String name = "Spring";
		
		//length()
		assertThat (name.length(), is(6));
		
		Method lengthMethod = String.class.getMethod("length");
		assertThat (lengthMethod.invoke(name) , is(6));
		
		//charAt()
		assertThat(name.charAt(0), is('S'));
		
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat(charAtMethod.invoke(name, 0), is('S'));
		
	}

}
