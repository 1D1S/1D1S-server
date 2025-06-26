package com.odos.odos_server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DateInput {
  private int year;
  private int month;
  private int day;
}
