package pers.jason.etl.metadatamanager.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.core.support.Symbol;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.support.util.MetadataUtil;
import pers.jason.etl.metadatamanager.core.synchronize.Metadata;
import pers.jason.etl.metadatamanager.core.synchronize.external.ExternalPlatform;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;
import pers.jason.etl.metadatamanager.web.MetadataUnitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jason
 * @date 2020/2/19 20:04
 * @description
 */
@SpringBootTest
public class MetadataSyncTemplateTest {

  private static final Integer THREAD_COUNT = 20;

  @Autowired
  private MetadataSynchronizeTemplate template;

  @Test
  public void testForRegisterFullNameInMap() {
    //prepare 800000 data
    ExternalPlatform platform1 = new ExternalPlatform();
    platform1.setTypeCode(0);
    platform1.setId(1L);
    platform1.setName("platform_1");
    platform1.setFullName(Symbol.SIGN_SLASH + MetadataUtil.METADATA_EXTERNAL_FULL_NAME_PREFIX + 1L);
    platform1.setSchemaSet(MetadataUnitUtil.getSchema(platform1.getFullName(), 1L, 3000, 1L));



    ExternalPlatform platform2 = new ExternalPlatform();
    platform2.setTypeCode(0);
    platform2.setId(2L);
    platform2.setName("platform_2");
    platform2.setFullName(Symbol.SIGN_SLASH + MetadataUtil.METADATA_EXTERNAL_FULL_NAME_PREFIX + 2L);
    platform2.setSchemaSet(MetadataUnitUtil.getSchema(platform2.getFullName(), 2L, 3000, 1L));


    Long start = System.currentTimeMillis();
    Map<String, List<Metadata>> map = template.mergeData(platform1, platform2);
    System.out.println("消耗时间：" + (System.currentTimeMillis() - start));
    System.out.println("本地缺失数据：" + map.get("missingData").size() + "; 本地多余数据：" + map.get("refundData").size());
  }

  @Test
  public void successForSync() {
    try {
//      template.synchronize(getSynchronizeModelRemote());
      template.synchronize(getSynchronizeModelLocal());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void successForConcurrentAccess() {
    CountDownLatch cdt = new CountDownLatch(THREAD_COUNT);
    CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_COUNT);
    ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    for (int i = 0; i < THREAD_COUNT; i++) {
      if(0 == (i & 1)) {
        pool.execute(new SyncService(cyclicBarrier, cdt, getSynchronizeModelLocal()));
      } else {
        pool.execute(new SyncService(cyclicBarrier, cdt, getSynchronizeModelRemote()));
      }
    }

    try {
      cdt.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("主线程结束");
  }

  private SynchronizeModel getSynchronizeModelLocal() {
    SynchronizeModel synchronizeModel = new SynchronizeModel();
    synchronizeModel.setUrl("jdbc:mysql://localhost:3306?serverTimezone=UTC");
    synchronizeModel.setUsername("jzh");
    synchronizeModel.setPassword("Jiangzhihao@521");
    synchronizeModel.setDriverName("com.mysql.jdbc.Driver");
    synchronizeModel.setPlatformId(1L);
    return synchronizeModel;
  }

  private SynchronizeModel getSynchronizeModelRemote() {
    SynchronizeModel synchronizeModel = new SynchronizeModel();
    synchronizeModel.setUrl("jdbc:mysql://47.100.102.187:3306?serverTimezone=UTC");
    synchronizeModel.setUsername("root");
    synchronizeModel.setPassword("Hongdou@521");
    synchronizeModel.setDriverName("com.mysql.jdbc.Driver");
    synchronizeModel.setPlatformId(2L);
    return synchronizeModel;
  }

  class SyncService implements Runnable{

    CyclicBarrier cyclicBarrier;

    CountDownLatch countDownLatch;

    SynchronizeModel synchronizeModel;

    public SyncService(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, SynchronizeModel synchronizeModel) {
      this.cyclicBarrier = cyclicBarrier;
      this.countDownLatch = countDownLatch;
      this.synchronizeModel = synchronizeModel;
    }

    @Override
    public void run() {
      try {
        cyclicBarrier.await();
        System.out.println(Thread.currentThread().getName() + "开始运行");
        template.synchronize(synchronizeModel);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      } finally {
        countDownLatch.countDown();
      }
    }

  }
}
