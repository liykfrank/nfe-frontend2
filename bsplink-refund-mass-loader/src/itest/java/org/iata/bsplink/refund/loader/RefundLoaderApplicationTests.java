package org.iata.bsplink.refund.loader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
// We only want to test the context loading but we do not want
// the job to be launched so Spring Batch is disabled.
@SpringBootTest(properties = "spring.batch.job.enabled = false")
public class RefundLoaderApplicationTests {

    @Test
    public void contextLoads() {
        // tests the context load
    }

}
