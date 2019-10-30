package objects

data class RoleAndStatus(
    val id : Int,
    val user_state : String,
    val name : String
)

data class LocalUser(
    val id: Int,
    val name : String,
    val biometricID : String,
    val user_state: String,
    val roleID : Int
){
    constructor() : this(0, "", "", "" , 0)
}

data class UserList(
    val list : ArrayList<LocalUser>
){
    constructor() : this(arrayListOf())
}

data class RegisterRequest(
        val name : String,
        val biometricID : String
){
    constructor() : this("","")
}

data class ChangeState(
        val biometricID : String,
        val user_state : String
){
    constructor() : this("", "")
}

data class LogData(
        val name : String,
        val time : String,
        val date : String,
        val status : String
){
    constructor() : this("","","","")
}

data class LogList(
        val list : ArrayList<LogData>
){
    constructor() : this(arrayListOf())
}