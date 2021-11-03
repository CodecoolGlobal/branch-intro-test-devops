package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class OrderDaoJDBC implements OrderDao {

    private final DataSource dataSource;
    private static OrderDao instance;
    private final ProductMapper productMapper = new ProductMapper();

    private OrderDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static OrderDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new OrderDaoJDBC(dataSource);
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        Map<String, String> data = order.getRelevantInformation();
        try (Connection conn = dataSource.getConnection()) {
            String firstSql;
            String secondSql;
            PreparedStatement firstStatement;
            int id;
            if (data.get("valid").equals("true")) {
                firstSql = "INSERT INTO valid_order (first_name, last_name, country, city, address, user_id) VALUES (?, ?, ?, ?, ?, ?)";
                firstStatement = conn.prepareStatement(firstSql, Statement.RETURN_GENERATED_KEYS);
                firstStatement.setString(1, data.get("first_name"));
                firstStatement.setString(2, data.get("last_name"));
                firstStatement.setString(3, data.get("country"));
                firstStatement.setString(4, data.get("city"));
                firstStatement.setString(5, data.get("address"));
                firstStatement.setInt(6, Integer.parseInt(data.get("user_id")));
                firstStatement.executeUpdate();
                secondSql = "INSERT INTO public.order (valid, cart_id, cart_data, valid_id) VALUES (true, ?, ?, ?)";
            } else {
                firstSql = "INSERT INTO invalid_order (message) VALUES (?)";
                firstStatement = conn.prepareStatement(firstSql, Statement.RETURN_GENERATED_KEYS);
                firstStatement.setString(1, data.get("message"));
                firstStatement.executeUpdate();
                secondSql = "INSERT INTO public.order (valid, cart_id, cart_data, invalid_id) VALUES (false, ?, ?, ?)";
            }
            ResultSet keys = firstStatement.getGeneratedKeys();
            id = keys.getInt(1);
            PreparedStatement secondStatement = conn.prepareStatement(secondSql);
            secondStatement.setInt(1, Integer.parseInt(data.get("cart_id")));
            secondStatement.setString(2, data.get("cart_data"));
            secondStatement.setInt(2, id);
            secondStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding order");
        }
    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }
}
