package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;

import java.util.List;
import java.util.Optional;


public interface HoursInDayRepo extends JpaRepository<HoursInDay, Integer> {

    @Query("select h from HoursInDay h where h.gwdHiId = :gwdHiId order by h.hFrom")
    Optional<List<HoursInDay>> findAllByHiId(@Param("gwdHiId") Integer gwdHiId);

}
