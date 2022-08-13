package by.aab.isp.dao;

import by.aab.isp.entity.Entity;

import java.util.Optional;

public interface CrudRepository<T extends Entity> {

    void init();

    T save(T entity);

    long count();
    
    Iterable<T> findAll();
    
    Optional<T> findById(long id);

    void update(T entity);
}
