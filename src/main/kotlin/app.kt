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
                println(resp)
                call.respondText { resp }
            }
            post("/changeUserState"){
                val req = call.receive<ChangeState>()

                GeneralDao.changeUserState(req.biometricID, req.user_state)

                call.respondText { "El estado del usuario fue cambiado exitosamente" }
            }
            get("/insertLog"){
                val queryParameters = call.request.queryParameters

                GeneralDao.insertLog(
                    queryParameters.get("name")!!,
                    queryParameters.get("date")!!,
                    queryParameters.get("time")!!,
                    queryParameters.get("event")!!
                )

                call.respondText { "success" }
            }
        }
    }
    server.start(wait = true)
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
)