package com.shermatov.customer;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDAOService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;


    public CustomerJDBCDAOService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * from customer
                """;

        return jdbcTemplate.query(sql,(customerRowMapper));

    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT * from customer WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(email)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return  count != null && count > 0;
    }

    @Override
    public boolean existsCustomerWithID(Integer customerId) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);

        return  count != null && count > 0;
    }

    @Override
    public void updateCustomer(Customer update) {
        if(update.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, update.getName(), update.getId());
            System.out.println("jdbcTemplate.update: " + result);
        }

        if(update.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, update.getEmail(), update.getId());
            System.out.println("jdbcTemplate.update: " + result);
        }
        if(update.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(sql, update.getAge(), update.getId());
            System.out.println("jdbcTemplate.update: " + result);
        }

    }
}
