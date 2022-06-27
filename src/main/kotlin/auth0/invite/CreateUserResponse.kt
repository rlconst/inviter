package auth0.invite

import io.micronaut.core.annotation.Introspected
import java.time.Instant

@Introspected
data class CreateUserResponse(
    val created_at: Instant,
    val email: String,
    val email_verified: Boolean,
    val identities: List<UserIdentity>,
    val name: String,
    val nickname: String,
    val picture: String,
    val updated_at: Instant,
    val user_id: String,
)

@Introspected
data class UserIdentity(
    val connection: String,
    val user_id: String,
    val provider: String,
    val isSocial: Boolean,
)