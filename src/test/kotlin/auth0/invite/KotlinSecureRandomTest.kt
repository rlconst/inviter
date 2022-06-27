package auth0.invite

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class KotlinSecureRandomTest : StringSpec({
    "test password not repeated" {
        generatePassword() shouldNotBe generatePassword()
        generatePassword().length shouldBe 16
    }
})