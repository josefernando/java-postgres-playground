package com.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AppBD {
    public static void main(String[] args) throws SQLException {
        // Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");

            // conn = DriverManager.getConnection("jdbc:postgresql://172.18.0.2:5432/postgres", "root", "root");

            System.out.println("Conexão realizada com sucesso.");
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível carregar a biblioteca para acesso ao banco de dados." +  e.getMessage());
            e.printStackTrace();
        }

        Statement statement = null;
/*
 * Introduzido no java 8, o "try (resource){
 * }" é utlizado para fechar o recurso ao final do processamento. É uma alternativa ao "finally".
 */

        try (
            var conn = DriverManager.getConnection("jdbc:postgresql://172.18.0.2:5432/postgres", "root", "root")){
                
            statement = conn.createStatement();

            var resultSet = statement.executeQuery("select * from estado");

            while(resultSet.next()){
                System.out.printf("Id: %d | Nome: %-20s | Estado: %s%n"
                , resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("uf"));
            }

        } catch (SQLException e) {
            if(statement != null){
                System.out.println("Erro no comando sql: " 
                + e.getMessage());
                e.printStackTrace();
            } else {
                System.out.println("Erro ao conectar o banco de dados: " + e.getMessage());
            }
        } 
    }
}