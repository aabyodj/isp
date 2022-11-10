package by.aab.isp.service.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import by.aab.isp.repository.entity.Customer;
import by.aab.isp.repository.entity.Employee;
import by.aab.isp.repository.entity.User;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class AppUserDetails implements UserDetails {

    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final long id;
    private final String email;
    private final String passwordHash;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.authorities = toGrantedAuthorities(user);
        this.passwordHash = new String(user.getPasswordHash());
    }

    public long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @ToString.Include(name = "password")
    private String getHiddenPassword() {
        return "[PROTECTED]";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @ToString.Include(name = "username")
    private String getHiddenUsername() {
        return "[PROTECTED]";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static final Collection<? extends GrantedAuthority> GA_ROLE_ADMIN = List.of(() -> ROLE_ADMIN);
    private static final Collection<? extends GrantedAuthority> GA_ROLE_MANAGER = List.of(() -> ROLE_MANAGER);
    private static final Collection<? extends GrantedAuthority> GA_ROLE_CUSTOMER = List.of(() -> ROLE_CUSTOMER);

    private static Collection<? extends GrantedAuthority> toGrantedAuthorities(User user) {
        if (user instanceof Customer) {
            return GA_ROLE_CUSTOMER;
        } else if (user instanceof Employee employee) {
            if (employee.getRole() == Employee.Role.ADMIN) {
                return GA_ROLE_ADMIN;
            } else if (employee.getRole() == Employee.Role.MANAGER) {
                return GA_ROLE_MANAGER;
            }
        }
        throw new IllegalArgumentException("Could not resolve an AuthorityRole for " + user);
    }

}
