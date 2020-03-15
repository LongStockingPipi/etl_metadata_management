package pers.jason.etl.metadatamanager.web.rest.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.metadatamanager.core.support.PlatformType;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.web.exception.PlatformNotFoundException;
import pers.jason.etl.metadatamanager.web.rest.dao.ExternalPlatformDao;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataCrudService;
import pers.jason.etl.metadatamanager.web.rest.service.SynchronizeServiceHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static pers.jason.etl.metadatamanager.core.support.MetadataType.SCHEMA;

/**
 * @author Jason
 * @date 2020/3/15 16:30
 * @description 适用于mysql、oracle、sql server
 */
public abstract class JdbcMetadataSynchronizeService extends MetadataSynchronizeTemplate {

  private static final Logger logger = LoggerFactory.getLogger(MetadataSynchronizeTemplate.class);

  @Autowired
  private ExternalPlatformDao platformDao;

  @Autowired
  private SynchronizeServiceHolder synchronizeServiceHolder;

  @Override
  protected Platform findDataFromLocal(Long platformId, Long schemaId, Long tableId) {
    Platform platform = platformDao.findAll(platformId, schemaId, tableId);
    if(null == platform) {
      throw new PlatformNotFoundException("the platform information is not available locally");
    }
    return platform;
  }

  @Override
  protected void processingData(Platform localData, Map<String, List<Metadata>> discrepantData) {
    MetadataCrudService metadataCrudService = synchronizeServiceHolder.findMetadataCrudService(PlatformType.MYSQL);
    List<Metadata> mis = discrepantData.get(DATA_MISSING);
    List<Metadata> ref = discrepantData.get(DATA_REFUND);
    logger.info(Thread.currentThread().getName() + "本地缺失数据：" + mis.size() + "；本地多余数据：" + ref.size());
    metadataCrudService.deleteMetadata(ref);

    if(!CollectionUtils.isEmpty(mis)) {
      List<Metadata> missingData =
          removeRedundancyMetadataAndSetParentId(localData, Lists.newArrayList(mis));

      metadataCrudService.insertMetadata(missingData);
    }

    //todo 更新缓存
  }

  /**
   * 去除重复数据
   * 为新数据添加父ID
   * 时间复杂度(n*m)
   * @param localData
   * @param missingData
   * @return
   */
  private List<Metadata> removeRedundancyMetadataAndSetParentId(Platform localData, List<Metadata> missingData) {
    //排序，保证元数据顺序是p-s-t-c
    Collections.sort(missingData);

    //准备已存在的元数据id映射关系
    Map<String, Long> fullNameAndId = Maps.newHashMap();
    registerFullNameAndIdInMap(localData, fullNameAndId);

    //去重后的fullName集合
    List<String> names = Lists.newArrayList();
    missingData.forEach(metadata -> {
      names.add(metadata.getFullName());
    });
    Collections.sort(names);

    //去重后的metadata集合
    List<Metadata> newMetadata = Lists.newArrayList();
    missingData.forEach(metadata -> {
      if(null != metadata && null == metadata.getId()) {
        String fullName = metadata.getFullName();
        String parentFullName = MetadataUtil.getParentFullName(fullName);
        if(!names.contains(parentFullName)) { //断层插入
          if(!SCHEMA.equals(metadata.returnMetadataType())) {
            if(null == metadata.getId()) { //为新数据查询父ID
              metadata.setParentId(fullNameAndId.get(parentFullName));
            }
          }
          newMetadata.add(metadata);
        }
      }
    });
    return newMetadata;
  }

  private void registerFullNameAndIdInMap(Metadata metadata, Map<String, Long> map) {
    Set<Metadata> child = metadata.getChild();
    if(!CollectionUtils.isEmpty(child)) {
      for(Metadata data : child) {
        registerFullNameAndIdInMap(data, map);
      }
    }

    map.put(metadata.getFullName(), metadata.getId());
  }
}
