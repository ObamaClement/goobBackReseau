package com.example.entity;

public enum CollaborationStatus {
    PENDING,    // En attente d'approbation (par l'éditeur)
    ACTIVE,     // Collaboration acceptée et active
    REJECTED,   // Demande refusée
    TERMINATED  // Collaboration terminée (par l'une ou l'autre partie)
}