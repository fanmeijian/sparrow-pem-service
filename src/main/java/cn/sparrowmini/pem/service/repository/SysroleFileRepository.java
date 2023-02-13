package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.SysroleFile;
import cn.sparrowmini.pem.model.SysroleFile.SysroleFilePK;

public interface SysroleFileRepository extends JpaRepository<SysroleFile, SysroleFilePK> {

	Page<SysroleFile> findByIdFileId(String fileId, Pageable pageable);

}
