# Internet Server Provider

## Tutorial Project

**Customer** views and replenishes their account balance, observes traffic consumption,
changes their credentials and tariff plan.

**Manager** manages customers accounts, tariff plans (modifies and adds new ones),
announces discounts and promotions etc; can block *customers*.

## Functional requirements

- [x] Authentication (*sign in* and *sign out*)
- [x] Passwords are hashed before being persisted
- [x] *Add an entity* feature
- [x] *View entities* feature
- [x] *Modify an entity* feature
- [x] *Remove an entity* feature
- [ ] *Customers* can pay on credit
- [x] The application interface is internationalized

### Customers

- [x] View their account balance and traffic
- [x] Replenish their balance
- [x] Change their *tariff plan*
- [x] Update their credentials

### Managers

- [x] Manage *customers* accounts (create, modify, block and unblock)
- [x] Manage *tariff plans* (create and modify)
- [x] Manage *discounts and promotions* (create, modify, stop)
- [x] Update their credentials

### Administrators

- [x] Have all the *managers* authorities
- [x] Manage other *administrators* and *managers* accounts (create, modify, change role, block, unblock)
- [x] The last active *administrator* cannot be blocked or demoted

### Anonymous users

- [x] View actual tariff plans and discounts
- [ ] Submit a registration request

## Nonfunctional requirements

- [x] Spring Boot
- [x] Spring JPA
- [x] Spring MVC
- [x] Spring Security
- [x] Spring i18n
- [x] Spring Aspects
- [x] 3-tier architecture
- [x] Database schema comprises at least 6 tables
- [x] DTO pattern
- [x] Log4J2/SLF4J
- [x] Custom annotations
- [x] Make use of HTTP Session
- [x] Make use of HTTP Filter or HandlerInterceptor
- [x] JSP scriptlets are prohibited
- [x] Any frontend technology is allowed (JavaScript, AJAX etc)
- [x] Duplicate HTTP request protection
- [x] HTML and JavaScript injection protection
- [x] Pagination
- [x] Client-side validation
- [x] Server-side validation
- [ ] Javadoc
- [x] Java Code Convention
- [ ] JUnit
- [x] Git