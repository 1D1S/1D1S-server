package com.odos.odos_server.error.handler;

import com.odos.odos_server.error.exception.CustomException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof CustomException customEx) {
      return GraphqlErrorBuilder.newError(env)
          .message(customEx.getErrorCode().getMessage())
          .errorType(ErrorType.BAD_REQUEST) // graphql의 errorClassification 상속받음
          .path(env.getExecutionStepInfo().getPath())
          .build();
    }

    // fallback
    return GraphqlErrorBuilder.newError(env)
        .message("Internal Server Error")
        .errorType(ErrorType.INTERNAL_ERROR)
        .build();
  }
}
