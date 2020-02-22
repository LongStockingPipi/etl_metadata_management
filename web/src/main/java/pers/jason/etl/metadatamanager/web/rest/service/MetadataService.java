package pers.jason.etl.metadatamanager.web.rest.service;

import pers.jason.etl.metadatamanager.web.rest.request.SynchronyRequest;

public interface MetadataService {

  void syncMetadata(SynchronyRequest request);

}
