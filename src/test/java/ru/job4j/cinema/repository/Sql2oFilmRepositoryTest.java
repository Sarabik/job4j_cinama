package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;

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

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    @Test
    public void whenFindByIdThenFindIt() {
        String str = "Sauron forces have laid siege to Minas Tirith, the capital of Gondor, in their efforts to eliminate the race of men. The once-great kingdom, watched over by a fading steward, has never been in more desperate need of its king. But can Aragorn answer the call of his heritage and become what he was...";
        Film expectedFilm = new Film(1, "The Lord of the Rings: The Return of the King", str, 2003,
                "Fantasy", 13, 200, 1);

        Film actualFilm = sql2oFilmRepository.findById(1).get();

        assertThat(actualFilm).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenDidNotFindById() {
        Optional<Film> actualFilm = sql2oFilmRepository.findById(10);
        assertThat(actualFilm).isEmpty();
    }

    @Test
    public void whenFindAllThenGetCollection() {
        Collection<Film> collection = sql2oFilmRepository.findAll();
        assertThat(collection.size()).isEqualTo(5);
    }
}