package pers.jason.etl.metadatamanager.web.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.web.rest.service.impl.RedisServiceImpl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jason
 * @date 2020/2/19 20:19
 * @description
 */
@SpringBootTest
public class RedisServiceTest {

  @Autowired
  private RedisServiceImpl cacheService;

  @Test
  public void testForSaveAndGet() {
    assert cacheService.setObj("name", "jason");
    assert cacheService.setObj("user_1", new Person("Jason", 27));

    assert "jason".equals(cacheService.getObj("name").orElse(""));
    assert 27 == ((Person) cacheService.getObj("user_1").orElse(null)).getAge();

  }

  @Test
  public void testForDistributedLock() {
    final int threadSize = 10;
    CountDownLatch countDownLatch = new CountDownLatch(threadSize);
    CountDownLatch countDownLatch2 = new CountDownLatch(threadSize);

    String lockName = "sync_lock";
    ExecutorService threadPool = Executors.newFixedThreadPool(threadSize);
    for (int i = 0; i < threadSize; i++) {
      threadPool.submit(() -> {
        String threadName = Thread.currentThread().getName();
        countDownLatch.countDown();
        String v = cacheService.getDistributedLock(lockName);
        if(StringUtils.isEmpty(v)) {
          System.out.println(threadName + "获取锁超时");
        } else {
          System.out.println(threadName + "获取锁成功");
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            cacheService.releaseDistributedLock(lockName, v);
            System.out.println(threadName + "释放锁");
          }
        }
        countDownLatch2.countDown();
      });
    }

    try {
      countDownLatch.await();
      countDownLatch2.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      threadPool.shutdownNow();
    }
  }

}

class Person {
  private String name;
  private Integer age;

  public Person() {
  }

  public Person(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
