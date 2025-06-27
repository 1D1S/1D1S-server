package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.dto.GoalDto;
import com.odos.odos_server.domain.diary.entity.DiaryGoal;

public record DiaryGoalDTO(Long id, GoalDto goal, Boolean isAchieved, Integer streakCount) {
  // 원하는 정보를 뿌려주기 위해서 프론트에, DTO를 설계하는 것이 중요하고 변환하는 것도 잘 알아야하고
  // 서비스와 컨트롤러 작성하기 전에 다 해놓는게 좋음
  // 연관관계가 많은 엔티티의 API를 개발해보니 참 많다...
  public static DiaryGoalDTO from(DiaryGoal goal) {
    // int streakCount = 1 /* 계산 로직 넣기 */ ;
    // return new DiaryGoalDTO(
    // goal.getId(),
    // goal.getChallengeGoal().getDiaryGoals().get(.getChallengeGoal()),
    // goal.getGoalCompleted(),
    // streakCount
    // );
    return null;
  }
}
