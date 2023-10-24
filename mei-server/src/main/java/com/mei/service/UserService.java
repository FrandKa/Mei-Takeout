package com.mei.service;

import com.mei.dto.UserLoginDTO;
import com.mei.entity.User;
import com.mei.vo.UserLoginVO;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
