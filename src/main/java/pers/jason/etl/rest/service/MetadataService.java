package pers.jason.etl.rest.service;

import pers.jason.etl.rest.request.SynchronyRequest;

public interface MetadataService {

  String SUFFIX_TEMPLATE_SERVICE_NAME = "SynchronizeTemplate";

  void syncMetadata(SynchronyRequest request);



}
