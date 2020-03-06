package pers.jason.etl.metadatamanager.core.lock;

/**
 * @author Jason
 * @date 2020/3/7 1:27
 * @description
 */
public interface Distributable<T> {

  T getDistributedLock(String lockName);

  void releaseDistributedLock(String lockName, T t);
}
