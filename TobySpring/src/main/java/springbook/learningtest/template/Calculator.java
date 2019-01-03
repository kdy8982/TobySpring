package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public int sum(String filePath) throws IOException {

		BufferedReaderCallback sumCallBack = new BufferedReaderCallback() {
			
			@Override
			public int doSomethingWithReader(BufferedReader br) throws NumberFormatException, IOException {
				String line = "";
				int sum = 0;

				while ((line = br.readLine()) != null) {
					sum += Integer.valueOf(line);
				}

				return sum;
			}
		}; 
		
		return fileReadTemplate(filePath, sumCallBack); // 템플릿 메소드 호출
	}
	
	public int multiple(String filePath) throws IOException {

		BufferedReaderCallback multipleCallBack = new BufferedReaderCallback() {
			
			@Override
			public int doSomethingWithReader(BufferedReader br) throws NumberFormatException, IOException {
				String line = "";
				int ret = 1;

				while ((line = br.readLine()) != null) {
					ret *= Integer.valueOf(line);
				}

				return ret;
			}
		}; 
		
		return fileReadTemplate(filePath, multipleCallBack); // 템플릿 메소드 호출
	}
	
	
	public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws NumberFormatException, IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int sum = callback.doSomethingWithReader(br);
		return sum;
	}

}
