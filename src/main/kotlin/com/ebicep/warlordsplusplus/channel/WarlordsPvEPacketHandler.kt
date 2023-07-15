package com.ebicep.warlordsplusplus.channel

import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import org.apache.logging.log4j.Level


object WarlordsPvEPacketHandler {

    private const val PROTOCOL_VERSION = "1"
    private val INSTANCE: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation("warlords", "warlords"),
        { PROTOCOL_VERSION },
        { PROTOCOL_VERSION == it },
        { PROTOCOL_VERSION == it }
    )
    private var id: Int = 0
        get() = field++

    init {
        WarlordsPlusPlus.LOGGER.log(Level.INFO, "Starting packet handler...")
        INSTANCE.messageBuilder(CooldownPacket::class.java, id)
            .encoder(CooldownPacket::encoder)
            .decoder(CooldownPacket::decoder)
            .consumerMainThread(CooldownPacket::messageConsumer)
            .add()
    }

}