package br.edu.fatecpg.pokemon.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    public static Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/DB_POKEMON";
        String usuario = "postgres";
        String senha = "Dani4520*";

        return DriverManager.getConnection(url, usuario, senha);
    }
}