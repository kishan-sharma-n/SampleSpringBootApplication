package com.kishan;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@RefreshScope
@RestController
public class TestRestController {

	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@Value("${message}")
	private String message = "message";
	
	@Autowired
    //private RestTemplate restTemplate = new RestTemplate();
    private RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/test/rest", method = RequestMethod.GET)
	public @ResponseBody TestObject sayHello(
			@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		logger.debug("Entering and exiting sayHello");
		System.out.println("message-->" + message);
		return new TestObject(String.format(template, name) + " ," + message, counter.incrementAndGet());

	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody TestObject sayHello2() {
		logger.debug("Entering and exiting sayHello2");
		System.out.println("message-->" + message);
		
		ResponseEntity<TestObject> exchange =
                this.restTemplate.exchange(
                        "http://configuration/test/rest?name={name}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<TestObject>() {
                        },
                        (Object) "fromEureka");
		
		return exchange.getBody();

	}
	
	
	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

}

/*@Component
class RestTemplateExample implements CommandLineRunner {


    @Override
    public void run(String... strings) throws Exception {
        // use the "smart" Eureka-aware RestTemplate
        ResponseEntity<TestObject> exchange =
                this.restTemplate.exchange(
                        "http://configuration/{userId}/bookmarks",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<TestObject>() {
                        },
                        (Object) "mstine");

        System.out.println("1--->"+exchange.getBody().toString());
    }

}*/
