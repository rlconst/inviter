package auth0.invite

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
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

    @field:Property(name = "client.connection")
    protected lateinit var clientConnection: String

    @field:Property(name = "roles")
    protected lateinit var userRoles: Map<String, String>

    @field:Client("\${client.domain}")
    @Inject
    lateinit var httpClient: HttpClient

    @Option(
        names = ["-e", "--email"],
        description = ["User email to send invite e.g. jdoe@example.com"],
        required = true
    )
    private lateinit var email: String

    @Option(names = ["-n", "--name"], description = ["User name e.g. John Doe"], required = true)
    private lateinit var name: String

    @Option(names = ["-r", "--roles"], description = ["Roles e.g. user"], required = true, arity = "1..*")
    private lateinit var roles: List<String>

    @Option(names = ["-o", "--organization"], description = ["Organization ids org_abcdefgh"], required = true)
    private lateinit var organization: String

    override fun run() {
        try {
            println("Invite $email/$name to $organization with $roles")
            val authResponse = httpClient.toBlocking().retrieve(
                POST(
                    "/oauth/token",
                    AuthRequest("client_credentials", clientId, clientSecret, clientAudience)
                ).header("content-type", MediaType.APPLICATION_FORM_URLENCODED),
                AuthResponse::class.java
            )
            println("Token requested")
            Thread.sleep(100)

            val createUserResponse = httpClient.toBlocking().retrieve(
                POST(
                    "/api/v2/users",
                    CreateUserRequest(email, name, clientConnection, generatePassword(), true)
                )
                    .header("content-type", MediaType.APPLICATION_JSON)
                    .bearerAuth(authResponse.access_token),
                CreateUserResponse::class.java
            )
            println("User ${createUserResponse.user_id} created")
            Thread.sleep(100)

            val roleIds = roles.map { userRoles[it]!! }
            httpClient.toBlocking().retrieve(
                POST(
                    UriBuilder.of("/api/v2/users/{user_id}/roles")
                        .expand(mutableMapOf("user_id" to createUserResponse.user_id)),
                    AssignRoleRequest(roleIds)
                )
                    .header("content-type", MediaType.APPLICATION_JSON)
                    .bearerAuth(authResponse.access_token),
                HttpStatus.NO_CONTENT::class.java
            )
            println("Roles $roleIds granted to ${createUserResponse.user_id}")
            Thread.sleep(100)

            httpClient.toBlocking().retrieve(
                POST(
                    UriBuilder.of("/api/v2/organizations/{org_id}/members")
                        .expand(mutableMapOf("org_id" to organization)),
                    AttachToOrganizationRequest(listOf(createUserResponse.user_id))
                )
                    .header("content-type", MediaType.APPLICATION_JSON)
                    .bearerAuth(authResponse.access_token),
                HttpStatus.NO_CONTENT::class.java
            )
            println("User ${createUserResponse.user_id} invited to $organization")
            Thread.sleep(100)

            httpClient.toBlocking().retrieve(
                POST(
                    "/dbconnections/change_password",
                    ResetPasswordRequest(clientId, clientConnection, email)
                )
                    .header("content-type", MediaType.APPLICATION_JSON)
                    .bearerAuth(authResponse.access_token),
                String::class.java
            )
            println("Password reset for ${createUserResponse.user_id}")

        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(InviterCommand::class.java, *args)
        }
    }
}
