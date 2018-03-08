package io.github.ma1uta.identity

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jidentity")
class IdentityProperties {

    val longTermKeys = KeyStore()

    val shortTermKeys = KeyStore()

    val usedShortTermKeys = KeyStore()

    var initialShortKeyPool = 20

    val selfKeyGenerator = SelfKeyGenerator()

    var useServerKey = false

    var hostname = "localhost"

    class SelfKeyGenerator {

        var issuerName = "jidentity"

        var secureRandomSeed = "seed"

        var serialNumberLength = 20

        var certificateValidTs: Long = 60 * 60 * 24 * 30 * 3
    }

    class KeyStore {

        var keyStore = "keyStore.jks"

        var keyStorePassword = "dummy"

        var keyPassword = "dummy"

        var keyStoreType = "pkcs12"

    }
}
