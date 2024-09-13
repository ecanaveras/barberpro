package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService {

    public static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(Tenant tenantId, String filter) {
        if (filter == null || filter.isEmpty()) {
            return productRepository.findAllByTenant(tenantId);
        } else {
            return productRepository.searchByTenant(tenantId, filter);
        }
    }

    public List<Product> findAll(Tenant tenantId) {
        return productRepository.findAllByTenant(tenantId);
    }

    public List<Product> findProductsByProfile(Tenant tenant, Profile profile) {
        return productRepository.findProductsByProfile(tenant, profile);
    }

    public long count() {
        return productRepository.count();
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public void save(Product product) {
        if (product == null) {
            LOGGER.log(Level.SEVERE, "Servicio es nulo, asegurese que los datos sean correctos");
            return;
        }
        productRepository.save(product);
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
