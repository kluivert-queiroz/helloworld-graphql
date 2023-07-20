package helloworld.factories

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import helloworld.fetchers.HelloDataFetcher
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import jakarta.inject.Singleton
import java.io.BufferedReader
import java.io.InputStreamReader

@Factory // (1)
class GraphQLFactory {

    @Bean
    @Singleton
    fun graphQL(resourceResolver: ResourceResolver, helloDataFetcher: HelloDataFetcher): GraphQL { // (2)

        val schemaParser = SchemaParser()
        val schemaGenerator = SchemaGenerator()

        // Parse the schema.
        val typeRegistry = TypeDefinitionRegistry();
        typeRegistry.merge(schemaParser.parse(BufferedReader(InputStreamReader(
            resourceResolver.getResourceAsStream("classpath:schema.graphqls").get()))))

        // Create the runtime wiring.
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query") { typeWiring -> typeWiring
                .dataFetcher("hello", helloDataFetcher) }
            .build()

        // Create the executable schema.
        val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}