package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.HarmIndividual;

import java.util.List;

@Service
public class HarmIndividualService extends CrudService<HarmIndividual, Integer> {

    private HarmIndividualRepo repo;

    public HarmIndividualService(@Autowired HarmIndividualRepo repo) {
        this.repo = repo;
    }

    @Override
    protected HarmIndividualRepo getRepository() {
        return repo;
    }




    public List<HarmIndividual> getHarmForWorker(Integer prcId, String period){
       return repo.getHarmForWorker(prcId, period);
    }

}
