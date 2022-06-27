package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class AssignRoleRequest(
    val roles: List<String>
)