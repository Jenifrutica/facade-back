package com.facade.hotel.subsystem;

import com.facade.hotel.domain.ExtraService;
import com.facade.hotel.domain.ExtraServiceType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Subsystem 3 - Additional services catalog (ServicioAdicionalService).
 * Knows which extras exist and how to build them for a given number of nights.
 */
@Service
public class ExtraServiceService {

    public List<ExtraServiceType> catalog() {
        return Arrays.asList(ExtraServiceType.values());
    }

    public ExtraService build(ExtraServiceType type, int nights) {
        return new ExtraService(type, nights);
    }
}
