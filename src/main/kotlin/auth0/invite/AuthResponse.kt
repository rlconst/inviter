package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class AuthResponse(
    val access_token: String,
    // Should be "create:users create:role_members create:organization_members"
    val scope: String,
    val expires_in: Long,
    val token_type: String,
)