package pers.jason.etl.metadatamanager.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;

import java.util.concurrent.BrokenBarrierException;
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

  @Autowired
  private MetadataSynchronizeTemplate template;

  @Test
  public void successForSync() {
    try {
      SynchronizeModel synchronizeModel = getSynchronizeModel();
      template.synchronize(synchronizeModel);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void successForConcurrentAccess() {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(20);
    ExecutorService pool = Executors.newFixedThreadPool(20);
    for (int i = 0; i < 20; i++) {
      pool.execute(new SyncService(cyclicBarrier, template));
    }
    System.out.println("初始化完成");
  }

  private SynchronizeModel getSynchronizeModel() {
    SynchronizeModel synchronizeModel = new SynchronizeModel();
    synchronizeModel.setUrl("jdbc:mysql://localhost:3306?serverTimezone=UTC");
    synchronizeModel.setUsername("jzh");
    synchronizeModel.setPassword("Jiangzhihao@521");
    synchronizeModel.setDriverName("com.mysql.jdbc.Driver");
    synchronizeModel.setPlatformId(1L);
//    synchronizeModel.setSchemaId(1L);
//    synchronizeModel.setTableId(1L);
//    synchronizeModel.setSchemaName("s_test");
//    synchronizeModel.setTableName("t_test");
    return synchronizeModel;
  }

  class SyncService implements Runnable{

    CyclicBarrier cyclicBarrier;

    MetadataSynchronizeTemplate template;

    public SyncService(CyclicBarrier cyclicBarrier, MetadataSynchronizeTemplate template) {
      this.cyclicBarrier = cyclicBarrier;
      this.template = template;
    }

    @Override
    public void run() {
      try {
        cyclicBarrier.await();
        System.out.println(Thread.currentThread().getName() + "开始运行");
        template.synchronize(getSynchronizeModel());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      }
    }

  }
}
