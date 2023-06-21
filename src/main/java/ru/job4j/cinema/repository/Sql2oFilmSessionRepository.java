package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {

    public final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<FilmSession> getById(int id) {
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT * FROM film_sessions
                    WHERE id = :id
                    """;
            Query query = connection.createQuery(str);
            query.addParameter("id", id);
            FilmSession filmSession = query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetchFirst(FilmSession.class);
            return Optional.ofNullable(filmSession);
        }
    }

    @Override
    public Collection<FilmSession> getAllByFilmId(int filmId) {
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT * FROM film_sessions
                    WHERE film_id = :film_id
                    """;
            Query query = connection.createQuery(str);
            query.addParameter("film_id", filmId);
            Collection<FilmSession> filmSessions = query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetch(FilmSession.class);
            return filmSessions;
        }
    }

    @Override
    public Collection<FilmSession> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * FROM film_sessions");
            Collection<FilmSession> filmSessions = query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetch(FilmSession.class);
            return filmSessions;
        }
    }


}
