package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

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

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @BeforeEach
    public void deleteAllTickets() {
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
        User user = new User(1, "name", "mail@mail.ru", "password");
        sql2oUserRepository.save(user);
        try (Connection connection = sql2o.open()) {
            String sql = "ALTER TABLE tickets ALTER COLUMN id RESTART WITH 1";
            Query query = connection.createQuery("delete from tickets");
            query.executeUpdate();
            Query query1 = connection.createQuery(sql);
            query1.executeUpdate();
        }
    }

    @AfterEach
    public void deleteUser() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("delete from users");
            query.executeUpdate();
        }
    }

    @Test
    public void whenSaveTicketThenFindIt() {
        Ticket expectedTicket = new Ticket(1, 1, 5, 5, 1);

        Ticket actualTicket = sql2oTicketRepository.save(expectedTicket).get();
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(expectedTicket);

        Ticket foundTicket = sql2oTicketRepository.findById(1).get();
        assertThat(foundTicket).usingRecursiveComparison().isEqualTo(expectedTicket);
    }

    @Test
    public void whenDidNotFoundTicket() {
        Optional<Ticket> foundTicket = sql2oTicketRepository.findById(1);
        assertThat(foundTicket).isEmpty();
    }
}