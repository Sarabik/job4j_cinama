package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;

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

        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @Test
    public void whenFindByIdThenFindIt() {
        File expectedFile = new File("001.jpg", "src/main/resources/files/001.jpg");
        expectedFile.setId(1);

        File actualFile = sql2oFileRepository.findById(1).get();

        assertThat(actualFile).usingRecursiveComparison().isEqualTo(expectedFile);
    }

    @Test
    public void whenDidNotFindById() {
        Optional<File> actualFile = sql2oFileRepository.findById(10);
        assertThat(actualFile).isEmpty();
    }
}