package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Tenand;
import com.piantic.ecp.gdel.application.backend.repository.TenandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenandService {

    private TenandRepository tenandRepository;

    public TenandService(TenandRepository tenandRepository) {
        this.tenandRepository = tenandRepository;
    }

    public List<Tenand> findAll() {
        return tenandRepository.findAll();
    }

    public void save(Tenand tenand) {
        tenandRepository.save(tenand);
    }

}
