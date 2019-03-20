package micronaut.aws.lambda.authorizer.web

import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import micronaut.aws.lambda.authorizer.LambdaAuthorizer
import micronaut.aws.lambda.authorizer.dto.AuthPolicy
import org.slf4j.LoggerFactory

@Controller("/")
class AuthorizerController {

    private val logger = LoggerFactory.getLogger(LambdaAuthorizer::class.java)

    @Get
    fun authorize(request: HttpRequest<String>): AuthPolicy {

        logger.info(request.body.toString())
        logger.info(request.headers.toString())
        logger.info(request.headers.values().toString())
        logger.info(request.headers["Jwt-Authorization"])

        request.headers.forEach {
            logger.info("{} - {}", it.key, it.value)
        }

        return AuthPolicy("", AuthPolicy.PolicyDocument.getDenyAllPolicy("", "", "", ""))


    }

}
