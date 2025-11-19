package com.example.demo.service;

import com.example.demo.model.Stress;
import com.example.demo.model.User;
import com.example.demo.repository.StressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class StressService {

    private final StressRepository stressRepository;

    @Autowired
    public StressService(StressRepository stressRepository) {
        this.stressRepository = stressRepository;
    }

    public Stress createStress(Stress stress) {
        return stressRepository.save(stress);
    }

    public List<Stress> getAllStress() {
        return stressRepository.findAll();
    }

    public Stress getStress(Long id) {
        return stressRepository.findById(id).orElse(null);
    }

    // User-specific methods
    public List<Stress> getStressByUser(User user) {
        return stressRepository.findByUserOrderByDateDesc(user);
    }

    public List<Stress> getStressByUserAndDateRange(User user, LocalDate start, LocalDate end) {
        return stressRepository.findByUserAndDateBetween(user, start, end);
    }

    public Double getAverageStressLevel(User user, LocalDate start, LocalDate end) {
        return stressRepository.getAverageStressLevel(user, start, end);
    }

    public Stress updateStress(Stress stress) {
        return stressRepository.save(stress);
    }

    public void deleteStress(Long id) {
        stressRepository.deleteById(id);
    }
}