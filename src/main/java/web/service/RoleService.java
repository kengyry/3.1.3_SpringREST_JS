package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.Model.Role;
import web.Model.User;
import web.Repositories.RoleRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Set<Role> findAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Transactional
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
