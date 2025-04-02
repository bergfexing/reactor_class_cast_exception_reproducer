package com.example.ReactorMathReproducer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

@RestController
public class DemoController {

    @GetMapping("/sum")
    public Mono<Integer> sum() {
        Flux<Integer> numbers = Flux.just(1, 2);
        return MathFlux.sumInt(numbers);
    }
}
