package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.SysroleModelAttribute;
import cn.sparrowmini.pem.model.SysroleModelAttribute.SysroleModelAttributeId;

public interface SysroleModelAttributeRepository extends JpaRepository<SysroleModelAttribute, SysroleModelAttributeId> {
	Page<SysroleModelAttribute> findByIdAttributeId(ModelAttributePK attributeId, Pageable pageable);

	Page<SysroleModelAttribute> findByIdAttributeIdAndIdSysroleId(ModelAttributePK attributeId, String sysroleId,
			Pageable pageable);
}
