package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.UserFile;
import cn.sparrowmini.pem.model.relation.UserFile.UserFilePK;

public interface UserFileReposiroty extends JpaRepository<UserFile, UserFilePK> {

	Page<UserFile> findByIdFileId(String fileId, Pageable pageable);

}
