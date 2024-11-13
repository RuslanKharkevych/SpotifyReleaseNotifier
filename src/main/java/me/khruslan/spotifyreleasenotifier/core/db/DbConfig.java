package me.khruslan.spotifyreleasenotifier.core.db;

import me.khruslan.spotifyreleasenotifier.release.model.ReleaseDto;
import me.khruslan.spotifyreleasenotifier.user.model.UserDto;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
    private static final String HIBERNATE_PROPERTIES_FILENAME = "hibernate.properties";

    @Bean
    public SessionFactory sessionFactory() {
        var registry = new StandardServiceRegistryBuilder()
                .loadProperties(HIBERNATE_PROPERTIES_FILENAME)
                .build();

        try {
            return new MetadataSources(registry)
                    .addAnnotatedClasses(UserDto.class, ReleaseDto.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }
}
