package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppBDSqlParametrizado {

    Connection conn;

    private final String DRIVER = "org.postgresql.Driver";
    private final String USER_DB = "root";
    private final String PASS_DB = "root";
    private final String URL_DB = "jdbc:postgresql://172.18.0.2:5432/postgres";

    public AppBDSqlParametrizado(String tableName){
        carregaDriverJDBC();

        conn = getConnection();

        consultaTabela(conn, tableName);
    }

    private void consultaTabela(Connection conn, String tableName) {
        try {
            String strSelect = "select * from " + tableName;
            var statement = conn.prepareStatement(strSelect);
            ResultSet resultSet = statement.executeQuery();
            //statement = conn.prepareStatement(strSelect);
            //resultSet = statement.executeQuery();

            while(resultSet.next()){
                int cols = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    System.out.printf("%-25s |",resultSet.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Erro na execução do SQL " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL_DB, USER_DB, PASS_DB);
        } catch (SQLException e) {
            System.out.println("Erro na conexão ao banco de dados." + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void carregaDriverJDBC() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não carregado. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppBDSqlParametrizado("cliente");
    }
}