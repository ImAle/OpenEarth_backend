package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.Rent;
import com.alejandro.OpenEarth.repository.RentRepository;
import com.alejandro.OpenEarth.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("rentService")
public class RentServiceImpl implements RentService {

    @Autowired
    @Qualifier("rentRepository")
    private RentRepository rentRepository;

    @Override
    public Rent addRent(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public Rent getRentById(long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        if(rent.isEmpty())
            throw new RuntimeException("Rent not found");

        return rent.get();
    }

    @Override
    public List<Rent> getRents() {
        return rentRepository.findAll();
    }

    @Override
    public Rent updateRent(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public void deleteRentById(long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        if(rent.isEmpty())
            throw new RuntimeException("Rent not found");

        rentRepository.deleteById(id);
    }
}
