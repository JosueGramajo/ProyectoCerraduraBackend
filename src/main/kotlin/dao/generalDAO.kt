package dao

import database.DBConnection

object GeneralDao{
    fun insertUser(name : String, id : String){
        DBConnection.executeUpdate("INSERT INTO local_user(name, biometricID, user_state, roleID) VALUES('$name','$id','unauthorized', 2)")
    }

    fun getRoleAndState(id : String) : String{
        val query = "select r.id, u.user_state from local_user u inner join user_role r on (u.roleID = r.id) where u.biometricID = '$id' "
        println(query)
        val rs = DBConnection.executeQuery(query)
        var result = ""
        while (rs!!.next()){
            result = "${rs.getInt("id")}&${rs.getString("user_state")}"
        }

        return result
    }
}