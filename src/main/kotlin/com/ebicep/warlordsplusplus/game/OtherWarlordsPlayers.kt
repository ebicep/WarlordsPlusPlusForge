package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.util.Specialization
import com.ebicep.warlordsplusplus.util.Team
import com.ebicep.warlordsplusplus.util.WarlordClass
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.client.player.RemotePlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.scores.PlayerTeam
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.*
import java.util.regex.Pattern


private val numberPattern = Pattern.compile("[0-9]{2}")

open class OtherWarlordsPlayer(val name: String, val uuid: UUID) {

    var kills: Int = 0
    var deaths: Int = 0
    var damageDone: Int = 0
    var damageReceived: Int = 0
    var healingDone: Int = 0
    var healingReceived: Int = 0
    var warlordClass = WarlordClass.NONE
    var spec = Specialization.NONE
    var team = Team.NONE
    var level = 0
    var prestiged: Boolean = false
    var left: Boolean = false

    //TODO
    var died: Int = 0
    var stoleKill: Int = 0
    var picks: Int = 0
    var returns: Int = 0
    var caps: Int = 0
    var isDead: Boolean = false
    var respawn: Int = -1

    var currentEnergy: Int = 0
    var maxEnergy: Int = 0
    var redCooldown: Int = 0
    var purpleCooldown: Int = 0
    var blueCooldown: Int = 0
    var orangeCooldown: Int = 0

    // [MAG][80][+]
    fun getInfoPrefix(): MutableComponent {
        return Component.literal("")
            .withStyle {
                it.withColor(ChatFormatting.DARK_GRAY)
            }
            .append(Component.literal("["))
            .append(Component.literal(warlordClass.shortName)
                .withStyle {
                    it.withColor(ChatFormatting.WHITE)
                })
            .append(Component.literal("]["))
            .append(Component.literal(level.toString().padStart(2, '0'))
                .withStyle {
                    it.withColor(if (prestiged) ChatFormatting.GOLD else ChatFormatting.WHITE)
                })
            .append(Component.literal("]["))
            .append(spec.iconComponent)
            .append(Component.literal("]"))
    }
}

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object OtherWarlordsPlayers {

    val playersMap: MutableMap<String, OtherWarlordsPlayer> = mutableMapOf()
    //val tempPlayersMap: MutableMap<String, WarlordsPlayer> = mutableMapOf()

    fun getOtherWarlordsPlayers(): Collection<OtherWarlordsPlayer> {
        return getOtherWarlordsPlayers(Minecraft.getInstance().player!!.connection.onlinePlayers)
    }

    fun getOtherWarlordsPlayers(playersInfo: MutableCollection<PlayerInfo>): Collection<OtherWarlordsPlayer> {
        playersInfo.filter {
            !playersMap.contains(it.profile.name)
        }.filter { playerInfo ->
            val playerTeam: PlayerTeam? = playerInfo.team
            if (playerTeam == null) {
                false
            } else {
                WarlordClass.values().any {
                    playerTeam.playerPrefix.string.contains(it.shortName)
                }
            }
        }.map { playerInfo ->
            val playerTeam: PlayerTeam = playerInfo.team!!
            val otherWarlordsPlayer = OtherWarlordsPlayer(playerInfo.profile.name, playerInfo.profile.id)
            otherWarlordsPlayer.warlordClass = WarlordClass.values().first {
                playerTeam.playerPrefix.string.contains(it.shortName)
            }
            playerInfo.tabListDisplayName?.string?.let {
                val m = numberPattern.matcher(it)
                otherWarlordsPlayer.level =
                    if (!m.find()) {
                        0
                    } else {
                        m.group().toInt()
                    }
            }

            otherWarlordsPlayer.team = Team.values().first {
                playerTeam.color == it.color
            }

            return@map otherWarlordsPlayer
        }.forEach {
            playersMap[it.name] = it
        }

        val players = Minecraft.getInstance().level!!.players()
        playersMap.filter {
            it.value.spec == Specialization.NONE || GameStateManager.inWarlords2
        }.forEach { player ->
            players.filter {
                it is RemotePlayer && it.gameProfile.name == player.key
            }.map { p ->
                val selectedItem = p.inventory.getSelected()
                if (selectedItem == ItemStack.EMPTY || selectedItem.count != 1) {
                    return@map
                }
                val tag: CompoundTag = selectedItem.tag ?: return@map
                val display: CompoundTag = tag.getCompound("display") ?: return@map
                val displayString = display.toString()
                when {
                    displayString.contains("RIGHT-CLICK") -> {
                        val lore: ListTag = display.getList("Lore", Tag.TAG_STRING.toInt()) ?: return@map
                        val skillBoostStart = lore.getString(4)
                        if (skillBoostStart.contains("green")) {
                            player.value.spec = Specialization.values().firstOrNull {
                                skillBoostStart.contains(it.classname)
                            } ?: Specialization.NONE
                        } else {
                            val afterRightClick = displayString.substring(displayString.indexOf("RIGHT-CLICK"))
                            player.value.spec = Specialization.values().firstOrNull {
                                afterRightClick.contains(it.weapon)
                            } ?: Specialization.NONE
                        }
                    }

                    displayString.contains("LEFT-CLICK") -> {
                        val name = display.getString("Name") ?: return@map
                        player.value.spec = Specialization.values().firstOrNull {
                            name.contains(it.weapon)
                        } ?: Specialization.NONE
                    }

                    displayString.contains("Cooldown") && !display.contains("Mount") -> {
                        val name = display.getString("Name") ?: return@map
                        val ability: ((Specialization) -> String) = when (selectedItem.item) {
                            Items.RED_DYE -> { it: Specialization -> it.red }
                            Items.GLOWSTONE_DUST -> { it: Specialization -> it.purple }
                            Items.LIME_DYE -> { it: Specialization -> it.blue }
                            Items.ORANGE_DYE -> { it: Specialization -> it.orange }
                            else -> return@map
                        }
                        player.value.spec = Specialization.values().firstOrNull {
                            name.contains(ability(it))
                        } ?: Specialization.NONE
                    }
                }

            }
        }

        //TODO reupdate player

        return playersMap.values
    }

    fun getPlayerByName(name: String): OtherWarlordsPlayer? {
        return playersMap.values.firstOrNull { it.name == name }
    }

    @SubscribeEvent
    fun onReset(event: WarlordsGameEvents.ResetEvent) {
        playersMap.clear()
        getOtherWarlordsPlayers()
    }

    @SubscribeEvent
    fun onKill(event: WarlordsPlayerEvents.KillEvent) {
        if (event.deathPlayer in playersMap) {
            val otherWarlordsPlayer = playersMap[event.deathPlayer]!!
            otherWarlordsPlayer.deaths++
            otherWarlordsPlayer.isDead = true
            otherWarlordsPlayer.respawn = event.respawn
        }
        if (event.player in playersMap) {
            playersMap[event.player]!!.kills++
        }
    }

    @SubscribeEvent
    fun onAbstractDamageHealEnergyEvent(event: WarlordsPlayerEvents.AbstractDamageHealEnergyEvent) {
        val player = event.player
        val otherWarlordsPlayer = if (player in playersMap) playersMap[player]!! else return
        when (event::class) {
            WarlordsPlayerEvents.DamageDoneEvent::class -> otherWarlordsPlayer.damageDone += event.amount

            WarlordsPlayerEvents.DamageTakenEvent::class -> otherWarlordsPlayer.damageReceived += event.amount

            WarlordsPlayerEvents.HealingGivenEvent::class -> otherWarlordsPlayer.healingDone += event.amount

            WarlordsPlayerEvents.HealingReceivedEvent::class -> otherWarlordsPlayer.healingReceived += event.amount
        }
    }
}