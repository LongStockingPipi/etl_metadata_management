package pers.jason.etl.rest.service;

import pers.jason.etl.rest.pojo.ConnectPioneer;
import pers.jason.etl.rest.pojo.dto.SimpleResponse;

/**
 * @author Jason
 * @date 2020/2/18 0:50
 * @description
 */
public interface DatabaseService {

  String SQL_EXIST_TABLE = "select exists(select * from INFORMATION_SCHEMA.TABLES where table_schema = '%s' and table_name = '%s') as is_exist";

  String SQL_RESULT_COLUMN_LABEL_IS_EXIST = "is_exist";

  boolean connect(ConnectPioneer pioneer);
}
