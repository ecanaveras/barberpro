package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.repository.RoleRepository;
import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RoleService implements GenericService<Role> {

    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findByTenantAndEnabledTrue(Application.getTenant());
    }

    public List<Role> findAll(String filter) {
        if (filter == null || filter.isEmpty()) {
            return roleRepository.findByTenantAndEnabledTrue(Application.getTenant());
        } else {
            return roleRepository.search(Application.getTenant(), filter);
        }
    }

    public Role findById(Long id) {
        Optional<Role> object = roleRepository.findById(id);
        if (object.isPresent()) {
            return object.get();
        }
        return null;
    }

    public long count() {
        return roleRepository.count();
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public void save(Role role) {
        if (role == null) {
            LOGGER.log(Level.SEVERE, "Role es nulo, asegurese que los datos sean correctos");
            return;
        }
        roleRepository.save(role);
    }

    public Set<Profile> getProfilesByRoleId(Long roleId) {
        return roleRepository.findByIdWithProfile(Application.getTenant(), roleId)
                .map(Role::getProfiles)
                .orElse(Collections.emptySet());
    }


    @Override
    public Long getId(Role entity) {
        return entity.getId();
    }

}
