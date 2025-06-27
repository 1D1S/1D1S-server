package com.odos.odos_server.error.handler;

import com.odos.odos_server.error.exception.CustomException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomGraphQlExceptionHandler implements DataFetcherExceptionResolver {

  @Override
  public Mono<List<GraphQLError>> resolveException(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof CustomException customEx) {
      GraphQLError error =
          GraphqlErrorBuilder.newError(env)
              .message(customEx.getMessage())
              .errorType(ErrorType.BAD_REQUEST)
              .build();
      return Mono.just(List.of(error));
    }
    return Mono.empty();
  }
}
