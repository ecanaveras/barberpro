package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProfileService {

    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());

    private ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public List<Profile> findAll(String textfilter) {
        if (textfilter == null || textfilter.isEmpty()) {
            return profileRepository.findAll();
        } else {
            return profileRepository.search(textfilter);
        }
    }

    public long count() {
        return profileRepository.count();
    }

    public void delete(Profile profile) {
        profileRepository.delete(profile);
    }

    public void save(Profile profile) {
        if (profile == null) {
            LOGGER.log(Level.SEVERE, "Perfil es nulo, asegurese que los datos sean correctos");
            return;
        }
        profileRepository.save(profile);
    }

    public Profile findById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isPresent()) {
            return profile.get();
        }
        return null;
    }

    public List<Profile> findByTenant(Tenant tenant) {
        return profileRepository.findByTenant(tenant);
    }

    public List<Profile> findProfileByProduct(Product product) {
        return profileRepository.findProfilesByProduct(product);
    }

    public Set<Role> getRolesByProfileId(Long roleId) {
        return profileRepository.findByIdWithProfile(roleId)
                .map(Profile::getRoles)
                .orElse(Collections.emptySet());
    }

    public List<Profile> getProfileByRoleId(Long roleId) {
        return profileRepository.findByRoleId(roleId);
    }

/*
    @PostConstruct
    public void populateTestData(){
        if(profileRepository.count()==0){
            Random r = new Random(0);
            List<Profile> profiles = profileRepository.findAll();
            profileRepository.saveAll(Stream.of("Peluquero Master", "ToruMacto", "Wendypy").map(name -> {
              Profile profile = new Profile();
              profile.setNameProfile(name);
              profile.setPin(name.contains("o") ? 1234 : null);
              profile.setStatus(Profile.Status.values()[r.nextInt(Profile.Status.values().length)]);
              return profile;
            }).collect(Collectors.toList()));
        }
    }

 */

}
