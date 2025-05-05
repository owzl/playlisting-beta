package com.playlisting.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/playlisting_beta?serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "playlisting_beta";
    private static final String PASSWORD = "playlisting_beta";

    // JDBC 드라이버 로딩 (클래스가 로드될 때 한 번만 실행)
    static {
        try {
            // MySQL JDBC 드라이버 클래스 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver 로드 성공!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver 로드 실패!");
            e.printStackTrace();
        }
    }

    // DB 연결 가져오기
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 자원 해제 (Connection, Statement, ResultSet)
    // Connection
    public static void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection 닫기 실패!");
            e.printStackTrace();
        }
    }

    // Statement
    public static void close(Statement stmt) {
         try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
                // System.out.println("Statement 닫기 성공!"); // 디버깅용
            }
        } catch (SQLException e) {
            System.err.println("Statement 닫기 실패!");
            e.printStackTrace();
        }
    }

    // ResultSet
    public static void close(ResultSet rs) {
         try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("ResultSet 닫기 실패!");
            e.printStackTrace();
        }
    }

     public static void close(Connection conn, Statement stmt, ResultSet rs) {
         close(rs);
         close(stmt);
         close(conn);
     }

     public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
         close(rs);
         close(pstmt);
         close(conn);
     }

     public static void close(Connection conn, Statement stmt) {
        close(stmt);
        close(conn);
     }

     public static void close(Connection conn, PreparedStatement pstmt) {
        close(pstmt);
        close(conn);
     }

    public static void testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                System.out.println("DB 연결 테스트 성공!");
            }
        } catch (SQLException e) {
            System.err.println("DB 연결 테스트 실패!");
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

     public static void main(String[] args) {
         DBUtil.testConnection();
     }
}
