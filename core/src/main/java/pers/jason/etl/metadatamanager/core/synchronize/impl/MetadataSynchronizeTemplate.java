package pers.jason.etl.metadatamanager.core.synchronize.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.MetadataSynchronize;
import pers.jason.etl.metadatamanager.core.synchronize.Platform;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jason
 * @date 2020/2/22 22:23
 * @description
 */
public abstract class MetadataSynchronizeTemplate implements MetadataSynchronize {

  private static final Logger logger = LoggerFactory.getLogger(MetadataSynchronizeTemplate.class);

  protected static final String DATA_MISSING = "missingData";

  protected static final String DATA_REFUND = "refundData";

  private static final ConcurrentHashMap<String, ThreadLock> lockMap = new ConcurrentHashMap<>();

  /**
   * 抽象同步方法
   * 当前锁竞争机制只适合单节点发布，如果使用分布式，需要分布式锁解决方案
   * @param synchronizeModel
   * @throws InterruptedException
   */
  @Override
  public final void synchronize(SynchronizeModel synchronizeModel) throws InterruptedException {
    String threadName = Thread.currentThread().getName();
    Long platformId = synchronizeModel.getPlatformId();
    String lockName = "sync_lock_name_" + platformId;
    ThreadLock syncLock = lockMap.computeIfAbsent(lockName, k -> new ThreadLock(platformId));

    Platform remoteData = findDataFromRemote(synchronizeModel.getUrl(), synchronizeModel.getUsername(), synchronizeModel.getPassword()
        , platformId, synchronizeModel.getSchemaName(), synchronizeModel.getTableName());

    logger.info(threadName + "：开始申请锁" + syncLock.getPlatform());
    if(syncLock.tryLock(15, TimeUnit.SECONDS)) {
      logger.info(threadName + "：申请锁成功" + syncLock.getPlatform());
      try {
        Platform localData = findDataFromLocal(platformId, synchronizeModel.getSchemaId(), synchronizeModel.getTableId());
        Map<String, List<Metadata>> discrepantData = mergeData(localData, remoteData);
        processingData(localData, discrepantData);
      } finally {
        logger.info(threadName + "：运行完成" + syncLock.getPlatform());
        syncLock.unlock();
      }
    } else {
      logger.info(threadName + "：同步超时" + syncLock.getPlatform());
    }

  }

  protected abstract Platform findDataFromLocal(Long platformId, Long schemaId, Long tableId);

  protected abstract Platform findDataFromRemote(String url, String username, String password, Long platformId, String schemaName, String tableName);

  private Map<String, List<Metadata>> mergeData(Platform localData, Platform remoteData) {
    Map<String, List<Metadata>> diff = Maps.newHashMap();
    if(null == remoteData) {
      throw new RuntimeException("外部数据库server无数据");
    }
    if(null == localData) {
      diff.put(DATA_MISSING, Lists.newArrayList(remoteData.getChild()));
      return diff;
    }

    Map<String, CountAndMetadata> middleWare = Maps.newHashMap();
    //组装map，时间复杂度为On
    registerFullNameInMap(localData, true, middleWare);
    registerFullNameInMap(remoteData, false, middleWare);

    //分类元数据，时间复杂度为On
    List<Metadata> refundData = Lists.newArrayList();
    List<Metadata> missingData = Lists.newArrayList();
    Set<Map.Entry<String, CountAndMetadata>> entrySet = middleWare.entrySet();
    Iterator<Map.Entry<String, CountAndMetadata>> iter = entrySet.iterator();
    while (iter.hasNext()) {
      Map.Entry<String, CountAndMetadata> entry = iter.next();
      CountAndMetadata countAndMetadata = entry.getValue();
      if(1 == countAndMetadata.count) {
        refundData.add(countAndMetadata.metadata);
      } else if(-1 == countAndMetadata.count) {
        missingData.add(countAndMetadata.metadata);
      }
    }

    diff.put(DATA_MISSING, missingData);
    diff.put(DATA_REFUND, refundData);

    return diff;
  }

  protected abstract void processingData(Platform localData, Map<String, List<Metadata>> discrepantData);

  /**
   * 将元数据的fullName放入map中，k是fullName，v是计数器+元数据对象的组合
   * 最终正反两次计算，标记两次元数据各自独立的数据
   * @param metadata
   * @param sign
   * @param map
   */
  private void registerFullNameInMap(final Metadata metadata, boolean sign, Map<String, CountAndMetadata> map) {
    Set<Metadata> child = metadata.getChild();
    if(!CollectionUtils.isEmpty(child)) {
      for(Metadata data : child) {
        registerFullNameInMap(data, sign, map);
      }
    }

    String fn = metadata.getFullName();
    CountAndMetadata v = map.containsKey(fn) ? map.get(fn) : new CountAndMetadata(0, metadata);
    map.put(fn, sign ? v.incrementCount() : v.decrementCount());
  }

  class CountAndMetadata {

    private Integer count;

    private Metadata metadata;

    public CountAndMetadata(Integer count, Metadata metadata) {
      this.count = count;
      this.metadata = metadata;
    }

    public CountAndMetadata incrementCount() {
      this.count++;
      return this;
    }

    public CountAndMetadata decrementCount() {
      this.count--;
      return this;
    }
  }

  class ThreadLock extends ReentrantLock{
    private Long platform;

    public ThreadLock(Long platform) {
      this.platform = platform;
    }

    public Long getPlatform() {
      return platform;
    }
  }
}
