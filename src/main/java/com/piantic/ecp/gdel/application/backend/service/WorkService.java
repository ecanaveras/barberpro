package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WorkService {

    public static final Logger LOGGER = Logger.getLogger(WorkService.class.getName());

    private WorkRepository workRepository;

    public WorkService(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public List<Work> findAll(String filter) {
        if (filter == null || filter.isEmpty()) {
            return workRepository.findAll();
        } else {
            return workRepository.search(filter);
        }
    }

    public List<Work> findAll() {
        return workRepository.findAll();
    }

    public List<Work> findWorkForProfile(Long profileId) {
        return workRepository.findWorksForProfile(profileId);
    }

    public long count() {
        return workRepository.count();
    }

    public void delete(Work work) {
        workRepository.delete(work);
    }

    public void save(Work work) {
        if (work == null) {
            LOGGER.log(Level.SEVERE, "Servicio es nulo, asegurese que los datos sean correctos");
            return;
        }
        workRepository.save(work);
    }

    public List<Work> findWorksNotAssignedToRoleId(long roleId) {
        return workRepository.findWorksNotAssignedToRoleId(roleId);
    }
/*
    @PostConstruct
    public void populateTestData() {
        if (workRepository.count() == 0) {
            //Random r = new Random(0);
            //List<Work> works = workRepository.findAll();
            workRepository.saveAll(Stream.of("Corte de Cabello", "Barba", "Cejas", "Manicure", "Pelicure", "Propina", "Bebida")
                    .map(name -> {
                        Work work = new Work();
                        work.setTitle(name);
                        if (name.equals("Corte de Cabello"))
                            work.setImage(LineAwesomeIcon.CUT_SOLID.getSvgName());
                        if (name.equals("Barba"))
                            work.setImage(LineAwesomeIcon.AFFILIATETHEME.getSvgName());
                        if (name.equals("Cejas"))
                            work.setImage(LineAwesomeIcon.GLASSES_SOLID.getSvgName());
                        work.setPrice(new Random().nextDouble(20000));
                        work.setDescription("Lorem ipsum dolor sit amet");
                        work.setObservations("Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet");
                        work.setCommissions(new Random().nextDouble(0.10));
                        return work;
                    }).collect(Collectors.toList()));
        }
    }

 */
}
