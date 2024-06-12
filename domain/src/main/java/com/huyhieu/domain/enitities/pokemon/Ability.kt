package com.huyhieu.domain.enitities.pokemon

data class Ability(
    val ability: PokemonUrlForm,
    val is_hidden: Boolean,
    val slot: Int
)