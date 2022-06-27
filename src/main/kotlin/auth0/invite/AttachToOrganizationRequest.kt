package auth0.invite

import io.micronaut.core.annotation.Introspected

@Introspected
data class AttachToOrganizationRequest(
    val members: List<String>
)