package cn.sparrowmini.pem.service.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.pem.model.Sysrole;

public interface SysroleRepository extends SparrowJpaRepository<Sysrole, String> {

	Page<Sysrole> findByNameContaining(String name, Pageable p);

	List<Sysrole> findByCode(String name);

	@Transactional
	void deleteByIdIn(@NotNull String[] ids);
}
