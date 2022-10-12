package by.aab.isp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
@Entity
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Employee extends User {

    @Column(name = "role_id")
    private Role role;

    public enum Role {
        ADMIN("msg.employee.role.admin"),
        MANAGER("msg.employee.role.manager");

        private final String messageKey;

        Role(String messageKey) {
            this.messageKey = messageKey;
        }

        public String getMessageKey() {
            return messageKey;
        }
    }

}
