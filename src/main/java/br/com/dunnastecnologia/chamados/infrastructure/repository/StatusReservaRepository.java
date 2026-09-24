package br.com.dunnastecnologia.chamados.infrastructure.repository;

import br.com.dunnastecnologia.chamados.domain.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusReservaRepository extends JpaRepository<StatusReserva, UUID> {


    /** Busca o status pelo nome de foma case-insensitive,
     *  mantendo padrão de arquitetura
     */

    @Query("""
            select s 
            from StatusReserva s
            where lower(s.nome) = lower(:nome)            
            """)
    Optional<StatusReserva> findByNome(@Param("nome") String nome);
}
