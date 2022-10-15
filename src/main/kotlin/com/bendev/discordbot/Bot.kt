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

    @Throws(LoginException::class, IOException::class)
    fun init() {

        PropertiesManager.loadPropertiesFromFile(Constant.PROPERTIES_FILE_PATH)

        JDABuilder
                .createDefault(
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

@Throws(LoginException::class, IOException::class)
fun main(args: Array<String>) {

    if (args.isNotEmpty()) {
        when (args[0]) {
            "init" -> PropertiesManager.initFile(Constant.PROPERTIES_FILE_PATH)
        }
        return
    }

    Bot().init()

}