package main.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

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
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Post post = session.get(Post.class, 3);
        post.setModerationStatus(StatusType.NEW);
        session.save(post);

        transaction.commit();
        sessionFactory.close();
        SpringApplication.run(Main.class, args);
    }
}
