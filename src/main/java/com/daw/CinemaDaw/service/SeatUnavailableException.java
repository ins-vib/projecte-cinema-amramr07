package com.daw.CinemaDaw.service;

import java.util.List;

public class SeatUnavailableException extends RuntimeException {

    private final List<String> conflicts;

    public SeatUnavailableException(List<String> conflicts) {
        super("Some seats are no longer available");
        this.conflicts = conflicts;
    }

    public List<String> getConflicts() {
        return conflicts;
    }
}
