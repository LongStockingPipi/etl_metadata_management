package pers.jason.etl.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import pers.jason.etl.rest.pojo.po.Metadata;
import pers.jason.etl.rest.pojo.po.Platform;

import java.util.Map;
import java.util.Set;

/**
 * @author Jason
 * @date 2020/2/18 0:51
 * @description
 */
public abstract class MetadataSynchronizeTemplate {

  private static final String DATA_MISSING = "missingData";

  private static final String DATA_REFUND = "refundData";

  public final void synchronize(Integer syncType, Long platformId, Long schemaId, Long tableId) {
    Platform localData = findDataFromLocal(syncType, platformId, schemaId, tableId);
    Platform remoteData = findDataFromRemote();
    Map<String, Set<Metadata>> discrepantData = mergeData(localData, remoteData);
    processingData(discrepantData);
  }

  protected abstract Platform findDataFromLocal(Integer syncType, Long platformId, Long schemaId, Long tableId);

  protected abstract Platform findDataFromRemote();

  private Map<String, Set<Metadata>> mergeData(Platform localData, Platform remoteData) {
    return null;
  }

  private void processingData(Map<String, Set<Metadata>> discrepantData) {}
}
