package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.game.GameStateManager
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import java.util.regex.Pattern

const val SOMEBODY_DID = """«"""
const val YOU_DID = """»"""

private val numberPattern = Pattern.compile("\\s[0-9]+\\s")

object DamageAndHealParser : ChatParser {

    override fun onChatReceived(e: ClientChatReceivedEvent.System) {
        if (GameStateManager.notInGame) {
            return
        }
        try {
            val msg: String = e.message.string
            if (!(msg.contains(SOMEBODY_DID) || msg.contains(YOU_DID))) {
                return
            }

            var otherPlayer: String
            val amount = getDamageOrHealthValue(msg)
            val isCrit = msg.contains("!")

            if (msg.contains(SOMEBODY_DID)) {
                otherPlayer = fun(): String {
                    return when {
                        //PLAYER's ability hit you
                        msg.contains("Your") && msg.contains("you") -> Minecraft.getInstance().player!!.name.string
                        msg.contains("'s") -> msg.substring(2, msg.indexOf("'s"))
                        //You took 500 dmg (revenant)
                        msg.contains("You took") -> "EXTERNAL"
                        //PLAYER hit you for
                        msg.contains("hit") -> msg.substring(2, msg.indexOf(" hit"))
                        else -> "UNDEFINED"
                    }
                }()
                when {
                    msg.contains("health") -> {
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.HealingReceivedEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                    }

                    msg.contains("damage") -> {
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.DamageTakenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                        //Player lost Energy from otherPlayer's Avenger's Strike
                        if (msg.contains("Avenger's Strike"))
                            FORGE_BUS.post(
                                WarlordsPlayerEvents.EnergyLostEvent(
                                    6,
                                    otherPlayer
                                )
                            )
                    }

                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(0, msg.indexOf("'s"))
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.EnergyReceivedEvent(
                                amount,
                                otherPlayer,
                                ability = ""
                            )
                        )
                    }
                }

            } else if (msg.contains(YOU_DID)) {
                when {
                    msg.contains("health") && !msg.contains("you") -> {
                        otherPlayer = msg.substring(msg.indexOf("healed ") + 7, msg.indexOf("for") - 1)
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.HealingGivenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                    }

                    msg.contains("health") -> {
                        otherPlayer = Minecraft.getInstance().player!!.name.string
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.HealingGivenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                    }

                    msg.contains("damage") -> {
                        otherPlayer = msg.substring(msg.indexOf("hit ") + 4, msg.indexOf("for") - 1)
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.DamageDoneEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                        //Player's Avenger's Strike stole energy from otherPlayer
                        if (msg.contains("Avengers Strike"))
                            FORGE_BUS.post(
                                WarlordsPlayerEvents.EnergyStolenEvent(
                                    6,
                                    otherPlayer
                                )
                            )
                    }

                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(msg.indexOf("gave") + 5, msg.indexOf("energy") - 4)
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.EnergyGivenEvent(
                                amount,
                                otherPlayer,
                                ability = ""
                            )
                        )
                    }

                    msg.contains("absorbed") -> {
                        otherPlayer = msg.substring(msg.indexOf("by") + 3)
                        FORGE_BUS.post(
                            WarlordsPlayerEvents.DamageAbsorbedEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                ability = ""
                            )
                        )
                    }
                }
            }
        } catch (throwable: Throwable) {
            WarlordsPlusPlus.LOGGER.error(throwable)
        }
    }

    private fun getDamageOrHealthValue(inputMessage: String): Int {
        try {
            var message = ""
            if (inputMessage.contains("for")) {
                message = inputMessage.substring(inputMessage.indexOf("for"))
            } else if (inputMessage.contains("Crusader")) {
                //giving energy
                message = inputMessage.substring(inputMessage.indexOf("energy") - 4)
            }
            val m = numberPattern.matcher(message.replace("!", ""))
            if (!m.find()) {
                return 0
            }
            return m.group().trim().toInt()
        } catch (e: Exception) {
            WarlordsPlusPlus.LOGGER.error("Failed to extract damage from this message: $inputMessage")
            e.printStackTrace()
        }
        return 0
    }
}



