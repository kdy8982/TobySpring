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
		
				return lineReadTemplate(filePath, sumCallback, 0); // 템플릿 메소드 호출
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

	/* 가장 처음 만든 fileReaderTemplate */
	public int fileReadTemplate(String filePath, BufferedReaderCallback callback)
			throws NumberFormatException, IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int sum = callback.doSomethingWithReader(br);
		return sum;
	}

	/* 두번째로 만든 템플릿 업그레이드 */
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
