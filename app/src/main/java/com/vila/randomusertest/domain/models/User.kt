package com.vila.randomusertest.domain.models

data class User(
    val name: Name = Name(),
    val email: String = "",
    val coordinates: Coordinates = Coordinates(),
    val age: Int = 0,
    val picture: Picture = Picture(),
    val userName : String = ""
)
