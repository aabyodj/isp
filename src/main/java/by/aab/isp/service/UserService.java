package by.aab.isp.service;

import by.aab.isp.entity.User;

public interface UserService {
    Iterable<User> getAll();

    User getById(long id);

    User save(User user);

    User login(String email, String password);
}
