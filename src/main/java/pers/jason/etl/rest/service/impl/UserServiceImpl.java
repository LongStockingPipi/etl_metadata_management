package pers.jason.etl.rest.service.impl;

import org.springframework.stereotype.Service;
import pers.jason.etl.rest.service.UserService;

/**
 * @author Jason
 * @date 2020/2/22 3:01
 * @description
 */
@Service
public class UserServiceImpl implements UserService {
  @Override
  public Long getUserNow() {
    return 1L;
  }
}
