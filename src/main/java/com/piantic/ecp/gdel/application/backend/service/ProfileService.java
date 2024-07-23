package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProfileService {
    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());
    private ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public List<Profile> findAll(){
        return profileRepository.findAll();
    }

    public List<Profile> findAll(String textfilter) {
        if(textfilter == null || textfilter.isEmpty()) {
            return profileRepository.findAll();
        }else{
            return profileRepository.search(textfilter);
        }
    }

    public long count(){
        return profileRepository.count();
    }

    public void delete(Profile profile){
        profileRepository.delete(profile);
    }

    public void save(Profile profile){
        if(profile == null){
            LOGGER.log(Level.SEVERE, "Perfil es nulo, asegurese que los datos sean correctos");
            return;
        }
        profileRepository.save(profile);
    }


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

}
