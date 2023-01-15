package com.emented.weblab4.setup;

import com.emented.weblab4.model.Role;
import com.emented.weblab4.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SetupRoleLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    boolean alreadySetup = false;

    @Autowired
    public SetupRoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        Role adminRole = new Role();
        adminRole.setName("ADMIN_ROLE");

        Role directorRole = new Role();
        directorRole.setName("DIRECTOR_ROLE");

        Role userRole = new Role();
        userRole.setName("USER_ROLE");

        roleRepository.saveRole(adminRole);
        roleRepository.saveRole(directorRole);
        roleRepository.saveRole(userRole);

        alreadySetup = true;

    }
}
