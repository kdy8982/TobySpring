package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class CalcSumTest {

	@Test
	public void sumTest() throws IOException {
		try {
			Calculator calculator = new Calculator();
			int sum = calculator.sum(getClass().getResource("/numbers.txt").getPath());
			int multiple = calculator.multiple(getClass().getResource("/numbers.txt").getPath());
			
			assertThat(sum, is(10));
			assertThat(multiple, is(24));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
