package com.facade.hotel.dto;

import com.facade.hotel.domain.ExtraServiceType;
import jakarta.validation.constraints.NotNull;

/**
 * Incoming payload to add an additional service to an existing reservation.
 */
public class AddServiceRequest {

    @NotNull(message = "El tipo de servicio es obligatorio")
    private ExtraServiceType type;

    public ExtraServiceType getType() {
        return type;
    }

    public void setType(ExtraServiceType type) {
        this.type = type;
    }
}
