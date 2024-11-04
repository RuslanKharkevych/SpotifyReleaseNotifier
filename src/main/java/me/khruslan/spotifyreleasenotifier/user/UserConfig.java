package me.khruslan.spotifyreleasenotifier.user;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    SessionFactory sessionFactory() {
        var registry = new StandardServiceRegistryBuilder()
                .loadProperties("hibernate.properties")
                .build();

        try {
            return new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }
}
