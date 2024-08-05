package com.idms.base.service;

import java.util.List;
import java.util.Optional;

import com.idms.base.api.v1.model.dto.ModuleDto;
import com.idms.base.dao.entity.Role;
import com.idms.base.dao.entity.User;

public interface UserService {
	
	List<Role> getAllRoles();

	List<User> getUsers(String depotCode, List<Integer> ids);

	Optional<User> findByUserName(String name);

	User saveUser(User user);

	Optional<Role> getRoleById(Integer id);

	void deleteUser(User user);

	List<ModuleDto> getAllModuleByRole(Integer roleId);

	Role getRoleByCode(String roleCode);

	List<Integer> getTpIds(String[] groupIds);

}
