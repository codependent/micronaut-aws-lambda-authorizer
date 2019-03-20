package micronaut.aws.lambda.authorizer

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("micronaut.aws.lambda.authorizer")
                .mainClass(Application.javaClass)
                .start()
    }
}