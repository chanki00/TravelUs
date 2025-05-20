package com.DB_PASSWORD_REDACTED.trip.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DBUtil {
	private static final String DRIVERCLASSNAME = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/tripdb?serverTimezone=UTC";
	private static final String USER = "DB_PASSWORD_REDACTED";
	private static final String PASSWORD = "DB_PASSWORD_REDACTED";
	private static DataSource ds;

	static {
		// jdbc 커넥션 설정
		HikariConfig config = new HikariConfig();
		config.setUsername(USER);
		config.setPassword(PASSWORD);
		config.setJdbcUrl(URL);
		config.setDriverClassName(DRIVERCLASSNAME);
	
		// DataSource 설정
		config.setMaximumPoolSize(5);
		config.setMinimumIdle(3);
		config.setConnectionTimeout(1000 * 60 * 10);
		config.setIdleTimeout(1000 * 60 * 5);
		ds = new HikariDataSource(config);
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return ds.getConnection();
	}

	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable res : closeables) {
			try {
				res.close();
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

