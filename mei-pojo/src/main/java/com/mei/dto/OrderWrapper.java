package com.mei.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-11-01 19:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWrapper {
    private LocalDateTime min;
    private LocalDateTime max;
    private Integer status;
}
