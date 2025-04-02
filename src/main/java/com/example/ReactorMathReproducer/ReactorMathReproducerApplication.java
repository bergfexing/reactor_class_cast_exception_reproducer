package com.example.ReactorMathReproducer;

import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

import java.util.function.Function;

@SpringBootApplication
public class ReactorMathReproducerApplication {

	private static  <T> Function<? super Publisher<T>, ? extends Publisher<T>> tracingLift() {
		return Operators.lift((a, b) -> b);
	}

	public static void main(String[] args) {
		Hooks.onEachOperator("testTracingLift", tracingLift());
		Hooks.enableAutomaticContextPropagation();
		//Hooks.onOperatorDebug();

		SpringApplication.run(ReactorMathReproducerApplication.class, args);
	}

}
