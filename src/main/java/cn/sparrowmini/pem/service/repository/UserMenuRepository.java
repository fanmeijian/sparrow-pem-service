package cn.sparrowmini.pem.service.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.pem.model.UserMenu;
import cn.sparrowmini.pem.model.UserMenu.UserMenuPK;

public interface UserMenuRepository extends JpaRepository<UserMenu, UserMenuPK> {

  @Transactional
  void deleteByIdUsernameAndIdMenuIdIn(String username, List<String> menuIds);

  Set<UserMenu> findByIdUsername(String username);

  @Transactional
  void deleteByIdIn(List<UserMenuPK> ids);

  Iterable<UserMenu> findByIdMenuId(String menuId);
}
