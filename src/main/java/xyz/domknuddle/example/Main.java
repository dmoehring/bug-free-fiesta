package xyz.domknuddle.example;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		File personFile = new File("src/main/resources/Personen.txt");
		new TimothysReader().readWithAnnotation(PersonBean.class, personFile).forEach(System.out::println);
	}

}
