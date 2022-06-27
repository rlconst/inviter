package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreateUserRequest(
    val email: String,
    val name: String,
    val connection: String,
    val password: String,
    val email_verified: Boolean
)