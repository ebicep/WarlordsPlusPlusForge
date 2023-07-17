package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.game.GameModes
import com.ebicep.warlordsplusplus.game.GameStateManager
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object RespawnTimerDetector {

    var respawnTimer = 0

    @SubscribeEvent
    fun onReset(event: WarlordsGameEvents.ResetEvent) {
        respawnTimer = 18
    }

    @SubscribeEvent
    fun onSecond(event: WarlordsGameEvents.SecondEvent) {
        respawnTimer--
        if (GameStateManager.currentGameMode == GameModes.CTF) {
            val second = GameStateManager.second
            if (respawnTimer < 0) {
                respawnTimer = 0
            }
            if (second % 12 == 0) {
                respawnTimer = 12
            }
        } else if (GameStateManager.currentGameMode == GameModes.DOM) {
            if (respawnTimer < 0) {
                respawnTimer = 17
            }
        }
        OtherWarlordsPlayers.playersMap.forEach { (_, value) ->
            if (!value.isDead) {
                return@forEach
            }
            if (value.respawn == -1) {
                return@forEach
            }
            value.respawn--
            if (value.respawn == 0) {
                value.isDead = false
                value.respawn = -1
            }
        }
    }

}