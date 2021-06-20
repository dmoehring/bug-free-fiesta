package xyz.domknuddle.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CSVReadable {

	String[] headers();

	boolean optional() default false;

	String formatter() default "";

}
