package micronaut.aws.lambda.authorizer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import micronaut.aws.lambda.authorizer.dto.AuthPolicy
import micronaut.aws.lambda.authorizer.dto.TokenAuthorizerContext
import org.slf4j.LoggerFactory


class LambdaAuthorizer : RequestHandler<TokenAuthorizerContext, AuthPolicy> {

    private val logger = LoggerFactory.getLogger(LambdaAuthorizer::class.java)

    override fun handleRequest(input: TokenAuthorizerContext, context: Context): AuthPolicy {

        logger.info(input.authorizationToken)
        logger.info(input.methodArn)
        logger.info(input.type)

        //val token = input.getAuthorizationToken()

        // validate the incoming token
        // and produce the principal user identifier associated with the token

        // this could be accomplished in a number of ways:
        // 1. Call out to OAuth provider
        // 2. Decode a JWT token in-line
        // 3. Lookup in a self-managed DB
        val principalId = "xxxx"

        // if the client token is not recognized or invalid
        // you can send a 401 Unauthorized response to the client by failing like so:
        // throw new RuntimeException("Unauthorized");

        // if the token is valid, a policy should be generated which will allow or deny access to the client

        // if access is denied, the client will receive a 403 Access Denied response
        // if access is allowed, API Gateway will proceed with the back-end integration configured on the method that was called

        val methodArn = input.methodArn
        logger.info("methodArn {}", input.methodArn)
        val arnPartials = methodArn.split(":")
        logger.info("arnPartials {}", arnPartials.toString())
        val region = arnPartials[3]
        logger.info("region {}", region)
        val awsAccountId = arnPartials[4]
        logger.info("awsAccountId {}", awsAccountId)
        val apiGatewayArnPartials = arnPartials[5].split("/")
        logger.info("apiGatewayArnPartials {}", apiGatewayArnPartials.joinToString(" "))
        val restApiId = apiGatewayArnPartials[0]
        logger.info("restApiId {}", restApiId)
        val stage = apiGatewayArnPartials[1]
        logger.info("stage {}", stage)
        val httpMethod = apiGatewayArnPartials[2]
        logger.info("httpMethod {}", httpMethod)
        var resource = "" // root resource
        if (apiGatewayArnPartials.size == 4) {
            resource = apiGatewayArnPartials[3]
        }
        logger.info("resource {}", resource)


        // this function must generate a policy that is associated with the recognized principal user identifier.
        // depending on your use case, you might store policies in a DB, or generate them on the fly

        // keep in mind, the policy is cached for 5 minutes by default (TTL is configurable in the authorizer)
        // and will apply to subsequent calls to any method/resource in the RestApi
        // made with the same token

        // the example policy below denies access to all resources in the RestApi

        return if(isValidToken(input.authorizationToken)) {
            logger.info("Allowing access")
            AuthPolicy(principalId, AuthPolicy.PolicyDocument.getAllowAllPolicy(region, awsAccountId, restApiId, stage))
        } else {
            logger.info("Denying access")
            AuthPolicy(principalId, AuthPolicy.PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage))
        }

    }

    private fun isValidToken(token : String) : Boolean {
        return token.length > 5
    }

}
