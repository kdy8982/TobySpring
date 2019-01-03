package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public int sum(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String line = "";
		int sum = 0;

		while ((line = br.readLine()) != null) {
			sum += Integer.valueOf(line);
		}

		return sum;

	}

}
