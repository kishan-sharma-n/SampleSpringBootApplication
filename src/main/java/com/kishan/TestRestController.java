package com.kishan;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@RefreshScope
@RestController
public class TestRestController {

	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@Value("${message}")
	private String message = "message";

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

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

}
