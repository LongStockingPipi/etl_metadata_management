package pers.jason.etl.metadatamanager.core.synchronize;

import pers.jason.etl.metadatamanager.core.support.SynchronizeModel;

/**
 * @author Jason
 * @date 2020/2/22 22:06
 * @description
 */
public interface Synchronized {

  void synchronize(SynchronizeModel model) throws InterruptedException;

}
