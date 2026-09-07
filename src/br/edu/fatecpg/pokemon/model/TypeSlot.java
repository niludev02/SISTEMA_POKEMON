package br.edu.fatecpg.pokemon.model;

public class TypeSlot {
    private int slot;
    private TypeInfo type;

    public TypeSlot() {

    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public TypeInfo getType() {
        return type;
    }

    public void setType(TypeInfo type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}