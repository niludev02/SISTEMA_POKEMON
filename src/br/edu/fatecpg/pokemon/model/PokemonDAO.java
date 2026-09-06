package br.edu.fatecpg.pokemon.model;

import br.edu.fatecpg.pokemon.data.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {
    public void criar(Pokemon pokemon) throws SQLException {
        String query = """
                INSERT INTO pokemons
                    (id_pokemon, nome, tipo, altura, peso, experiencia_base)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Conexao.conectar();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, pokemon.getId_pokemon());
            stmt.setString(2, pokemon.getNome());
            stmt.setString(3, pokemon.getTipo());
            stmt.setDouble(4, pokemon.getAltura());
            stmt.setDouble(5, pokemon.getPeso());
            stmt.setDouble(6, pokemon.getExperiencia_base());
            stmt.executeUpdate();
        }
    }

    public List<Pokemon> listar() throws SQLException {
        List<Pokemon> pokemon = new ArrayList<>();
        String query = "SELECT * FROM pokemons ORDER BY id";

        try (Connection connection = Conexao.conectar();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pokemon.add(mapearPokemon(rs));
            }

        }
        return pokemon;

    }

    public void excluir(int pokemonId) throws SQLException {
        String query = "DELETE FROM pokemons WHERE id_pokemon = ?";
        try (Connection connection = Conexao.conectar();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pokemonId);
            stmt.executeUpdate();
        }
    }

    private Pokemon mapearPokemon(ResultSet rs) throws SQLException {
        return new Pokemon(
                rs.getInt("id_pokemon"),
                rs.getString("nome"),
                rs.getString("tipo"),
                rs.getDouble("altura"),
                rs.getDouble("peso"),
                rs.getDouble("experiencia_base")
        );
    }
}
