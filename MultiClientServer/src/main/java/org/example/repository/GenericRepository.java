package org.example.repository;

import org.example.Config.SQLDataSourceConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class GenericRepository<T> {

    protected Connection connection;

    public GenericRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    protected List<T> findAll(String query, Function<ResultSet, T> mapper) throws SQLException {
        List<T> items = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(mapper.apply(rs));
            }
        }
        return items;
    }

    protected T findById(String query, Function<ResultSet, T> mapper, Object... params) throws SQLException {
        T item = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    item = mapper.apply(rs);
                }
            }
        }
        return item;
    }

    protected void update(String query, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    protected void insert(String query, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }
}

