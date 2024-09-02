package pl.kskowronski.data.service.egeria.ek.graphics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HarmIndividualService extends CrudService<HarmIndividual, Integer> {

    private HarmIndividualRepo repo;
    private TypeOfAbsenceRepo typeOfAbsenceRepo;
    private HoursInDayService hoursInDayService;

    private List<HarmIndividual> harm = new ArrayList<>();
    private List<HarmIndividual> harms = new ArrayList<>();

    public HarmIndividualService(@Autowired HarmIndividualRepo repo, TypeOfAbsenceRepo typeOfAbsenceRepo, HoursInDayService hoursInDayService) {
        this.repo = repo;
        this.typeOfAbsenceRepo = typeOfAbsenceRepo;
        this.hoursInDayService = hoursInDayService;
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
           List<HoursInDay> hdList = hoursInDayService.getMainHours(item.getHiId());
           if (hdList.size() > 0) {
               item.setHhFrom( hdList.stream().min( Comparator.comparing(HoursInDay::gethFrom) ).get().gethFrom().toString().substring(0,5) );
               item.setHhTo( hdList.stream().max( Comparator.comparing(HoursInDay::gethTo) ).get().gethTo().toString().substring(0,5) );
           }
       });
       return harm;
    }

    public void getHarmForWorkers(List<Pracownik> workers, String periodText){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        final String[] skList = {""};
        harms.clear();

        workers.forEach( w -> {
            skList[0] = "";
            w.getZatrudnienia().forEach( z -> {
                skList[0] += z.getSkKod() + " ";
            });
            getHarmForWorker(w.getPrcId(), periodText).forEach( h -> {
                h.setPrcNumer(w.getPrcNumer());
                h.setPrcImie(w.getPrcImie());
                h.setPrcNazwisko(w.getPrcNazwisko());
                h.setSkKod(skList[0]);
                h.setHiDateS(sdf.format(h.getHiDate()));
                harms.add(h);
            });
        });
    }

    public List<? extends HarmIndividual> getHarmForWorker() {
        return harm;
    }

    public List<? extends HarmIndividual> getHarmForWorkers() {
        return harms;
    }
}
