package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Role;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProfileService {

    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());

    @Autowired
    private ProductService productService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ProfileRepository profileRepository;

    public ProfileService(){
    }

    public List<Profile> findAll() {
        return profileRepository.findByTenantAndStatusAndEnabledTrue(Application.getTenant(), Profile.Status.Activo);
    }

    public List<Profile> findAll(String textfilter) {
        if (textfilter == null || textfilter.isEmpty()) {
            return profileRepository.findByTenantAndEnabledTrue(Application.getTenant());
        } else {
            return profileRepository.search(Application.getTenant(), textfilter);
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

    public List<Profile> findByTenant() {
        return profileRepository.findByTenantAndEnabledTrue(Application.getTenant());
    }

    public List<Profile> findProfilesActives() {
        return profileRepository.findByTenantAndStatusAndEnabledTrue(Application.getTenant(), Profile.Status.Activo);
    }

    public List<Profile> findProfileByProduct(Product product) {
        return profileRepository.findProfilesByProduct(Application.getTenant(), product);
    }

    public Set<Role> getRolesByProfileId(Long roleId) {
        return profileRepository.findByIdWithProfile(Application.getTenant(), roleId)
                .map(Profile::getRoles)
                .orElse(Collections.emptySet());
    }

    public List<Profile> getProfileByRoleId(Long roleId) {
        return profileRepository.findByRoleId(Application.getTenant(), roleId);
    }

    public List<Product> getProductsByProfileId(Profile profile){
        return productService.findProductsByProfile(Application.getTenant(), profile);
    }

    public List<Appointment> getTenLastAppoimentsByProfileId(Profile profile){
        // Obtener el mes y año actual
        YearMonth currentMonth = YearMonth.now();

        // Fecha de inicio (primer día del mes a las 00:00:00)
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();

        // Fecha de fin (último día del mes a las 23:59:59)
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return appointmentService.findLastTenAppointmentsOfCurrentMonth(profile, startOfMonth, endOfMonth);

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
