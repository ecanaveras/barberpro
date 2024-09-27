package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.repository.TenandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenandService {

    private TenandRepository tenandRepository;

    public TenandService(TenandRepository tenandRepository) {
        this.tenandRepository = tenandRepository;
    }

    public List<Tenant> findAll() {
        return tenandRepository.findAll();
    }

    public Tenant findTenantByEmail(String email) {
        return tenandRepository.findTenantByEmailAndEnabledTrue(email);
    }

    public void save(Tenant tenand) {
        tenandRepository.save(tenand);
    }

}
