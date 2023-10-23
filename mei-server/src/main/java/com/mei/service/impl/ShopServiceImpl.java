package com.mei.service.impl;

import com.mei.constant.ShopStatusConstant;
import com.mei.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 15:33
 **/
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Integer getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(ShopStatusConstant.KEY);
        log.info("查询店铺状态: {}", status);
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set(ShopStatusConstant.KEY, status);
    }
}
