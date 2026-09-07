package br.edu.fatecpg.pokemon.view;
import br.edu.fatecpg.pokemon.data.Conexao;
import br.edu.fatecpg.pokemon.model.PokemonDAO;
import br.edu.fatecpg.pokemon.model.Pokemon;
import br.edu.fatecpg.pokemon.services.PokeAPI;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PokemonDAO pokemonDAO = new PokemonDAO();
    private static final PokeAPI pokeAPI = new PokeAPI();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
                criarTabelas();
                System.out.println("Banco conectado e tabelas prontas.");
                exibirDatas();
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
            System.out.println("6 - Ler Pokemon salvo em JSON");
            System.out.println("0 - Sair");
            opcao = lerInteiro("Opcao: ");

            switch (opcao) {
                case 1 -> ListarTodosPokemons();
                case 2 -> ListarPokemons();
                case 3 -> FavoritarPokemon();
                case 4 -> ListarPokemonFavorito();
                case 5 -> DesfavoritarPokemon();
                case 6 -> lerPokemonJson();
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void ListarPokemonFavorito() {
        try {
            List<Pokemon> favoritos = pokemonDAO.listar();
            favoritos.forEach(System.out::println);
            registrarLog("Favoritos listados: " + favoritos.size() + " Pokemon");
        } catch (SQLException e) {
            System.err.println("Erro ao listar favoritos: " + e.getMessage());
        }
    }

    private static void DesfavoritarPokemon() {
        int id = lerInteiroPositivo("ID do Pokemon: ");
        try {
            pokemonDAO.excluir(id);
            registrarLog("Pokemon desfavoritado: ID " + id);
            System.out.println("Pokemon removido dos favoritos.");
        } catch (SQLException e) {
            System.err.println("Erro ao desfavoritar Pokemon: " + e.getMessage());
        }
    }

    private static void FavoritarPokemon() {
        String name = lerTexto("Nome do Pokemon: ");
        try {
            Pokemon pokemon = pokeAPI.consultar(name);
            if (pokemon == null) {
                return;
            }
            pokemonDAO.criar(pokemon);
            salvarPokemonJson(pokemon);
            registrarLog("Pokemon favoritado: " + pokemon.getName() + " (ID " + pokemon.getId() + ")");
            System.out.println("Pokemon favoritado: " + pokemon);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar a PokeAPI: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao favoritar Pokemon: " + e.getMessage());
        }
    }

    private static void ListarPokemons() {
        String name = lerTexto("Nome do Pokemon: ");
        try {
            Pokemon pokemon = pokeAPI.consultar(name);
            if (pokemon != null) {
                System.out.println(pokemon);
                registrarLog("Pokemon consultado: " + pokemon.getName() + " (ID " + pokemon.getId() + ")");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar a PokeAPI: " + e.getMessage());
        }
    }

    private static void ListarTodosPokemons() {
        try {
            System.out.println("\n=== POKEMON DISPONIVEIS NA API ===");
            List<String> names = pokeAPI.listarTodos();
            for (int i = 0; i < names.size(); i++) {
                System.out.printf("%d - %s%n", i + 1, names.get(i));
            }
            System.out.println("Total: " + names.size() + " Pokemon.");
            registrarLog("Todos os Pokemon listados: " + names.size() + " Pokemon");
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao listar Pokemon da API: " + e.getMessage());
        }
    }

    private static void exibirDatas() {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime nowInBrasilia = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

        System.out.println("Data e hora local do sistema: " + now);
        System.out.println("Data e hora em Brasilia: " + nowInBrasilia);
    }

    private static void salvarPokemonJson(Pokemon pokemon) throws IOException {
        objectMapper.writeValue(new File("pokemon.json"), pokemon);
        System.out.println("Pokemon salvo no arquivo pokemon.json.");
    }

    private static void lerPokemonJson() {
        try {
            Pokemon pokemon = objectMapper.readValue(new File("pokemon.json"), Pokemon.class);
            System.out.println("Pokemon lido do arquivo: " + pokemon);
        } catch (IOException e) {
            System.err.println("Erro ao ler pokemon.json: " + e.getMessage());
        }
    }

    private static void registrarLog(String mensagem) {
        try (FileWriter escritor = new FileWriter("log.txt", true)) {
            escritor.write(LocalDateTime.now() + " - " + mensagem + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
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