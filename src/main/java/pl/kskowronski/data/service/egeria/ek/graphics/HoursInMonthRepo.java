package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInMonth;

import java.util.List;
import java.util.Optional;

public interface HoursInMonthRepo extends JpaRepository<HoursInMonth, Integer> {

    @Query("select h from HoursInMonth h where h.gwmPrcId = :prcId and to_char(h.date,'YYYY-MM') = :period order by h.gwmRgCode")
    Optional<List<HoursInMonth>> findAllForPrcIdAndPeriod(@Param("prcId") Integer prcId, @Param("period") String period);

}
