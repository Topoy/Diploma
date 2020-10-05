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
        /*Post post = new Post();
        User user = new User();
        user.setName("German Frolov");
        user.setEmail("german.frolov@gmail.com");
        user.setId(3);
        user.setRegTime(LocalDateTime.of(2020, Month.JUNE, 1, 10, 19));
        post.setUser(user);
        post.setTitle("Третий пост в блоге");
        post.setText("А блог по-маленьку заполняется. Будет интересно посмотреть, что получится после релиза");
        post.setTime(LocalDateTime.of(2020, Month.JULY, 10, 11, 17));
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("#Pretty cool");
        session.save(user);
        session.save(post);
        session.save(tag);*/

        //Post post = session.get(Post.class, 1);
        //post.setModerationStatus(StatusType.NEW);
        //session.save(post);
        //Tag tag = session.get(Tag.class, 3);
        //ArrayList<Post> posts = new ArrayList<>();
        //Post post = session.get(Post.class, 3);
        //posts.add(post);
        //tag.setPosts(posts);
        //session.save(tag);

        transaction.commit();
        sessionFactory.close();
        SpringApplication.run(Main.class, args);
    }
}
