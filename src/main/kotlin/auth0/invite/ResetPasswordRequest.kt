package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class ResetPasswordRequest(
    val client_id: String,
    val connection: String,
    val email: String,
)