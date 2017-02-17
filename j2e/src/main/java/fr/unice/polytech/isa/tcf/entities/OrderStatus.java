package fr.unice.polytech.isa.tcf.entities;

import java.util.Optional;

public enum OrderStatus {

	CREATED, VALIDATED, IN_PROGRESS, READY;

    public static Optional<OrderStatus> next(OrderStatus status) {
        Optional<OrderStatus> result = Optional.empty();
        switch (status) {
            case CREATED:
                result = Optional.of(VALIDATED);
                break;
            case VALIDATED:
                result = Optional.of(IN_PROGRESS);
                break;
            case IN_PROGRESS:
                result =  Optional.of(READY);
                break;
            default:
        }
        return result;
    }

}
