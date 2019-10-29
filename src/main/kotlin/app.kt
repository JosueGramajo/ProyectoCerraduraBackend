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

                GeneralDao.insertUser(req.name, req.id)

                call.respondText("Su solicitud de registro fue enviada al administrador, debera esperar a que la acepte")
            }
            post("/getRoleAndState"){
                val id = call.receiveText()
                val resp = GeneralDao.getRoleAndState(id)
                println(resp)
                call.respondText { resp }
            }
            get("/test"){

            }
        }
    }
    server.start(wait = true)
}

data class RegisterRequest(
        val name : String,
        val id : String
){
    constructor() : this("","")
}