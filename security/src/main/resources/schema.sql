--for PersistentTokenRepository
--https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html#remember-me-persistent-token
create table persistent_logins (username varchar(64) not null,
								series varchar(64) primary key,
								token varchar(64) not null,
								last_used timestamp not null);

--for JdbcUserDetailsManager
--https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/jdbc.html#servlet-authentication-jdbc-schema
create table users (
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(500) not null,
	enabled boolean not null
);
create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

--full custom
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256)
);
CREATE TABLE authority (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(256),
    member_id BIGINT,
    FOREIGN KEY(member_id) REFERENCES member(id)
);