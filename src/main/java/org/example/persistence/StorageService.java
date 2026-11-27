package org.example.persistence;

import org.example.exception.KonzervGyarException;
import org.example.service.GyarManager;

public interface StorageService {

    void ment(GyarManager gyarManager, String fajlUtvonal) throws KonzervGyarException;

    GyarManager betolt(String fajlUtvonal) throws KonzervGyarException;
}