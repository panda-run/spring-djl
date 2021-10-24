package org.pandarun;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 描述:
 *
 * @author James Zow
 * @create 2021/10/24
 */
@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(StartApplication.class).run(args);
    }
}
