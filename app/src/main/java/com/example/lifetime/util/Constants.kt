package com.example.lifetime.util

const val DB_NAME = "life_spiral_database"

enum class LoggedInMode constructor(val type: Int) {
    LOGGED_IN_MODE_LOGGED_OUT(0),
    LOGGED_IN_MODE_LOGGED_IN(1),
}