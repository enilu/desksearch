package org.enilu.desksearch;

public class Test {

	public static void main(String[] args) {
		String s = Test.class.getClassLoader().getResource("./").toString()
				+ "conf/conf.properties";
		System.out.println(s);
		System.out.println((System.getProperty("user.dir")));
	}
}
