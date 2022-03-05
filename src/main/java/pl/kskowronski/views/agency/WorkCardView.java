package pl.kskowronski.views.agency;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import pl.kskowronski.data.entity.egeria.ek.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.service.egeria.ek.HarmIndividualService;
import pl.kskowronski.views.componets.PeriodLayout;

import java.util.List;

@Component
@UIScope
@Route("grid-styling")
public class WorkCardView extends Dialog {

    private HarmIndividualService harmIndividualService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<HarmIndividual> grid = new Grid<>(HarmIndividual.class, false);
    private Grid<HarmIndividual> gridDetail = new Grid<>(HarmIndividual.class, false);

    private Pracownik worker;

    public WorkCardView(HarmIndividualService harmIndividualService) {
        this.harmIndividualService = harmIndividualService;
        this.setWidth("1200px");
        this.setHeight("750px");
        grid.setWidthFull();
        grid.setHeight("690px");
        gridDetail.setWidthFull();
        gridDetail.setHeight("690px");

        grid.addColumn(HarmIndividual::getHiType).setHeader("").setWidth("25px");
        grid.addColumn(HarmIndividual::getDay).setHeader("D").setWidth("25px");
        grid.addColumn(HarmIndividual::getHiNameHarm).setHeader("Zmiana");
        grid.addColumn(HarmIndividual::getHiHoursPlan).setHeader("Plan");
        grid.addColumn(HarmIndividual::getHiHoursOverworked).setHeader("Wykonanie");
        grid.addColumn(HarmIndividual::getAbsenceName).setHeader("");


        grid.setClassNameGenerator(hi -> {
            if (hi.getHiType().equals("WS"))
                return "high-rating";
            if (hi.getHiType().equals("SW"))
                return "low-rating";
            return null;
        });


        periodText.addClickListener( e -> {
            getHarmForWorker();
        });

        add(new HorizontalLayout(periodText), new HorizontalLayout(grid, gridDetail));
    }

    public void openView(Pracownik worker) {
        this.worker = worker;
        getHarmForWorker();
        this.open();
    }

    private void getHarmForWorker() {
       List<HarmIndividual> harm =  harmIndividualService.getHarmForWorker(worker.getPrcId(), periodText.getPeriod() );
        grid.setItems(harm);
    }

}
