package com.gearit.api.dto.view;

import com.gearit.api.entity.auto.Automobile;

public record AutomobileView(
        Long id,
        String factory,
        String model,
        String color,
        String license,
        String bodyType
) {

    public static AutomobileView from(Automobile automobile) {
        return new AutomobileView(
                automobile.getId(),
                automobile.getFactory().getName(),
                automobile.getModel().getName(),
                automobile.getColor(),
                automobile.getLicense(),
                automobile.getType().name()
        );

    }
}
