package com.example.repository;

import com.example.entity.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, UUID> {

    /**
     * Trouve toutes les collaborations pour un éditeur donné.
     * @param editorId L'ID de l'éditeur.
     * @return La liste de ses collaborations.
     */
    List<Collaboration> findByEditorId(UUID editorId);

    /**
     * Trouve toutes les collaborations pour un affilié donné.
     * @param affiliateId L'ID de l'affilié.
     * @return La liste de ses collaborations.
     */
    List<Collaboration> findByAffiliateId(UUID affiliateId);
}