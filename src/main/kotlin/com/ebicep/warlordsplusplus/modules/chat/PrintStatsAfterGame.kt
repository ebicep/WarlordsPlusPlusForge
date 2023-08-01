package com.ebicep.warlordsplusplus.modules.chat

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.config.ConfigChatGui
import com.ebicep.warlordsplusplus.detectors.GameEndDetector
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayer
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import com.ebicep.warlordsplusplus.game.WarlordsPlayer
import com.ebicep.warlordsplusplus.util.Team
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object PrintStatsAfterGame {

    private val printAbilityStatsAfterGame: Boolean
        get() = ConfigChatGui.printAbilityStatsAfterGame.get()
    private val printGeneralStatsAfterGame: Boolean
        get() = ConfigChatGui.printGeneralStatsAfterGame.get()
    private val printScoreboardStatsAfterGame: Boolean
        get() = ConfigChatGui.printScoreboardStatsAfterGame.get()

    val WHITE_SPACER = Component.literal(" - ").withStyle { it.withColor(ChatFormatting.WHITE) }
    val abilityStats: MutableMap<String, AbilityStat> = mutableMapOf<String, AbilityStat>()
    var divider = ""

    @SubscribeEvent
    fun onAbilityUse(event: WarlordsPlayerEvents.AbstractDamageHealEnergyEvent) {
        if (event is WarlordsPlayerEvents.DamageDoneEvent ||
            event is WarlordsPlayerEvents.HealingGivenEvent
        ) {
            val abilityStat: AbilityStat = abilityStats.computeIfAbsent(event.ability) { AbilityStat() }
            abilityStat.timesProcd++
            if (event.isCrit) {
                abilityStat.crits++
            }
        }
    }

    @SubscribeEvent
    fun onGameEnd(event: WarlordsGameEvents.GameEndEvent) {
        val shouldPrintAnything =
            printAbilityStatsAfterGame ||
                    printGeneralStatsAfterGame ||
                    printScoreboardStatsAfterGame
        if (!shouldPrintAnything) {
            return
        }
        if (printAbilityStatsAfterGame) {
            printEmpty()
            printAbilityStats()
        }
        if (printGeneralStatsAfterGame) {
            printEmpty()
            getGeneralStats()
        }
        if (printScoreboardStatsAfterGame) {
            printEmpty()
            printScoreboardStats()
        }
    }

    private fun printEmpty() {
        Minecraft.getInstance().player!!.sendSystemMessage(Component.empty())
    }

    private fun printAbilityStats() {

    }

    private fun getGeneralStats() {

    }

    private fun printScoreboardStats() {
        val players = OtherWarlordsPlayers.playersMap.values
        val teamBlue = players.filter { it.team == Team.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == Team.RED }.sortedByDescending { it.level }

        for (player in teamBlue) {
            Minecraft.getInstance().player!!.sendSystemMessage(getPlayerScoreboardState(player, WarlordsPlayer.team))
        }
        Minecraft.getInstance().player!!.sendSystemMessage(GameEndDetector.dividerComponent)
        for (player in teamRed) {
            Minecraft.getInstance().player!!.sendSystemMessage(getPlayerScoreboardState(player, WarlordsPlayer.team))
        }
    }

    private fun getPlayerScoreboardState(player: OtherWarlordsPlayer, playerTeam: Team): MutableComponent {
        val onSameTeam = playerTeam == player.team
        val otherTeam = if (player.team == Team.BLUE) Team.RED else Team.BLUE
        val teamColor = player.team.color
        val component = Component.empty()
            .append(player.getInfoPrefix())
            .append(Component.literal(" "))
            .append(Component.literal(player.name)
                .withStyle { it.withColor(teamColor) }
            )
            .append(WHITE_SPACER)
            .append(Component.literal(player.kills.toString())
                .withStyle { it.withColor(ChatFormatting.GREEN) }
            )
            .append(
                Component.literal(":")
                    .withStyle { it.withColor(ChatFormatting.WHITE) }
            )
            .append(Component.literal(player.deaths.toString())
                .withStyle { it.withColor(ChatFormatting.RED) }
            )
            .append(WHITE_SPACER)
            .append(
                if (onSameTeam) {
                    Component.empty()
                        .append(Component.literal(player.healingReceived.toString())
                            .withStyle { it.withColor(ChatFormatting.GREEN) })
                        .append(Component.literal(":")
                            .withStyle { it.withColor(ChatFormatting.WHITE) }
                        )
                        .append(Component.literal(player.healingDone.toString())
                            .withStyle { it.withColor(ChatFormatting.DARK_GREEN) })
                } else {
                    Component.empty()
                        .append(Component.literal(player.damageReceived.toString())
                            .withStyle { it.withColor(ChatFormatting.RED) })
                        .append(Component.literal(":")
                            .withStyle { it.withColor(ChatFormatting.WHITE) }
                        )
                        .append(Component.literal(player.damageDone.toString())
                            .withStyle { it.withColor(ChatFormatting.DARK_RED) })
                }
            )
        if (player.picks > 0) {
            component
                .append(WHITE_SPACER)
                .append(Component.literal("Picks: ")
                    .withStyle { it.withColor(ChatFormatting.GOLD) }
                    .append(Component.literal(player.picks.toString())
                        .withStyle { it.withColor(ChatFormatting.GREEN) }
                    )
                )
        }
        if (player.caps > 0) {
            component
                .append(WHITE_SPACER)
                .append(Component.literal("Caps: ")
                    .withStyle { it.withColor(ChatFormatting.GOLD) }
                    .append(Component.literal(player.caps.toString())
                        .withStyle { it.withColor(otherTeam.color) }
                    )
                )
        }
        if (player.returns > 0) {
            component
                .append(WHITE_SPACER)
                .append(Component.literal("Returns: ")
                    .withStyle { it.withColor(ChatFormatting.GOLD) }
                    .append(Component.literal(player.returns.toString())
                        .withStyle { it.withColor(teamColor) }
                    )
                )
        }
        return component
    }

    data class AbilityStat(
        var timesProcd: Int = 0,
        var crits: Int = 0
    ) {
        fun getCritChance(): Double {
            if (crits == 0) {
                return 0.0
            }
            return crits / timesProcd.toDouble()
        }
    }
}