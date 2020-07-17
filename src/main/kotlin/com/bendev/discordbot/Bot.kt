package com.bendev.discordbot
import com.bendev.discordbot.listener.MessageListener
import com.bendev.discordbot.listener.PrivateMessageListener
import com.bendev.discordbot.listener.ReactionListener
import com.bendev.discordbot.utils.Constant
import com.bendev.discordbot.utils.properties.PropertiesManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.IOException

import javax.security.auth.login.LoginException


class Bot {

    companion object {

        @Throws(LoginException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            Bot().init()

        }

    }

    @Throws(LoginException::class, IOException::class)
    fun init() {

        PropertiesManager.loadPropertiesFromFile("properties.json")

        JDABuilder
                .create(
                        PropertiesManager.properties.token,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS
                ).addEventListeners(
                        MessageListener(),
                        ReactionListener(),
                        PrivateMessageListener()
                ).build()

    }

}