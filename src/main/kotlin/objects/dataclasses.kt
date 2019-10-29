package objects

data class RoleAndStatus(
    val id : Int,
    val user_state : String
)

data class LocalUser(
    val id: Int,
    val name : String,
    val biometricID : String,
    val user_state: String,
    val roleID : Int
)

data class UserList(
    val list : ArrayList<LocalUser>
){
    constructor() : this(arrayListOf())
}