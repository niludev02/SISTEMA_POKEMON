package br.edu.fatecpg.pokemon.model;

import java.util.List;

public class Pokemon {
    private int id;
    private String name;
    private List<TypeSlot> types;
    private double height;
    private double weight;
    private double base_experience;

    public Pokemon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeSlot> getTypes() {
        return types;
    }

    public void setTypes(List<TypeSlot> types) {
        this.types = types;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(double base_experience) {
        this.base_experience = base_experience;
    }

    @Override
    public String toString() {
        return "Pokemon: " +
                "id: " + id +
                ", name: " + name +
                ", types: " + types +
                ", height: " + height +
                ", weight: " + weight +
                ", base_experience: " + base_experience;
    }
}
