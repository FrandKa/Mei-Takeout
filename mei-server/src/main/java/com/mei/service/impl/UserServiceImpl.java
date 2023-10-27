package com.mei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mei.constant.MessageConstant;
import com.mei.dto.UserLoginDTO;
import com.mei.entity.User;
import com.mei.exception.LoginFailedException;
import com.mei.mapper.UserMapper;
import com.mei.properties.WeChatProperties;
import com.mei.service.UserService;
import com.mei.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-24 10:57
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();

        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        // 1. 调用接口服务获取openId:
        String res = HttpClientUtil.doGet(
                weChatProperties.getWeiChatUrl(),
                params
        );
        // 获取openId: {"session_key":"vUwYlqpMAGMaq5OuQKFA1g==","openid":"oGwSM60lU9g4KyAXeTOZo4j4GZGQ"}
        JSONObject jsonObject = JSON.parseObject(res);
        String openid = jsonObject.getString("openid");
        log.info("获取openId: {}", openid);
        // 2. 判断openId是否为空:
        // 为空抛出异常
        if(StringUtils.isBlank(openid)) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 3. 判断用户是否存在(openId):
        // 不存在: 存储openId >> 创建user >> 自动完成注册
        User user = userMapper.queryByOpenid(openid);
        if(Objects.nonNull(user)) {
            return user;
        }
        user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
        userMapper.insert(user);
        return user;
    }
}
