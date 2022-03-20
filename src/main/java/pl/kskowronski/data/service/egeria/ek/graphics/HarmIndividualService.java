package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;

import java.util.ArrayList;
import java.util.List;

@Service
public class HarmIndividualService extends CrudService<HarmIndividual, Integer> {

    private HarmIndividualRepo repo;
    private TypeOfAbsenceRepo typeOfAbsenceRepo;

    private List<HarmIndividual> harm = new ArrayList<>();

    public HarmIndividualService(@Autowired HarmIndividualRepo repo, TypeOfAbsenceRepo typeOfAbsenceRepo) {
        this.repo = repo;
        this.typeOfAbsenceRepo = typeOfAbsenceRepo;
    }

    @Override
    protected HarmIndividualRepo getRepository() {
        return repo;
    }



    public List<HarmIndividual> getHarmForWorker(Integer prcId, String period){
       harm = repo.getHarmForWorker(prcId, period);
       harm.stream().forEach( item -> {
           item.setAbsenceName( item.getHiRdaId() != null ? typeOfAbsenceRepo.findById( item.getHiRdaId() ).get().getRdaName() : null  );
           item.setDay( (item.getHiDate().getDate()) + "");
       });
       return harm;
    }

    public List<? extends HarmIndividual> getHarmForWorker() {

        return harm;
    }
}
