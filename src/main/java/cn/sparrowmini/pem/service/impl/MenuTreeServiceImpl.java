package cn.sparrowmini.pem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.sparrowmini.common.api.AbstractTreeService;
import cn.sparrowmini.common.SparrowTree;
import cn.sparrowmini.pem.model.Menu;

@Service
public class MenuTreeServiceImpl extends AbstractTreeService<Menu, String> {

//  @Autowired
//  OrganizationRelationRepository organizationRelationRepository;

  @Override
  public SparrowTree<Menu, String> buildTree(String parentId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SparrowTree<Menu, String> buildTreeWithParent(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<SparrowTree<Menu, String>> getChildren(String parentId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isChild(String childId, String parentId) {
//    return isChild$(childId, parentId);
	  return false;
  }

  @Override
  public boolean isAndChild(String childId, String parentId) {
    if (childId.equals(parentId) || isChild(childId, parentId))
      return true;
    return false;
  }

  @Override
  public boolean isParent(String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isAndParent(String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isChildToParent(String id, String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isAndChildToParent(String id, String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isChildToAndParent(String id, String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isAndChildToAndParent(String id, String childId, String parentId) {
    // TODO Auto-generated method stub
    return false;
  }

//  private boolean isChild$(String childId, String parentId) {
//    boolean isChild = false;
//    for (OrganizationRelation organizationRelation : organizationRelationRepository
//        .findByIdParentId(parentId)) {
//      if (organizationRelation.getId().getOrganizationId().equals(parentId)) {
//        return true;
//      } else {
//        isChild = isChild || isChild$(childId, organizationRelation.getId().getOrganizationId());
//      }
//    }
//    return isChild;
//  }

}
