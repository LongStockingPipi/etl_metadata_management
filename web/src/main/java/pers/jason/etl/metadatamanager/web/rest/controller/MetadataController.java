package pers.jason.etl.metadatamanager.web.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.jason.etl.metadatamanager.web.rest.request.SynchronyRequest;
import pers.jason.etl.metadatamanager.web.rest.service.MetadataService;

/**
 * @author Jason
 * @date 2020/3/4 14:46
 * @description
 */
@RestController
@RequestMapping("metadata")
public class MetadataController {

  @Autowired
  private MetadataService metadataService;

  @PostMapping("sync")
  public void sync(SynchronyRequest request) {
    metadataService.syncMetadata(request);
  }


}
