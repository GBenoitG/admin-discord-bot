package utils.properties

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
        var token: String = "",
        var prefix: String = "$",

        /**
         *
         * */
        @SerialName("allowed_roles_list")
        var allowedRolesName: MutableList<String> = mutableListOf(),

        /**
         *
         * */
        @SerialName("allowed_users_id_list")
        var allowedUsersId: MutableList<String> = mutableListOf()
)