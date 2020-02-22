package pers.jason.etl.metadatamanager.web.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.jason.etl.metadatamanager.core.cache.CacheTemplate;

/**
 * @author Jason
 * @date 2020/2/19 20:19
 * @description
 */
@SpringBootTest
public class RedisServiceTest {

  @Autowired
  private CacheTemplate cacheService;

  @Test
  public void testForSaveAndGet() {
    assert cacheService.setObj("name", "jason");
    assert cacheService.setObj("user_1", new Person("Jason", 27));

    assert "jason".equals(cacheService.getObj("name").orElse(""));
    assert 27 == ((Person) cacheService.getObj("user_1").orElse(null)).getAge();
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
