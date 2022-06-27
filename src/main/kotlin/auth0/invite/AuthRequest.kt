package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class AuthRequest(
    val grant_type: String,
    val client_id: String,
    val client_secret: String,
    val audience: String,
)