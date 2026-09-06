package br.edu.fatecpg.pokemon.model;

public class Pokemon {
    private int id_pokemon;
    private String nome;
    private String tipo;
    private double altura;
    private double peso;
    private double experiencia_base;

    public Pokemon(int id_pokemon, String nome, String tipo, double altura, double peso, double experiencia_base) {
        this.id_pokemon = id_pokemon;
        this.nome = nome;
        this.tipo = tipo;
        this.altura = altura;
        this.peso = peso;
        this.experiencia_base = experiencia_base;
    }

    public int getId_pokemon() {
        return id_pokemon;
    }

    public void setId_pokemon(int id_pokemon) {
        this.id_pokemon = id_pokemon;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public double getExperiencia_base() {
        return experiencia_base;
    }

    @Override
    public String toString() {
        return "Pokemon: " +
                "id_pokemon: " + id_pokemon +
                ", nome: " + nome +
                ", tipo: " + tipo +
                ", altura: " + altura +
                ", peso: " + peso +
                ", experiencia_base: " + experiencia_base;
    }
}
