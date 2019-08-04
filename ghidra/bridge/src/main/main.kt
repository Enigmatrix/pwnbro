package main

import ghidra.GhidraJarApplicationLayout
import ghidra.framework.HeadlessGhidraApplicationConfiguration
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import model.Binary
import model.Repository
import org.slf4j.event.Level
import routes.BinaryService
import routes.RepositoryService
import routes.routesFor
import util.RepositoryUtil.initServer
import ghidra.framework.Application as GhidraApplication


fun main() {
    init()
    embeddedServer(Netty, 8000,
        module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        //Jackson allows you to handle JSON content easily
        jackson { }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/api") }
    }

    val binSvc = BinaryService()
    val repoSvc = RepositoryService()

    routing {
        route("api") {
            routesFor(binSvc)
            routesFor(repoSvc)
        }
    }
}

fun init() {
    if (!GhidraApplication.isInitialized())
        GhidraApplication.initializeApplication(
            GhidraJarApplicationLayout(),
            HeadlessGhidraApplicationConfiguration()
        )

    initServer()
}

/*
    /*val run3 = measureTimeMillis {
        val repo = GhidraURLConnection(URL("ghidra", host, "/" + server.repositoryNames[0]))
        repo.isReadOnly = false

        val file = repo.projectData.rootFolder.files[0] as GhidraFile

        var program = file.getDomainObject(2, true, false, TaskMonitor.DUMMY) as ProgramDB
        println(program.name)
        program = edit(file, "Rename") { prog ->
            val main = prog.listing.getFunctions(true)
            .first { it.name == "FUN_00101069" }
            main.setName("main", SourceType.USER_DEFINED)
        }
    }

    println("rename took: ${run3/1000.0}")
*/

}

//TODO change this to an extension method of domainfile?
fun edit(file: DomainFile, msg: String, block: (ProgramDB) -> Unit): ProgramDB  {
    //TODO check return code
    file.checkout(false, TaskMonitor.DUMMY)
    val program = file.getDomainObject(2, true, false, TaskMonitor.DUMMY) as ProgramDB
    val tx = program.startTransaction(msg)
    //TODO try catch
    block(program)
    //TODO wtf is this boolean?
    program.endTransaction(tx, true)
    file.save(TaskMonitor.DUMMY)
    file.checkin(CheckinWithComment(msg), true, TaskMonitor.DUMMY)
    return program
}

class CheckinWithComment(val cmt: String): CheckinHandler {
    override fun getComment() = cmt
    override fun createKeepFile() = false
    override fun keepCheckedOut() = false

}
*/
