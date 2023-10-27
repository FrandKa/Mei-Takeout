package com.mei.controller.user;

import com.mei.dto.ShoppingCartDTO;
import com.mei.result.Result;
import com.mei.service.UserShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-27 16:08
 **/

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "用户购物车模块")
@Slf4j
public class UserShoppingCartController {
    @Autowired
    private UserShoppingCartService userShoppingCartService;

    @ApiOperation("添加购物车的功能")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车: {}", shoppingCartDTO);
        userShoppingCartService.add(shoppingCartDTO);

        return Result.success();
    }
}
