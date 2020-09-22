package main.model;

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
    @Autowired ImmutableCredentials immutableCredentials;

    @Override
    public void run(String... args) throws Exception
    {
        System.out.println(immutableCredentials.getTitle());
    }
    public static void main(String[] args)
    {
     /*   StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        transaction.commit();
        sessionFactory.close();*/
        SpringApplication.run(Main.class, args);
    }
}
