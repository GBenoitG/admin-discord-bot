package com.bendev.discordbot.utils.properties

import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import java.io.File

object PropertiesManager {

    private lateinit var filePath: String
    lateinit var properties: Properties
        private set

    private val json: Json by lazy { Json {
        prettyPrint = true
        encodeDefaults = true
    } }

    @Synchronized
    fun loadPropertiesFromFile(propertiesFilePath: String) {

        val file = initFile(propertiesFilePath)
        if (file.readBytes().toString(Charsets.UTF_8).isEmpty()) initPropertiesFile(file)

        file.inputStream().use {
            val data = it.readBytes().toString(Charsets.UTF_8)
            properties = json.decodeFromString(Properties.serializer(), data)
        }
        filePath = propertiesFilePath

    }

    fun updateProperties(newProperties: Properties) {

        val file = File(filePath)

        if (!file.exists()) file.delete()

        file.createNewFile()

        val dataToWrite = json.encodeToString(Properties.serializer(), newProperties)

        file.writer(Charsets.UTF_8).use {
            it.write(dataToWrite)
        }

    }

    private fun createPropertiesFile(file: File) {
        file.createNewFile()
        initPropertiesFile(file)
    }

    private fun initPropertiesFile(file: File) {
        val dataToWrite = json.encodeToString(Properties.serializer(), Properties())
        file.outputStream().use {
            it.write(dataToWrite.toByteArray(Charsets.UTF_8))
        }
    }

    fun isUserAllowed(guild: Guild, user: User): Boolean {

        val adminRoles = guild.roles.filter { properties.allowedRolesName.contains(it.name) }
        val adminUsers = guild.getMembersWithRoles(adminRoles).map { it.user.id }.toMutableList()

        adminUsers.addAll(properties.allowedUsersId)

        return adminUsers.any { it == user.id }

    }

    fun initFile(propertiesFilePath: String): File {

        if (propertiesFilePath.isEmpty()) throw Exception("You must provide a correct properties file path")

        val file = File(propertiesFilePath)

        if (!file.exists()) createPropertiesFile(file)

        return file
    }

}