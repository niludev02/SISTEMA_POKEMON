CREATE TABLE pokemons (
    id SERIAL PRIMARY KEY,
    id_pokemon INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    altura INTEGER NOT NULL,
    peso INTEGER NOT NULL,
    experiencia_base INTEGER NOT NULL
);