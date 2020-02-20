package pers.jason.etl.rest.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.commons.Symbol;
import pers.jason.etl.rest.pojo.ConnectPioneer;
import pers.jason.etl.rest.pojo.ExternalColumnType;
import pers.jason.etl.rest.pojo.ExternalTableType;
import pers.jason.etl.rest.pojo.MetadataType;
import pers.jason.etl.rest.pojo.PlatformType;
import pers.jason.etl.rest.pojo.VirtualPlatform;
import pers.jason.etl.rest.pojo.po.Platform;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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
    if(PlatformType.MYSQL.equals(type)) {
      return DRIVER_NAME_MYSQL;
    } else if(PlatformType.ORACLE.equals(type)) {
      return DRIVER_NAME_ORACLE;
    } else {
      return null;
    }
  }

  public static String getSuffixByType(PlatformType type) {
    if(PlatformType.MYSQL.equals(type)) {
      return URL_PREFIX_MYSQL;
    } else if(PlatformType.ORACLE.equals(type)) {
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
    return sb.toString().toLowerCase();
  }



}
