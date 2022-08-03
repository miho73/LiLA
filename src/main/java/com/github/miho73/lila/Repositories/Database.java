package com.github.miho73.lila.Repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;

@Repository
public class Database {
    private DriverManagerDataSource dataSource;

    @Value("${lila.database.connection.url}") private String DB_URL;
    @Value("${lila.database.connection.name}") private String DB_USERNAME;
    @Value("${lila.database.connection.password}") private String DB_PASSWORD;

    public Connection openConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public Connection openConnectionForEdit() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }
    public void commit(Connection connection) throws SQLException {
        connection.commit();
    }
    public void commitAndClose(Connection connection) throws SQLException {
        connection.commit();
        connection.close();
    }
    public void rollback(Connection connection) throws SQLException {
        connection.rollback();
    }
    public void rollbackAndClose(Connection connection) throws SQLException {
        connection.rollback();
        connection.close();
    }
    public void close(Connection connection) throws SQLException {
        connection.close();
    }

    @PostConstruct
    public void initRepository() {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
    }
}
