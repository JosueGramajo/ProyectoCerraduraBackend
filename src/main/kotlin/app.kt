import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dao.GeneralDao
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.request.receive
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.jackson.jackson
import io.ktor.request.receiveText
import io.ktor.response.respondText
import objects.ChangeState
import objects.LogData
import objects.RegisterRequest

fun main() {

    val server = embeddedServer(Netty, port = 8989) {

        install(ContentNegotiation) {
            jackson {
                disableDefaultTyping()
            }
        }

        routing {
            post("/registerUser") {
                val req = call.receive<RegisterRequest>()

                GeneralDao.insertUser(req.name, req.biometricID)

                call.respondText("Su solicitud de registro fue enviada al administrador, debera esperar a que la acepte")
            }
            post("/getRoleAndState"){
                val id = call.receiveText()
                val resp = GeneralDao.getRoleAndState(id)
                call.respondText { resp }
            }
            post("/changeUserState"){
                val req = call.receive<ChangeState>()

                GeneralDao.changeUserState(req.biometricID, req.user_state)

                call.respondText { "El estado del usuario fue cambiado exitosamente" }
            }
            get("/insertLog"){
                val queryParameters = call.request.queryParameters

                val json = GeneralDao.parseJson(queryParameters.get("jsonLog")!!)

                println(json)

                val mapper = ObjectMapper()

                val log = mapper.readValue<LogData>(json)

                GeneralDao.insertLog(log.name, log.date, log.time, log.status)

                call.respondText { "success" }
            }
            post("/getPendingUsers"){
                val json = GeneralDao.getPendingUsers()

                call.respondText { json }
            }
            post("/getLogs"){
                val json = GeneralDao.getLogs()

                println(json)

                call.respondText { json }
            }
        }
    }
    server.start(wait = true)
}