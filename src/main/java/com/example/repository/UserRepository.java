package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Spring Data JPA va créer automatiquement les requêtes pour ces méthodes.

    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     * @param username Le nom d'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur s'il est trouvé.
     */
    Optional<User> findByUsername(String username);

    /**
     * Trouve un utilisateur par son adresse e-mail.
     * @param email L'email à rechercher.
     * @return Un Optional contenant l'utilisateur s'il est trouvé.
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec ce nom d'utilisateur.
     * @param username Le nom d'utilisateur à vérifier.
     * @return true si l'utilisateur existe, false sinon.
     */
    Boolean existsByUsername(String username);

    /**
     * Vérifie si un utilisateur existe avec cet e-mail.
     * @param email L'email à vérifier.
     * @return true si l'utilisateur existe, false sinon.
     */
    Boolean existsByEmail(String email);
}