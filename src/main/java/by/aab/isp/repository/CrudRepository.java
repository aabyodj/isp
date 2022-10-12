package by.aab.isp.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    T save(T entity);

    long count();

    List<T> findAll();

    List<T> findAll(OrderOffsetLimit orderOffsetLimit);

    Optional<T> findById(long id);

    void update(T entity);
}
