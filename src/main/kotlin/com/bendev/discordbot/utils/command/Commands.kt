package com.bendev.discordbot.utils.command

import com.bendev.discordbot.utils.properties.PropertiesManager
import org.jetbrains.annotations.NotNull
import kotlin.reflect.KClass

object Commands {

    enum class CommandName(val contentType: KClass<out CommandContent>, @NotNull vararg val value: String){
        /**
         * Help command display a list of help
         * */
        COMMAND_DEOP(OperatorContent::class, "deop"),

        COMMAND_HELP(EmptyContent::class,"help"),

        COMMAND_OP(OperatorContent::class, "op"),

        COMMAND_PERMISSION(PermissionContent::class, "permission");

        fun getFullCommand(): String {
            return "${PropertiesManager.properties.prefix}${this.value}"
        }

        companion object {
            fun getPrefixedCommandList() : List<String> = values().map {
                it.value.map { string ->
                    "${PropertiesManager.properties.prefix}$string"
                }
            }.flatten()

        }

    }

}