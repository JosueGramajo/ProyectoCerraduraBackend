package dao

import com.fasterxml.jackson.databind.ObjectMapper
import database.DBConnection
import objects.LocalUser
import objects.RoleAndStatus
import objects.UserList
import java.text.SimpleDateFormat

object GeneralDao{
    fun insertUser(name : String, biometricId : String){
        DBConnection.executeUpdate("INSERT INTO local_user(name, biometricID, user_state, roleID) VALUES('$name','$biometricId','unauthorized', 2)")
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

    fun getUsersWithPendingState() : String{
        val query = "select * from local_user where user_state = 'pending'"

        val rs = DBConnection.executeQuery(query)

        val listObj = UserList()

        val result = ""

        while (rs!!.next()){
            val usr = LocalUser(
                rs.getInt("jsonLOG"),
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
/*
	id int identity(1,1) primary key,
	name nvarchar(60),
	log_time time,
	log_date date,
	event_status nvarchar(20)
* */
    fun insertLog(name : String, date : String, time : String, event : String){

        val query = "INSERT INTO system_log VALUES ('$name','$time','$date','$event')"

        println(query)

        DBConnection.executeUpdate(query)
    }

    fun parseJson(json : String) : String{
        var result = json.replace("**","{")
        result = result.replace("*","}")
        result = result.replace("_",",")
        return result
    }
}