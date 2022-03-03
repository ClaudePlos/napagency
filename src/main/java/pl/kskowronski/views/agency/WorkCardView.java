package pl.kskowronski.views.agency;


import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import pl.kskowronski.data.entity.egeria.ek.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.service.egeria.ek.HarmIndividualService;
import pl.kskowronski.views.componets.PeriodLayout;

import java.util.List;

@Component
@UIScope
public class WorkCardView extends Dialog {

    private HarmIndividualService harmIndividualService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<HarmIndividual> grid = new Grid<>(HarmIndividual.class, false);

    public WorkCardView(HarmIndividualService harmIndividualService) {
        this.harmIndividualService = harmIndividualService;

        add(new HorizontalLayout(periodText), grid);
    }

    public void openView(Pracownik worker) {
        getHarmForWorker(worker);
        this.open();
    }

    private void getHarmForWorker(Pracownik worker) {
       List<HarmIndividual> harm =  harmIndividualService.getHarmForWorker(worker.getPrcId(), periodText.getPeriod() );
        System.out.println(harm);
    }

}
