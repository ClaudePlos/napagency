package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInMonth;

import java.util.List;

@Service
public class HoursInMonthService extends CrudService<HoursInMonth, Integer> {

    private HoursInMonthRepo repo;
    private TypeOfHoursRepo typeOfHoursRepo;

    public HoursInMonthService(@Autowired HoursInMonthRepo repo, TypeOfHoursRepo typeOfHoursRepo) {
        this.repo = repo;
        this.typeOfHoursRepo = typeOfHoursRepo;
    }

    @Override
    protected HoursInMonthRepo getRepository() {
        return repo;
    }


    public List<HoursInMonth> findAllForPeriodAndPrcId(String period, Integer prcId) {
        var hours = repo.findAllForPrcIdAndPeriod( prcId, period ).get();
        hours.stream().forEach( item -> {
            item.setRgName( typeOfHoursRepo.findById( item.getGwmRgCode() ).get().getRgName() );
        });
        return hours;
    }

}
