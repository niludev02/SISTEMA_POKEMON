package br.edu.fatecpg.pokemon.services;

import br.edu.fatecpg.pokemon.model.Pokemon;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PokeAPI {

    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Pokemon consultar(String name) throws IOException, InterruptedException {
        String nameFormat = name.toLowerCase();

        String url = URL_BASE + "/" + nameFormat;

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

        return objectMapper.readValue(json, Pokemon.class);
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

        JsonNode root = objectMapper.readTree(response.body());
        List<String> names = new ArrayList<>();
        for (JsonNode result : root.path("results")) {
            names.add(result.path("name").asText());
        }

        return names;
    }
}