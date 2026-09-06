package br.edu.fatecpg.pokemon.view;
import br.edu.fatecpg.pokemon.data.Conexao;
import br.edu.fatecpg.pokemon.model.PokemonDAO;
import br.edu.fatecpg.pokemon.model.Pokemon;
import br.edu.fatecpg.pokemon.services.PokeAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PokemonDAO pokemonDAO = new PokemonDAO();
    private static final PokeAPI pokeAPI = new PokeAPI();

    public static void main(String[] args) {
        try {
                criarTabelas();
                System.out.println("Banco conectado e tabelas prontas.");
                executarMenu();
        } catch (SQLException e) {
                System.err.println("Nao foi possivel iniciar o sistema: " + e.getMessage());
                System.err.println("Verifique se o PostgreSQL esta ativo e se os dados em Conexao.java estao corretos.");
        } finally {
            scanner.close();
        }
    }

    public static void criarTabelas() throws SQLException {
        try (Connection conexao = Conexao.conectar();
            Statement statement = conexao.createStatement())   {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS pokemons (
                                id SERIAL PRIMARY KEY,
                                id_pokemon INTEGER NOT NULL,
                                nome VARCHAR(100) NOT NULL,
                                tipo VARCHAR(30) NOT NULL,
                                altura INTEGER NOT NULL,
                                peso INTEGER NOT NULL,
                                experiencia_base INTEGER NOT NULL
                    );
            """);
        }
    }

    private static void executarMenu() {
        int opcao;
        do {
            System.out.println("\n=== POKEMON ===");
            System.out.println("1 - Listar todos os Pokemons");
            System.out.println("2 - Procurar Pokemon");
            System.out.println("3 - Favoritar Pokemon");
            System.out.println("4 - Listar favoritos");
            System.out.println("5 - Desfavoritar Pokemon");
            System.out.println("0 - Sair");
            opcao = lerInteiro("Opcao: ");

            switch (opcao) {
                case 1 -> ListarTodosPokemons();
                case 2 -> ListarPokemons();
                case 3 -> FavoritarPokemon();
                case 4 -> ListarPokemonFavorito();
                case 5 -> DesfavoritarPokemon();
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void ListarPokemonFavorito() {
        try {
            pokemonDAO.listar().forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Erro ao listar favoritos: " + e.getMessage());
        }
    }

    private static void DesfavoritarPokemon() {
        int id = lerInteiroPositivo("ID do Pokemon: ");
        try {
            pokemonDAO.excluir(id);
            System.out.println("Pokemon removido dos favoritos.");
        } catch (SQLException e) {
            System.err.println("Erro ao desfavoritar Pokemon: " + e.getMessage());
        }
    }

    private static void FavoritarPokemon() {
        String nome = lerTexto("Nome do Pokemon: ");
        try {
            Pokemon pokemon = pokeAPI.consultar(nome);
            if (pokemon == null) {
                return;
            }
            pokemonDAO.criar(pokemon);
            System.out.println("Pokemon favoritado: " + pokemon);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar a PokeAPI: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao favoritar Pokemon: " + e.getMessage());
        }
    }

    private static void ListarPokemons() {
        String nome = lerTexto("Nome do Pokemon: ");
        try {
            Pokemon pokemon = pokeAPI.consultar(nome);
            if (pokemon != null) {
                System.out.println(pokemon);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar a PokeAPI: " + e.getMessage());
        }
    }

    private static void ListarTodosPokemons() {
        try {
            System.out.println("\n=== POKEMON DISPONIVEIS NA API ===");
            List<String> nomes = pokeAPI.listarTodos();
            for (int i = 0; i < nomes.size(); i++) {
                System.out.printf("%d - %s%n", i + 1, nomes.get(i));
            }
            System.out.println("Total: " + nomes.size() + " Pokemon.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao listar Pokemon da API: " + e.getMessage());
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Este campo e obrigatorio.");
        }
    }
    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private static int lerInteiroPositivo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor > 0) {
                return valor;
            }
            System.out.println("O valor deve ser maior que zero.");
        }
    }



}