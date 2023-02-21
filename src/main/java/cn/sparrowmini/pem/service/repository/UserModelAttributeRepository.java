package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.UserModelAttribute;
import cn.sparrowmini.pem.model.UserModelAttribute.UserModelAttributeId;

public interface UserModelAttributeRepository extends JpaRepository<UserModelAttribute, UserModelAttributeId> {
	Page<UserModelAttribute> findByIdAttributeId(ModelAttributePK attributeId, Pageable pageable);

	Page<UserModelAttribute> findByIdAttributeIdAndIdUsername(ModelAttributePK attributeId, String username,
			Pageable pageable);
}
