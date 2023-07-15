package com.ebicep.warlordsplusplus.util

import net.minecraft.client.Minecraft
import net.minecraft.world.scores.PlayerTeam

object ScoreboardUtils {

    fun getPlayerTeamFromName(name: String): PlayerTeam? {
        return Minecraft.getInstance().player?.scoreboard?.playerTeams?.firstOrNull { it.name.equals(name) }
    }

    fun getUnformattedText(playerTeam: PlayerTeam): String {
        return playerTeam.playerPrefix.string + playerTeam.playerSuffix.string
    }

    /**
     * @param at represents sidebar number starting from bottom = 1 (basically read off sidebar)
     */
    fun containsAt(teams: List<PlayerTeam>, text: String, at: Int): Boolean {
        val relativeLine = at - 1
        if (relativeLine < 0) {
            return false
        }
        if (relativeLine >= teams.size) {
            return false
        }
        return getUnformattedText(teams[relativeLine]).contains(text)
    }

    fun containsAtAnywhere(teams: List<PlayerTeam>, text: String): Boolean {
        return teams.any { getUnformattedText(it).contains(text) }
    }

}