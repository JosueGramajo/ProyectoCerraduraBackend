package database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.DriverManager
import java.sql.ResultSet



object DBConnection {

    fun createConnection() : Connection{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance()

        val cn = DriverManager.getConnection(
            "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CerraduraDB;integratedSecurity=false",
            "sa",
            "123456"
        )

        return cn
    }

    fun executeQuery(query: String): ResultSet? {
        var rs: ResultSet? = null

        try {
            val connection = createConnection()

            val ps = connection.prepareCall(query)
            rs = ps.executeQuery()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rs
    }

    fun executeUpdate(query: String) {
        val rs: ResultSet? = null

        try {
            val connection = createConnection()

            val ps = connection.prepareCall(query)
            ps.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}