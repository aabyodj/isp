package by.aab.isp.dao;

import by.aab.isp.entity.Entity;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T extends Entity> {

    T save(T entity);
    
    Collection<T> findAll();
    
    Optional<T> findById(long id);

    boolean update(T entity);
}
