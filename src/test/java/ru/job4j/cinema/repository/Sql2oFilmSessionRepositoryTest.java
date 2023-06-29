package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.FilmSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

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
        Sql2o sql2o = configuration.databaseClient(datasource);

        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
    }

    @Test
    public void whenGetByIdThenFindIt() {
        LocalDateTime start = LocalDateTime.of(2023, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 1, 12, 15);
        FilmSession expectedFilmSession = new FilmSession(1, 4, 1, start, end, 6);

        FilmSession actualFilmSession = sql2oFilmSessionRepository.getById(1).get();

        assertThat(actualFilmSession).usingRecursiveComparison().isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenDidNotGetById() {
        Optional<FilmSession> actualFilmSession = sql2oFilmSessionRepository.getById(30);
        assertThat(actualFilmSession).isEmpty();
    }

    @Test
    public void whenGetAllSessionsByFilmId() {
        Collection<FilmSession> collection = sql2oFilmSessionRepository.getAllByFilmId(1);
        assertThat(collection.size()).isEqualTo(2);
        for (FilmSession filmSession : collection) {
            assertThat(filmSession.getFilmId()).isEqualTo(1);
        }
    }

    @Test
    public void whenFindAllThenGetCollection() {
        Collection<FilmSession> collection = sql2oFilmSessionRepository.findAll();
        assertThat(collection.size()).isEqualTo(9);
    }
}