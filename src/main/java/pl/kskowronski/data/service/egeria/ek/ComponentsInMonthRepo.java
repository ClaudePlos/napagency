package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.SkladnikCzasowy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ComponentsInMonthRepo  extends JpaRepository<SkladnikCzasowy, Integer> {

    @Query("select s from SkladnikCzasowy s where s.skczPrcId = :prcId and s.skczDataOd >= :dateFrom and s.skczDataDo <= :dateTo order by s.skczId")
    Optional<List<SkladnikCzasowy>> findAllForPrcIdAndPeriod(@Param("prcId") Integer prcId, @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);

}
