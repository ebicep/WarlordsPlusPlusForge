package com.ebicep.warlordsplusplus.util

import net.minecraft.ChatFormatting

enum class Team(val teamName: String, val color: ChatFormatting?) {
    BLUE("Blue", ChatFormatting.BLUE),
    RED("Red", ChatFormatting.RED),
    NONE("NONE", null)
}

enum class WarlordClass(val classname: String, val shortName: String) {
    WARRIOR("Warrior", "WAR"),
    SHAMAN("Shaman", "SHA"),
    MAGE("Mage", "MAG"),
    PALADIN("Paladin", "PAL"),
    ROGUE("Rogue", "ROG"),
    NONE("NONE", "NONE")
}

enum class Specialization(
    val classname: String,
    val type: SpecType
) {
    AVENGER(
        "Avenger",
        SpecType.DAMAGE

    ),
    CRUSADER(
        "Crusader",
        SpecType.TANK
    ),
    PROTECTOR(
        "Protector",
        SpecType.HEALER
    ),
    BERSERKER(
        "Berserker",
        SpecType.DAMAGE
    ),
    DEFENDER(
        "Defender",
        SpecType.TANK
    ),
    REVENANT(
        "Revenant",
        SpecType.HEALER
    ),
    PYROMANCER(
        "Pyromancer",
        SpecType.DAMAGE
    ),
    CRYOMANCER(
        "Cryomancer",
        SpecType.TANK
    ),
    AQUAMANCER(
        "Aquamancer",
        SpecType.HEALER
    ),
    THUNDERLORD(
        "Thunderlord",
        SpecType.DAMAGE
    ),
    SPIRITGUARD(
        "Spiritguard",
        SpecType.TANK
    ),
    EARTHWARDEN(
        "Earthwarden",
        SpecType.HEALER
    ),
    ASSASSIN(
        "Assassin",
        SpecType.DAMAGE
    ),
    VINDICATOR(
        "Vindicator",
        SpecType.TANK
    ),
    APOTHECARY(
        "Apothecary",
        SpecType.HEALER
    ),

    NONE(
        "NONE",
        SpecType.NONE
    )

    ;

    val icon
        get() = type.coloredSymbol
}

enum class SpecType(val specName: String, val symbol: String, val color: ChatFormatting) {
    DAMAGE("damage", "銌", ChatFormatting.RED),
    TANK("defense", "鉰", ChatFormatting.YELLOW),
    HEALER("healer", "銀", ChatFormatting.GREEN),
    NONE("NONE", "", ChatFormatting.OBFUSCATED);

    val coloredSymbol = "$color$symbol" //TODO
}
