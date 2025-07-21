package com.shermatov.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        // Given
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getInt("age")).thenReturn(25);

        CustomerRowMapper rowMapper = new CustomerRowMapper();

        // When
        Customer actual = rowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1, "John", "john@example.com", 25);
        assert actual != null;
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo("John");
        assertThat(actual.getEmail()).isEqualTo("john@example.com");
        assertThat(actual.getAge()).isEqualTo(25);
    }
}