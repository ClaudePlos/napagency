package pl.kskowronski.views.agency;


import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;
import pl.kskowronski.data.service.egeria.ek.graphics.HarmIndividualService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayRepo;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayService;
import pl.kskowronski.views.componets.PeriodLayout;

import java.util.List;

@Component
@UIScope
@Route("grid-styling")
public class WorkCardView extends Dialog {

    private HarmIndividualService harmIndividualService;
    private HoursInDayService hoursInDayService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<HarmIndividual> grid = new Grid<>(HarmIndividual.class, false);
    private Grid<HoursInDay> gridHoursInDay = new Grid<>(HoursInDay.class, false);

    private Pracownik worker;

    private Label labNameWorker = new Label("");

    public WorkCardView(HarmIndividualService harmIndividualService, HoursInDayService hoursInDayService) {
        this.harmIndividualService = harmIndividualService;
        this.hoursInDayService = hoursInDayService;
        this.setWidth("1200px");
        this.setHeight("750px");
        grid.setWidthFull();
        grid.setHeight("690px");
        gridHoursInDay.setWidthFull();
        gridHoursInDay.setHeight("390px");

        grid.addColumn(HarmIndividual::getHiType).setHeader("").setWidth("25px");
        grid.addColumn(HarmIndividual::getDay).setHeader("D").setWidth("25px");
        grid.addColumn(HarmIndividual::getHiNameHarm).setHeader("Zmiana").setWidth("35px");
        grid.addColumn(HarmIndividual::getHiHoursPlan).setHeader("Plan").setWidth("35px");
        grid.addColumn(HarmIndividual::getHiHoursOverworked).setHeader("Wykonanie");
        grid.addColumn(HarmIndividual::getAbsenceName).setHeader("");

        grid.setClassNameGenerator(hi -> {
            if (hi.getHiType().equals("WS")) // from css
                return "ws";
            if (hi.getHiType().equals("SW"))
                return "sw";
            return null;
        });

        grid.addItemClickListener( e -> {
            getHoursForDay( e.getItem().getHiId() );
        });


        gridHoursInDay.addColumn(HoursInDay::gethFrom).setHeader("Od").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::gethTo).setHeader("Do").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::getGwdRgCode).setHeader("Kod").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::getRgName).setHeader("Nazwa").setWidth("200px");

        periodText.addClickListener( e -> {
            getHarmForWorker();
        });

        add(new HorizontalLayout(periodText, labNameWorker), new HorizontalLayout(grid, gridHoursInDay));
    }

    public void openView(Pracownik worker) {
        this.worker = worker;
        this.labNameWorker.setText(worker.getNazwImie());
        getHarmForWorker();
        this.open();
    }

    private void getHarmForWorker() {
       List<HarmIndividual> harm =  harmIndividualService.getHarmForWorker(worker.getPrcId(), periodText.getPeriod() );
        grid.setItems(harm);
    }

    private void getHoursForDay( Integer hiId) {
        var hours = hoursInDayService.findAllByHiId( hiId );
        gridHoursInDay.setItems(hours);
    }

}
