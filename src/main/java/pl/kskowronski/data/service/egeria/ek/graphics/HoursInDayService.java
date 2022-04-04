package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;

import java.util.List;

@Service
public class HoursInDayService extends CrudService<HoursInDay, Integer> {

    private HoursInDayRepo repo;
    private TypeOfHoursRepo typeOfHoursRepo;

    public HoursInDayService(@Autowired HoursInDayRepo repo, TypeOfHoursRepo typeOfHoursRepo) {
        this.repo = repo;
        this.typeOfHoursRepo = typeOfHoursRepo;
    }

    @Override
    protected HoursInDayRepo getRepository() {
        return repo;
    }


    public List<HoursInDay> findAllByHiId(Integer hiId) {
        var hours = repo.findAllByHiId( hiId ).get();
        hours.stream().forEach( item -> {
            item.setRgName( typeOfHoursRepo.findById( item.getGwdRgCode() ).get().getRgName() );
        });
        return hours;
    }

    public List<HoursInDay> getMainHours(Integer hiId) {
        var hours = repo.getMainHours( hiId ).get();
        hours.stream().forEach( item -> {
            item.setRgName( typeOfHoursRepo.findById( item.getGwdRgCode() ).get().getRgName() );
        });
        return hours;
    }


}
