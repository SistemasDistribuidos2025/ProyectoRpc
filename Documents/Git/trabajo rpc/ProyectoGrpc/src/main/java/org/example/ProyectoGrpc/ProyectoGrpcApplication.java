package org.example.ProyectoGrpc;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = {"org.example.ProyectoGrpc", "com.myorg.kafka_module"})
public class ProyectoGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoGrpcApplication.class, args);
	}
}
