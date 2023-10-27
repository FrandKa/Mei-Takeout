package com.mei.service;

import com.mei.dto.UserLoginDTO;
import com.mei.entity.User;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
