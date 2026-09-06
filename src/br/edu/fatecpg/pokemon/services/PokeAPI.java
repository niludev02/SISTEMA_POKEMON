package br.edu.fatecpg.pokemon.services;

import br.edu.fatecpg.pokemon.model.Pokemon;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokeAPI {

    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon";
    private final HttpClient client = HttpClient.newHttpClient();

    public Pokemon consultar(String nome) throws IOException, InterruptedException {
        String nome_format = nome.toLowerCase();

        String url = URL_BASE + "/" + nome_format;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro ao consultar API. Status: " + response.statusCode());
            return null;
        }

        String json = response.body();

        return parseJson(json, nome_format);
    }

    public List<String> listarTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "?limit=2000"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro ao listar Pokemon. Status: " + response.statusCode());
        }

        List<String> nomes = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response.body());

        while (matcher.find()) {
            nomes.add(matcher.group(1));
        }

        return nomes;
    }


    private Pokemon parseJson(String json, String nome_format) {
        int id = Integer.parseInt(extrairCampo(json, "id"));
        String nome = extrairCampo(json, "name");
        String tipo = extrairTipo(json);
        double altura = Double.parseDouble(extrairCampo(json, "height"));
        double peso = Double.parseDouble(extrairCampo(json, "weight"));
        double experiencia_base = Double.parseDouble(extrairCampo(json, "base_experience"));

        return new Pokemon(
                id,
                nome,
                tipo,
                altura,
                peso,
                experiencia_base
        );
    }
    private String extrairCampo(String json, String campo) {

        Pattern pattern = Pattern.compile(
                "\"" + campo + "\"\\s*:\\s*(?:\"([^\"]*)\"|(\\d+))"
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1) != null
                    ? matcher.group(1)
                    : matcher.group(2);
        }

        return "";
    }

    private String extrairTipo(String json) {
        Pattern pattern = Pattern.compile(
                "\"types\"\\s*:\\s*\\[.*?\"type\"\\s*:\\s*\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "desconhecido";
    }
}