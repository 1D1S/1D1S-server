package com.odos.odos_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureGraphQlTester
@ActiveProfiles("test")
class MemberQueryControllerTest {

  @Autowired private GraphQlTester graphQlTester;

  /*@Test
  void getMember_NotFound_ThrowsCustomException() {
    Long invalidId = 999L;

    graphQlTester
        .documentName("getMember") // src/test/resources/graphql-test/getMember.graphql
        .variable("id", invalidId)
        .execute()
        .errors()
        .expect(error -> error.getMessage().contains("Member not found"))
        .expect(error -> error.getErrorType().toString().equals("BAD_REQUEST"));
  } */

  /* @Test
  void getMember_ValidId_ReturnsMember() {
    // Long validId = 1L;
    String validNickname = "testMember";

    graphQlTester
        .documentName("getMember")
        .variable("nickname", validNickname)
        .execute()
        // .path("getMember.memberId").hasValue()
        .path("getMember.nickname")
        .hasValue()
        .path("getMember.nickname")
        .entity(String.class)
        .satisfies(
            name -> {
              assertThat(name).isEqualTo("testMember");
            });
  } */
}
