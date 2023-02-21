package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.SysroleModel;
import cn.sparrowmini.pem.model.SysroleModel.SysroleModelId;

public interface SysroleModelRepository extends JpaRepository<SysroleModel, SysroleModelId> {
	Page<SysroleModel> findByIdModelId(String modelId, Pageable pageable);

	Page<SysroleModel> findByIdModelIdAndIdSysroleId(String modelId, String sysroleId, Pageable pageable);
}
