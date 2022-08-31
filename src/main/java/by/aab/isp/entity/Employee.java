package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class Employee extends User {

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
