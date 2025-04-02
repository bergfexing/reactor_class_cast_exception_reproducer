# Reproducer sample for Reactor 3.6+ and TracingLifter issue (ClassCastException)

This sample is created to demonstrate an issue that was found on a SpringBoot 3.4+ reactive application using  MathFlux.
Issue seems to be related to https://github.com/reactor/reactor-core/issues/3762

To trigger the issue the following conditions must be fulfilled:
* a micrometer jar must be in classpath
* a reactor-core 3.6+
* `Hooks.enableAutomaticContextPropagation()` must be called
* `onEachOperator` hook is set up and provided a function that sets up tracing [lift operator](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/26f5a5ef2a66f6e0799f0a9819a64568507f43d1/instrumentation/reactor/reactor-3.1/library/src/main/java/io/opentelemetry/instrumentation/reactor/v3_1/ContextPropagationOperator.java#L186).
* MathFlux.sumInt() must be called

Starting the sample:

```
$ mvn spring-boot:run
$ curl http://localhost:8080/sum
```

## Expected behaviour
* Sample throws no exception

## Actual behaviour
java.lang.ClassCastException exception is thrown 

```
 java.lang.ClassCastException: class reactor.core.publisher.FluxContextWriteRestoringThreadLocals$ContextWriteRestoringThreadLocalsSubscriber cannot be cast to class reactor.core.Fuseable$QueueSubscription (reactor.core.publisher.FluxContextWriteRestoringThreadLocals$ContextWriteRestoringThreadLocalsSubscriber and reactor.core.Fuseable$QueueSubscription are in unnamed module of loader 'app')
	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onSubscribe(FluxMapFuseable.java:264) ~[reactor-core-3.7.4.jar:3.7.4]
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
Error has been observed at the following site(s):
	*__checkpoint  Handler com.example.ReactorMathReproducer.DemoController#sum() [DispatcherHandler]
	*__checkpoint  HTTP GET "/sum" [ExceptionHandlingWebHandler]
Original Stack Trace:
		at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onSubscribe(FluxMapFuseable.java:264) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.FluxContextWriteRestoringThreadLocals$ContextWriteRestoringThreadLocalsSubscriber.onSubscribe(FluxContextWriteRestoringThreadLocals.java:104) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.FluxHide$SuppressFuseableSubscriber.onSubscribe(FluxHide.java:122) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.FluxContextWriteRestoringThreadLocals$ContextWriteRestoringThreadLocalsSubscriber.onSubscribe(FluxContextWriteRestoringThreadLocals.java:104) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.MonoContextWriteRestoringThreadLocals$ContextWriteRestoringThreadLocalsSubscriber.onSubscribe(MonoContextWriteRestoringThreadLocals.java:95) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.math.MathSubscriber.onSubscribe(MathSubscriber.java:37) ~[reactor-extra-3.5.2.jar:3.5.2]
		at reactor.core.publisher.FluxContextWriteRestoringThreadLocalsFuseable$FuseableContextWriteRestoringThreadLocalsSubscriber.onSubscribe(FluxContextWriteRestoringThreadLocalsFuseable.java:105) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.FluxArray.subscribe(FluxArray.java:50) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.FluxArray.subscribe(FluxArray.java:59) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.core.publisher.InternalFluxOperator.subscribe(InternalFluxOperator.java:68) ~[reactor-core-3.7.4.jar:3.7.4]
		at reactor.math.MonoSumInt.subscribe(MonoSumInt.java:42) ~[reactor-extra-3.5.2.jar:3.5.2]
		at reactor.core.publisher.MonoContextWriteRestoringThreadLocals.subscribe(MonoContextWriteRestoringThreadLocals.java:44) ~[reactor-core-3.7.4.jar:3.7.4]
```

## Notes
* Calling `Hooks.onOperatorDebug()` "fixes" the problem, since lift is wrapped in another object.
