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

            stmt.setInt(1, pokemon.getId());
            stmt.setString(2, pokemon.getName());
            stmt.setString(3, obterNomeTipo(pokemon.getTypes()));
            stmt.setDouble(4, pokemon.getHeight());
            stmt.setDouble(5, pokemon.getWeight());
            stmt.setDouble(6, pokemon.getBase_experience());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Pokemon ja favoritado!");
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
        Pokemon pokemon = new Pokemon();
        pokemon.setId(rs.getInt("id_pokemon"));
        pokemon.setName(rs.getString("nome"));
        pokemon.setTypes(criarTipos(rs.getString("tipo")));
        pokemon.setHeight(rs.getDouble("altura"));
        pokemon.setWeight(rs.getDouble("peso"));
        pokemon.setBase_experience(rs.getDouble("experiencia_base"));
        return pokemon;
    }

    private List<TypeSlot> criarTipos(String nomeTipo) {
        TypeInfo typeInfo = new TypeInfo();
        typeInfo.setName(nomeTipo);

        TypeSlot typeSlot = new TypeSlot();
        typeSlot.setSlot(1);
        typeSlot.setType(typeInfo);

        List<TypeSlot> types = new ArrayList<>();
        types.add(typeSlot);
        return types;
    }

    private String obterNomeTipo(List<TypeSlot> types) {
        if (types == null || types.isEmpty() || types.get(0).getType() == null) {
            return "desconhecido";
        }
        return types.get(0).getType().getName();
    }
}
