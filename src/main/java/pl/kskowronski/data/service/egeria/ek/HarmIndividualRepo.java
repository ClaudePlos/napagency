package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.HarmIndividual;

import java.util.List;

public interface HarmIndividualRepo extends JpaRepository<HarmIndividual, Integer> {


    @Query(value = "select h from HarmIndividual h where h.hiPrcId = :prcId and to_char(h.hiDay,'YYYY-MM') = :period")
    List<HarmIndividual> getHarmForWorker(@Param("prcId") Integer prcId, @Param("period") String period);

}
