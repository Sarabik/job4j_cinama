package ru.job4j.cinema.repository;

import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.Optional;

@Service
public class Sql2oHallRepository implements HallRepository {

    public final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> getById(int id) {
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT id, name, row_count, place_count, description
                    FROM halls
                    WHERE id = :id
                    """;
            Query query = connection.createQuery(str);
            query.addParameter("id", id);
            Hall hall = query.setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }
}
