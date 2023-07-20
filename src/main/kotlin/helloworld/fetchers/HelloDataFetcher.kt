package helloworld.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton

@Singleton
class HelloDataFetcher : DataFetcher<String> {

    override fun get(env: DataFetchingEnvironment): String {
        var name = env.getArgument<String>("name")
        if (name == null || name.trim().isEmpty()) {
            name = "World"
        }
        return "Hello $name!"
    }
}