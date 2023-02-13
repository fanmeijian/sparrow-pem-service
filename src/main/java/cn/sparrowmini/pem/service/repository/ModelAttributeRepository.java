package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;

public interface ModelAttributeRepository extends JpaRepository<ModelAttribute, ModelAttributePK> {
  Long countById(ModelAttributePK id);
  
  @Transactional
  void deleteByIdIn(List<ModelAttributePK> ids);
}
