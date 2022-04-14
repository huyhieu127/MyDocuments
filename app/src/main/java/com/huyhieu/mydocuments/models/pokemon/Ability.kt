package com.huyhieu.mydocuments.models.pokemon

import com.huyhieu.mydocuments.models.pokemon.PokemonUrlForm

data class Ability(
    val ability: PokemonUrlForm,
    val is_hidden: Boolean,
    val slot: Int
)