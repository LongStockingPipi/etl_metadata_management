package pers.jason.etl.metadatamanager.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;
import pers.jason.etl.metadatamanager.core.synchronize.impl.MetadataSynchronizeTemplate;

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
