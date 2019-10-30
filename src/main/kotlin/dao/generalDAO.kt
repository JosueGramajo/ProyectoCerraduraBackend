package dao

import com.fasterxml.jackson.databind.ObjectMapper
import database.DBConnection
import objects.*
import java.text.SimpleDateFormat

object GeneralDao{
    fun insertUser(name : String, biometricId : String){
        DBConnection.executeUpdate("INSERT INTO local_user(name, biometricID, user_state, roleID) VALUES('$name','$biometricId','pending', 2)")
    }

    fun getRoleAndState(id : String) : String{
        val query = "select r.id, u.user_state, u.name from local_user u inner join user_role r on (u.roleID = r.id) where u.biometricID = '$id' "

        val rs = DBConnection.executeQuery(query)
        var result = ""
        while (rs!!.next()){
            val obj = RoleAndStatus(rs.getInt("id"), rs.getString("user_state"), rs.getString("name"))
            val mapper = ObjectMapper()
            result = mapper.writeValueAsString(obj)
        }

        return result
    }

    fun getPendingUsers() : String{
        val query = "select * from local_user where user_state = 'pending'"

        val rs = DBConnection.executeQuery(query)

        val listObj = UserList()

        val result = ""

        while (rs!!.next()){
            val usr = LocalUser(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("biometricID"),
                rs.getString("user_state"),
                rs.getInt("roleID")
            )

            listObj.list.add(usr)
        }

        val mapper = ObjectMapper()

        return mapper.writeValueAsString(listObj)
    }

    fun changeUserState(biometricID : String, state : String){
        DBConnection.executeUpdate("UPDATE local_user SET user_state = '$state' WHERE biometricID = '$biometricID'")
    }

    fun insertLog(name : String, date : String, time : String, event : String){

        val query = "INSERT INTO system_log VALUES ('$name','$time','$date','$event')"

        println(query)

        DBConnection.executeUpdate(query)
    }

    fun getLogs() : String{
        val query = "SELECT * FROM system_log"

        val rs = DBConnection.executeQuery(query)

        val format = SimpleDateFormat("yyyy-MM-dd")

        val list = arrayListOf<LogData>()

        while (rs!!.next()){
            val log = LogData(
                    rs.getString("name"),
                    format.format(rs.getDate("log_time")),
                    rs.getTime("log_date").toString(),
                    rs.getString("event_status")
            )

            list.add(log)
        }

        val listObj = LogList(list)

        val mapper = ObjectMapper()

        return mapper.writeValueAsString(listObj)
    }

    fun parseJson(json : String) : String{
        var result = json.replace("**","{")
        result = result.replace("*","}")
        result = result.replace("_",",")
        return result
    }
}