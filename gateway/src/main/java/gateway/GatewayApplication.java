package gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    private static final Logger log = LoggerFactory.getLogger(GatewayApplication.class);
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class, args);
    }
}
