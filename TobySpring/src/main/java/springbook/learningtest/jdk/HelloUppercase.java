package springbook.learningtest.jdk;

public class HelloUppercase implements Hello {

	Hello hello; // 위임할 타깃 오브젝트. 다른 프록시를 추가할 수 있으므로 인터페이스로 접근한다.
	
		
	public HelloUppercase(Hello hello) {
		this.hello = hello;
	}
	
	@Override
	public String sayHello(String name) {
		return hello.sayHello(name).toUpperCase();
	}

	@Override
	public String sayHi(String name) {
		return hello.sayHi(name).toUpperCase();
	}

	@Override
	public String sayThankyou(String name) {
		return hello.sayThankyou(name).toUpperCase();
	}

}
