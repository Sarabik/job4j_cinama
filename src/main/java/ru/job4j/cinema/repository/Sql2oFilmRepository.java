package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT films.id AS film_id, films.name AS film_name, description, film_year, 
                    genres.name AS film_genre, minimal_age, duration_in_minutes, file_id FROM films
                    JOIN genres ON genres.id = films.genre_id
                    WHERE films.id = :id
                    """;
            Query query = connection.createQuery(str);
            query.addParameter("id", id);
            Film film = query.setColumnMappings(Film.COLUMN_MAPPING)
                    .executeAndFetchFirst(Film.class);
            return Optional.ofNullable(film);
        }
    }

    @Override
    public Collection<Film> findAll() {
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT films.id AS film_id, films.name AS film_name, description, film_year, 
                    genres.name AS film_genre, minimal_age, duration_in_minutes, file_id FROM films
                    JOIN genres ON genres.id = films.genre_id
                    """;
            Query query = connection.createQuery(str);
            Collection<Film> collection = query.setColumnMappings(Film.COLUMN_MAPPING)
                    .executeAndFetch(Film.class);
            return collection;
        }
    }
}
