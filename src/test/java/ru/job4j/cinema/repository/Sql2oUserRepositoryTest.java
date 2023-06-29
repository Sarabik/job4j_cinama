package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFileRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @BeforeEach
    public void deleteAllUsers() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("delete from users");
            query.executeUpdate();
        }
    }

    @Test
    public void whenSaveUserThenFindIt() {
        User expectedUser = new User(1, "name", "mail@mail.ru", "password");

        User actualUser = sql2oUserRepository.save(expectedUser).get();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);

        User foundUser = sql2oUserRepository.findByEmailAndPassword("mail@mail.ru", "password").get();
        assertThat(foundUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void whenDidNotFoundUser() {
        Optional<User> foundUser = sql2oUserRepository.findByEmailAndPassword("mail@mail.ru", "password");
        assertThat(foundUser).isEmpty();
    }
}