package springbook.learningtest.jdk.proxy.review;

public class decoratedHello implements Hello{
	HelloTarget helloTarget ;
	
	public decoratedHello(HelloTarget helloTarget) {
		this.helloTarget = helloTarget;
	}
	
	public void setHelloTarget(HelloTarget helloTarget) {
		this.helloTarget = helloTarget;
	}

	@Override
	public String sayHello(String name) {
		return helloTarget.sayHello(name).toUpperCase();
	}

	@Override
	public String sayHi(String name) {
		return helloTarget.sayHi(name).toUpperCase();
	}

	@Override
	public String sayThankyou(String name) {
		return helloTarget.sayThankyou(name).toUpperCase();
	}
	
	
}
