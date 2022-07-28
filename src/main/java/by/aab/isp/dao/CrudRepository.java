package by.aab.isp.dao;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T> {
    
    Collection<T> findAll();
    
    Optional<T> findById(long id);
    
    T save(T object);
    
}
