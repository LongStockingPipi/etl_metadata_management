package pers.jason.etl.rest.service;

import pers.jason.etl.rest.pojo.ConnectPioneer;

/**
 * @author Jason
 * @date 2020/2/18 0:50
 * @description
 */
public interface DatabaseService {

  void connect(ConnectPioneer pioneer);
}
