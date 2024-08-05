package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.ModuleDto;
import com.idms.base.dao.entity.Module;
import com.idms.base.dao.entity.Role;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.repository.ModuleRepository;
import com.idms.base.dao.repository.RoleModuleAccessRepository;
import com.idms.base.dao.repository.RoleRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.UserRepository;
import com.idms.base.service.UserService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ModuleRepository moduleRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TransportUnitRepository tpRepo;
	
	@Override
	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

	@Override
	public List<User> getUsers(String depotCode, List<Integer> ids) {
		if(depotCode!=null)
			return userRepo.getUsersByDepot(depotCode);
		else
			return userRepo.getUsers(ids);
	}

	@Override
	public Optional<User> findByUserName(String name) {
		return userRepo.findById(name);
	}

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public Optional<Role> getRoleById(Integer id) {
		return roleRepo.findById(id);
	}

	@Override
	public void deleteUser(User user) {
		userRepo.delete(user);		
	}
	
	@Override 
	public Role getRoleByCode(String roleCode) {
		return roleRepo.findByName(roleCode);
	}
	
	@Override
	public List<ModuleDto> getAllModuleByRole(Integer roleId) {
		List<Module> modules = new ArrayList<Module>();
		List<Module> childModules = new ArrayList<>();
		List<ModuleDto> dtoList = new ArrayList<>();
		try { 
			modules = moduleRepo.getParentModulesByRole(roleId);
			childModules = moduleRepo.getChildModulesByRole(roleId);
			for(Module p : modules) {
				for(Module c : childModules) {
					if(p.getId()==c.getParentId().getId()) {
						if(p.getChildModule()!=null) {
							p.getChildModule().add(c);
						} else {
							List<Module> cm = new ArrayList<>();
							cm.add(c);
							p.setChildModule(cm);
						}
					}
				}
				dtoList.add(this.mapper.map(p, ModuleDto.class));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dtoList;
	}

	@Override
	public List<Integer> getTpIds(String[] groupIds) {
		List<Integer> tps = tpRepo.findByGroupIds(groupIds);
		return tps;
	}

}
