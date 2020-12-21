package main;

import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan(basePackageClasses = main.controller.ApiGeneralController.class)
@ComponentScan(basePackageClasses = main.controller.ApiPostController.class)
@EnableJpaRepositories(basePackages = "main/repository")
public class Main implements CommandLineRunner
{
    @Autowired
    ImmutableCredentials immutableCredentials;


    @Override
    public void run(String... args) throws Exception
    {
        System.out.println(immutableCredentials.getTitle());
    }
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }
}
