package pl.kskowronski.views.agency;


import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInMonth;
import pl.kskowronski.data.service.egeria.ek.graphics.HarmIndividualService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInMonthService;
import pl.kskowronski.views.componets.PeriodLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@UIScope
@Route("grid-styling")
public class WorkCardView extends Dialog {

    private HarmIndividualService harmIndividualService;
    private HoursInDayService hoursInDayService;
    private HoursInMonthService hoursInMonthService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<HarmIndividual> grid = new Grid<>(HarmIndividual.class, false);
    private Grid<HoursInDay> gridHoursInDay = new Grid<>(HoursInDay.class, false);
    private Grid<HoursInMonth> gridHoursInMonth = new Grid<>(HoursInMonth.class, false);

    private Pracownik worker;

    private Label labNameWorker = new Label("");
    private Button butClosePopUp = new Button("X");
    private List<HarmIndividual> harm = new ArrayList<>();
    private HorizontalLayout hAnchor = new HorizontalLayout();

    public WorkCardView(HarmIndividualService harmIndividualService, HoursInDayService hoursInDayService, HoursInMonthService hoursInMonthService) {
        this.harmIndividualService = harmIndividualService;
        this.hoursInDayService = hoursInDayService;
        this.hoursInMonthService = hoursInMonthService;
        this.setWidth("1200px");
        this.setHeight("750px");
        grid.setWidthFull();
        grid.setHeight("680px");
        gridHoursInDay.setWidthFull();
        gridHoursInDay.setHeight("370px");
        gridHoursInMonth.setWidthFull();
        gridHoursInMonth.setHeight("290px");

        grid.addColumn(HarmIndividual::getHiType).setHeader("").setWidth("25px");
        grid.addColumn(HarmIndividual::getDay).setHeader("D").setWidth("25px");
        grid.addColumn(HarmIndividual::getHiNameHarm).setHeader("Zmiana").setWidth("35px");
        grid.addColumn(HarmIndividual::getHiHoursPlan).setHeader("Plan").setWidth("35px");
        grid.addColumn(HarmIndividual::getHiHoursOverworked).setHeader("Wykonanie");
        grid.addColumn(HarmIndividual::getAbsenceName).setHeader("");

        grid.setClassNameGenerator(hi -> {
            if ( hi.getHiType() != null ){
                if (hi.getHiType().equals("WS")) // from css
                    return "ws";
                if (hi.getHiType().equals("SW"))
                    return "sw";
                return null;
            }
            return null;
        });

        grid.addItemClickListener( e -> {
            getHoursForDay( e.getItem().getHiId() );
        });


        gridHoursInDay.addColumn(HoursInDay::gethFrom).setHeader("Od").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::gethTo).setHeader("Do").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::getGwdRgCode).setHeader("Kod").setWidth("35px");
        gridHoursInDay.addColumn(HoursInDay::getRgName).setHeader("Nazwa").setWidth("200px");

        gridHoursInMonth.addColumn(HoursInMonth::getGwmRgCode).setHeader("Kod").setWidth("35px");
        gridHoursInMonth.addColumn(HoursInMonth::getRgName).setHeader("Nazwa").setWidth("150px");
        gridHoursInMonth.addColumn(HoursInMonth::getGwmNumberOfHours).setHeader("Godz. Miesiąc").setWidth("35px");
        gridHoursInMonth.addColumn(HoursInMonth::getGwmAdditionalHours).setHeader("Godz. Dod.").setWidth("35px");

        periodText.addClickListener( e -> {
            getHarmForWorker();
        });

        var hClose = new HorizontalLayout( butClosePopUp );
        //hClose.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        //hClose.setAlignSelf(FlexComponent.Alignment.END);
        hClose.getElement().getStyle().set("position","absolute");
        hClose.getElement().getStyle().set("margin-left", "1090px");

        hClose.setClassName("hClose");
        hClose.setWidth("100%");
        butClosePopUp.addClickListener( e -> {
            this.close();
        });
        butClosePopUp.setClassName("closePopUp");
        butClosePopUp.getStyle().set("margin-right", "auto");


        add(new HorizontalLayout(periodText, labNameWorker, hAnchor, hClose)
                , new HorizontalLayout(grid, new VerticalLayout(gridHoursInDay, gridHoursInMonth))
        );
    }

    public void openView(Pracownik worker) {
        this.worker = worker;
        this.labNameWorker.setText(worker.getNazwImie());
        getHarmForWorker();
        this.open();
    }

    private void getHarmForWorker() {
        hAnchor.removeAll();
        harm =  harmIndividualService.getHarmForWorker(worker.getPrcId(), periodText.getPeriod());
        grid.setItems(harm);
        var hoursInMonth = hoursInMonthService.findAllForPeriodAndPrcId(periodText.getPeriod(), worker.getPrcId());
        gridHoursInMonth.setItems(hoursInMonth);
        generatePDF();
    }

    private void generatePDF() {

        PrintPreviewReport<HarmIndividual> report = new PrintPreviewReport<>();
        report.setItems(harm);
        report.getReportBuilder()
                .addAutoText("+", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200)
                .setPrintBackgroundOnOddRows(true)
                .setTitle("Karta Pracy: " + labNameWorker.getText() + " " + periodText.getPeriod())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").build())
        ;
        StreamResource pdf = report.getStreamResource("karta.pdf", harmIndividualService::getHarmForWorker, PrintPreviewReport.Format.PDF);
        Anchor anchor = new Anchor(pdf, "PDF");
        anchor.setTarget("_blank");
        hAnchor.add(anchor);
    }

    private void getHoursForDay( Integer hiId) {
        var hours = hoursInDayService.findAllByHiId( hiId );
        gridHoursInDay.setItems(hours);
    }


}
