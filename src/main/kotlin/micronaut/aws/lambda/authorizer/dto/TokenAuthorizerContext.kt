package micronaut.aws.lambda.authorizer.dto

data class TokenAuthorizerContext(var type: String, var authorizationToken: String, var methodArn: String) {

    constructor() : this("", "", "")

}
