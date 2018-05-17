package org.iata.bsplink.test.dummy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dummy controller with test purposes.
 */
@RestController
public class DummyExceptionController {

    @GetMapping("/exception")
    public ResponseEntity<String> index() throws Exception {

        throw new RuntimeException("foo");
    }
}
