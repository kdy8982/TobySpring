package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public int sum(String filePath) throws IOException {

		LineCallback sumCallback = new LineCallback() {
			@Override
			public int doSomthingWithLine(String line, int value) {
				return value + Integer.valueOf(line);
			}
		}; 
		
				return lineReadTemplate(filePath, sumCallback, 0); // ���ø� �޼ҵ� ȣ��
	}

	public int multiple(String filePath) throws IOException {
		
		LineCallback multipleCallback = new LineCallback() {
			
			@Override
			public int doSomthingWithLine(String line, int initVal) {
				// TODO Auto-generated method stub
				int ret = initVal *= Integer.valueOf(line); 
				return ret;
			}
		};
		
		return lineReadTemplate(filePath, multipleCallback, 1);
	}

	/* ���� ó�� ���� fileReaderTemplate */
	public int fileReadTemplate(String filePath, BufferedReaderCallback callback)
			throws NumberFormatException, IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int sum = callback.doSomethingWithReader(br);
		return sum;
	}

	/* �ι�°�� ���� ���ø� ���׷��̵� */
	public int lineReadTemplate(String filePath, LineCallback callback, int initVal) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int ret = initVal;
		String line = null;
		while ((line = br.readLine()) != null) {
			ret = callback.doSomthingWithLine(line, ret);
		}
		return ret;
	}
}
