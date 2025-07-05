package com.example.service;

import com.example.entity.User;
import org.springframework.stereotype.Service;

/**
 * Interface pour le service gérant la logique métier liée aux profils professionnels
 * sur l'API externe d'organisation (YOWYOB).
 */
@Service
public interface OrganizationService {

    /**
     * Crée le profil professionnel d'un nouvel Éditeur sur l'API d'organisation.
     * Cette méthode sera appelée après la création de l'identité de l'utilisateur.
     *
     * @param newUser L'entité User locale de l'éditeur qui vient d'être créé.
     * @param yowyobUserToken Le token JWT de l'utilisateur YOWYOB, nécessaire pour autoriser l'appel.
     */
    void createEditorProfileOnExternalApi(User newUser, String yowyobUserToken);


    /**
     * Crée le profil professionnel d'un nouvel Annonceur (Affilié) sur l'API d'organisation.
     * (Nous implémenterons cette méthode plus tard).
     *
     * @param newUser L'entité User locale de l'affilié qui vient d'être créé.
     * @param yowyobUserToken Le token JWT de l'utilisateur YOWYOB.
     */
    // void createAffiliateProfileOnExternalApi(User newUser, String yowyobUserToken);
    void createAffiliateProfileOnExternalApi(User newUser, String yowyobUserToken);
}