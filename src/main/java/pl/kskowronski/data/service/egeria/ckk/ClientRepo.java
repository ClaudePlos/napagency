package pl.kskowronski.data.service.egeria.ckk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import java.util.Optional;

public interface ClientRepo  extends JpaRepository<Client, Integer> {

    // @Query("SELECT c FROM Client c WHERE upper(c.kldNazwa) like upper(:likeFilter) and c.klKod in "

    Optional<Client> getClientByKlKod(Integer klKod);

    @Query(value = "SELECT * from napwfv_kontrahenci " +
            "where upper(kld_nazwa) like upper('%'||:likeFilter||'%') " +
            "and kl_kod in (select ck_kl_kod from CKK_CECHY_KLIENTOW where ck_ce_id = 100722)", nativeQuery = true)
    Page<Client> findAllWithPagination(String likeFilter, Pageable pageable);

    Client findByKlKod( Integer klKod);
}
