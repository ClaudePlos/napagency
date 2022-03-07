package pl.kskowronski.data.service.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.admin.AgencyForLogin;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;

import java.util.List;

public interface AgencyForLoginRepo extends JpaRepository<AgencyForLogin, Integer> {

    @Query(value = "select a from AgencyForLogin a where a.userId = :prcId order by a.agencyName")
    List<AgencyForLogin> findAllForUser(@Param("prcId") Integer prcId);

}
