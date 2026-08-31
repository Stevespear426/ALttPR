package com.stingers.alttpr.model.api

enum class PrizePack(val id: String, val slots: Int) {
    Pack0("0", 8),
    Pack1("1", 8),
    Pack2("2", 8),
    Pack3("3", 8),
    Pack4("4", 8),
    Pack5("5", 8),
    Pack6("6", 8),
    Pull("pull", 3),
    Crab("crab", 2),
    Stun("stun", 1),
    Fish("fish", 1),
}
