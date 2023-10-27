package com.mei.controller.user;

import com.mei.dto.ShoppingCartDTO;
import com.mei.entity.ShoppingCart;
import com.mei.result.Result;
import com.mei.service.UserShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("查看购物车接口")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getList() {
        List<ShoppingCart> data = userShoppingCartService.getList();
        log.info("查询购物车信息: {}", data);
        return Result.success(data);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result cleanShoppingCart() {
        log.info("清空购物车");
        userShoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @ApiOperation("清除一个商品")
    @PostMapping("/sub")
    public Result deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除一个商品: {}", shoppingCartDTO);
        userShoppingCartService.deleteOne(shoppingCartDTO);

        return Result.success();
    }
}
