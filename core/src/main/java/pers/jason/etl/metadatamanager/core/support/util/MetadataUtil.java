package pers.jason.etl.metadatamanager.core.support.util;

import org.apache.commons.lang3.StringUtils;
import pers.jason.etl.metadatamanager.core.connect.ConnectPioneer;
import pers.jason.etl.metadatamanager.core.support.MetadataType;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.Symbol;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalColumnType;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalTableType;
import pers.jason.etl.metadatamanager.core.synchronize.internal.VirtualPlatform;

import java.util.Map;

import static pers.jason.etl.metadatamanager.core.support.PlatformType.MYSQL;
import static pers.jason.etl.metadatamanager.core.support.PlatformType.ORACLE;

/**
 * @author Jason
 * @date 2020/2/18 1:22
 * @description
 */
public class MetadataUtil {

  public static final String DRIVER_NAME_MYSQL = "com.mysql.jdbc.Driver";

  public static final String DRIVER_NAME_ORACLE = "com.mysql.jdbc.Driver";

  public static final String URL_PREFIX_MYSQL = "jdbc:mysql://";

  public static final String METADATA_EXTERNAL_FULL_NAME_PREFIX = "metadata_external_";

  public static VirtualPlatform getVirtualPlatform() {
    return new VirtualPlatform();
  }

  public static String getDriverNameByType(PlatformType type) {
    if(MYSQL.equals(type)) {
      return DRIVER_NAME_MYSQL;
    } else if(ORACLE.equals(type)) {
      return DRIVER_NAME_ORACLE;
    } else {
      return null;
    }
  }

  public static String getSuffixByType(PlatformType type) {
    if(MYSQL.equals(type)) {
      return URL_PREFIX_MYSQL;
    } else if(ORACLE.equals(type)) {
      return "";
    } else {
      return null;
    }
  }

  public static String joinProps(final Map<String, String> props) {
    StringBuilder sb = new StringBuilder();
    if(null != props && props.size() > 0) {
      for(Map.Entry entry : props.entrySet()) {
        sb.append(entry.getKey()).append(Symbol.SIGN_EQUALS).append(entry.getValue()).append(Symbol.SIGN_AND);
      }
    }
    return sb.toString();
  }

  public static String getUrlByPioneer(final ConnectPioneer pioneer) {
    StringBuilder sb = new StringBuilder();
    if(null == pioneer) {
      return "";
    }
    PlatformType type = pioneer.getPlatformType();
    String suffix = getSuffixByType(type);
    String url = pioneer.getUrl();
    if(StringUtils.isNotEmpty(url)) {
      sb.append(suffix).append(url);
      String schemaName = pioneer.getSchemaName();
      if(StringUtils.isNotEmpty(schemaName)) {
        sb.append(Symbol.SIGN_SLASH).append(schemaName);
      }
      Map<String, String> props = pioneer.getProps();
      if(null != props && !props.isEmpty()) {
        sb.append(Symbol.QUESTION_MARK).append(MetadataUtil.joinProps(props));
        sb.deleteCharAt(sb.length() - 1).toString();
      }
    }
    return sb.toString();
  }

  public static String getExternalMetadataFullName(MetadataType metadataType, Long platformId, String schemaName
      , String tableName, ExternalTableType tableType, String columnName, ExternalColumnType columnType, int columnPosition) {
    StringBuilder sb = new StringBuilder(Symbol.SIGN_SLASH).append(METADATA_EXTERNAL_FULL_NAME_PREFIX).append(platformId);
    if(metadataType.code > 0) {
      sb.append(Symbol.SIGN_SLASH).append(schemaName);
      if(metadataType.code > 1) {
        sb.append(Symbol.SIGN_SLASH).append(tableName).append(Symbol.SIGN_UNDERLINE).append(tableType.code);
        if(metadataType.code > 2) {
          sb.append(Symbol.SIGN_SLASH).append(columnName).append(Symbol.SIGN_UNDERLINE).append(columnType.getCode())
              .append(Symbol.SIGN_UNDERLINE).append(columnPosition);
        }
      }
    }
    return sb.toString();
  }

  public static String getParentFullName(String fullName) {
    if(StringUtils.isNotEmpty(fullName)) {
      String[] array = fullName.split(Symbol.SIGN_SLASH);
      if(array.length > 2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
          sb.append(array[i]).append(Symbol.SIGN_SLASH);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
      } else if(array.length == 2) {
        return fullName;
      } else {
        throw new RuntimeException("fullName格式错误");
      }
    } else {
      throw new RuntimeException("fullName不能为空");
    }
  }



}
