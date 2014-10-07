package net.vikev.android.plates.services;

import java.util.List;

import net.vikev.android.plates.entities.Scale;

public interface ScalesService {
    List<Scale> getAllScales();
    Scale getScale(String id);
}
