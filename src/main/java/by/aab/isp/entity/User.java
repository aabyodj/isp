package by.aab.isp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @ToString.Include(name = "email")
    private String getHiddenEmail() {
        return "[PROTECTED]";
    }

    @Column(name = "password_hash", nullable = false)
    private byte[] passwordHash;

    @ToString.Include(name = "passwordHash")
    private String getHiddenPasswordHash() {
        return "[PROTECTED]";
    }

    @Column(name = "active", nullable = false)
    private boolean active;

}
