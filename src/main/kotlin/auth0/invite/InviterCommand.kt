package auth0.invite

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
    name = "inviter", description = ["..."],
    mixinStandardHelpOptions = true
)
open class InviterCommand : Runnable {
    @field:Property(name = "client.id")
    protected lateinit var clientId: String

    @field:Property(name = "client.secret")
    protected lateinit var clientSecret: String

    @field:Property(name = "client.domain")
    protected lateinit var clientDomain: String

    @field:Property(name = "client.audience")
    protected lateinit var clientAudience: String

    @field:Client("\${client.domain}")
    @Inject
    lateinit var httpClient: HttpClient

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose: Boolean = false

    override fun run() {
        val authRequest = POST(
            "/oauth/token",
            AuthRequest("client_credentials", clientId, clientSecret, clientAudience)
        ).header("content-type", MediaType.APPLICATION_FORM_URLENCODED)

        try {
            val response = httpClient.toBlocking().retrieve(authRequest, AuthResponse::class.java)
            println(response)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }


        if (verbose) {
            println("hi")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(InviterCommand::class.java, *args)
        }
    }
}
