package pers.jason.etl.metadatamanager.web.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author Jason
 * @date 2020/2/19 19:03
 * @description
 */
@Configuration
public class RedisConfig {

  /**
   * 配置Redis的序列化方式，
   * key使用JdkSerializationRedisSerializer，序列化后结果长度最小；
   * value使用Jackson2JsonRedisSerializer，序列化效率高
   * @param factory
   * @return
   */
  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(getJdkSerializationRedisSerializer());
    redisTemplate.setValueSerializer(getJackson2JsonRedisSerializer());
    return redisTemplate;
  }

  @Bean(name = "jackson2JsonRedisSerializer")
  public Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer() {
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    return jackson2JsonRedisSerializer;
  }

  @Bean(name = "jdkSerializationRedisSerializer")
  public JdkSerializationRedisSerializer getJdkSerializationRedisSerializer() {
    return new JdkSerializationRedisSerializer();
  }

  @Bean(name = "stringRedisSerializer")
  public StringRedisSerializer getStringRedisSerializer() {
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    return stringRedisSerializer;
  }

  @Bean(name = "lockScript")
  public ScriptSource lockScript() {
    return new ResourceScriptSource(new ClassPathResource("script/lua/lock.lua"));
  }

  @Bean(name = "unLockScript")
  public ScriptSource unLockScript() {
    return new ResourceScriptSource(new ClassPathResource("script/lua/unlock.lua"));
  }
}
