package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
// import java.sql.Statement;
import java.sql.SQLException;

public class AppBdCrud {
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String JDBC = "jdbc:postgresql://172.18.0.2:5432/postgres";

    private Connection conn;

    private Produto produto;
    private Marca marca;

    public AppBdCrud(){
        try {
            conn = getConnection();
            listarTodosEstados(conn); 
            ListarEstadoPorUf(conn, "TO");

            marca = new Marca();
            marca.setId(1L);

            produto = new Produto();
            produto.setNome("Produto teste 001");
            produto.setMarca(marca);
            produto.setValor(100);


            inserirProduto(conn, produto);

            consultarTabela(conn, "produto");

        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        new AppBdCrud();
    }

    private void ListarEstadoPorUf(Connection conn, String uf) {
        try {
            // var statement = conn.createStatement();

            /* NUNCA CONCATENE STRING PARA ACESSAR BANCO DE DADOS */
            /*SQL INJECTION PRONE */
            // var resultSet = statement.executeQuery("select * from estado where uf = '" + uf + "'");

            String strSelectPorUf = "select * from estado where uf = ?";
            var statement = conn.prepareStatement(strSelectPorUf);

            statement.setString(1, uf);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                System.out.println("======= Lista Estado por UF ==========");
                System.out.printf("Id: %d | Nome: %-20s | Estado: %s%n"
                , resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("uf"));
            }

        } catch (SQLException e) {
                System.out.println("Erro no comando sql: " 
                + e.getMessage());
                e.printStackTrace();
        }      
    }

    private  void listarTodosEstados(Connection connection) {
        carregarDriveJDBC();
/*
 * Introduzido no java 8, o "try (resource){
 * }" é utlizado para fechar o recurso ao final do processamento. É uma alternativa ao "finally".
 */

        try {
            var statement = conn.createStatement();

            var resultSet = statement.executeQuery("select * from estado");

            while(resultSet.next()){
                System.out.printf("Id: %d | Nome: %-20s | Estado: %s%n"
                , resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("uf"));
            }

        } catch (SQLException e) {
                System.out.println("Erro no comando sql: " 
                + e.getMessage());
                e.printStackTrace();
        }
    }

    private  void carregarDriveJDBC() {
        try {
            Class.forName("org.postgresql.Driver");

            // conn = DriverManager.getConnection("jdbc:postgresql://172.18.0.2:5432/postgres", "root", "root");

           System.out.println("Conexão realizada com sucesso.");
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível carregar a biblioteca para acesso ao banco de dados." +  e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(JDBC, USER_NAME, PASSWORD);
    }

    private void consultarTabela(Connection conn, String tableName) {
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
    
    private void inserirProduto(Connection conn, Produto produto) {
        try {
            String strSql = "insert into Produto (nome, marca_id, valor) values(?, ?, ?)";
            var statement = conn.prepareStatement(strSql);

            statement.setString(1, produto.getNome());
            statement.setLong(2, produto.getMarca().getId());
            statement.setDouble(3, produto.getValor());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro na inserção SQL " + e.getMessage());
            e.printStackTrace();
        }
    }  
}