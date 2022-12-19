package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.SkladnikCzasowy;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInMonth;

import java.time.LocalDate;
import java.util.List;

@Service
public class ComponentsInMonthService extends CrudService<SkladnikCzasowy, Integer> {

    private ComponentsInMonthRepo repo;

    public ComponentsInMonthService(@Autowired ComponentsInMonthRepo repo) {
        this.repo = repo;
    }

    @Override
    protected ComponentsInMonthRepo getRepository() {
        return repo;
    }

    public List<SkladnikCzasowy> findAllForPeriodAndPrcId(Integer prcId, LocalDate dateFrom, LocalDate dateTo) {
        var components = repo.findAllForPrcIdAndPeriod( prcId, dateFrom,  dateTo).get();
        components.stream().forEach( item -> {
            if (item.getSkczDskId().equals(100542)) {
                item.setComponentName( "Nagroda uznaniowa" ); //TODO: get name form table dsk_nazwa
            } else {
                item.setComponentName( item.getSkczDskId().toString() );
            }
        });
        return components;
    }


}
