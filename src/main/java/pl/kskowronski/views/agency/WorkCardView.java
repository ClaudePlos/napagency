package pl.kskowronski.views.agency;


import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.egeria.ek.SkladnikCzasowy;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInDay;
import pl.kskowronski.data.entity.egeria.ek.graphics.HoursInMonth;
import pl.kskowronski.data.service.egeria.ek.ComponentsInMonthService;
import pl.kskowronski.data.service.egeria.ek.graphics.HarmIndividualService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInMonthService;
import pl.kskowronski.views.componets.PeriodLayout;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Component
@UIScope
@Route("grid-styling")
public class WorkCardView extends Dialog {

    private HarmIndividualService harmIndividualService;
    private HoursInDayService hoursInDayService;
    private HoursInMonthService hoursInMonthService;
    private ComponentsInMonthService componentsInMonthService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<HarmIndividual> grid = new Grid<>(HarmIndividual.class, false);
    private Grid<HoursInDay> gridHoursInDay = new Grid<>(HoursInDay.class, false);
    private Grid<HoursInMonth> gridHoursInMonth = new Grid<>(HoursInMonth.class, false);
    private Grid<SkladnikCzasowy> gridComponentsInMonth = new Grid<>(SkladnikCzasowy.class, false);

    private Pracownik worker;

    private Label labNameWorker = new Label("");
    private Button butClosePopUp = new Button("X");
    private List<HarmIndividual> harm = new ArrayList<>();
    private HorizontalLayout hAnchor = new HorizontalLayout();

    public WorkCardView(HarmIndividualService harmIndividualService, HoursInDayService hoursInDayService, HoursInMonthService hoursInMonthService, ComponentsInMonthService componentsInMonthService) {
        this.harmIndividualService = harmIndividualService;
        this.hoursInDayService = hoursInDayService;
        this.hoursInMonthService = hoursInMonthService;
        this.componentsInMonthService = componentsInMonthService;
        this.setWidth("1200px");
        this.setHeight("750px");
        grid.setWidthFull();
        grid.setHeight("680px");
        gridHoursInDay.setWidthFull();
        gridHoursInDay.setHeight("300px");
        gridHoursInMonth.setWidthFull();
        gridHoursInMonth.setHeight("200px");
        gridComponentsInMonth.setWidthFull();
        gridComponentsInMonth.setHeight("150px");

        grid.addColumn(HarmIndividual::getHiType).setHeader("").setWidth("25px");
        grid.addColumn(HarmIndividual::getDay).setHeader("D").setWidth("25px");
        grid.addColumn(HarmIndividual::getHiNameHarm).setHeader("Zmiana").setWidth("45px");
        grid.addColumn(HarmIndividual::getHiHoursPlan).setHeader("Plan").setWidth("30px");
        grid.addColumn(HarmIndividual::getHiHoursOverworked).setWidth("30px").setHeader("Wyk.");
        grid.addColumn(HarmIndividual::getAbsenceName).setHeader("");
        grid.addColumn(HarmIndividual::getHhFrom).setWidth("35px").setHeader("Od");
        grid.addColumn(HarmIndividual::getHhTo).setWidth("35px").setHeader("Do");

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

        Button butPDF = new Button("PDF");
        butPDF.addClickListener( e -> {
          WorkCardPdf repPdf = new WorkCardPdf();
            repPdf.openPopUp(harm);
        });




        gridComponentsInMonth.addColumn(SkladnikCzasowy::getComponentName).setHeader("Nazwa Składnika").setWidth("135px");
        gridComponentsInMonth.addColumn(SkladnikCzasowy::getSkczKwotaDod).setHeader("Kwota").setWidth("35px");
        gridComponentsInMonth.addColumn(SkladnikCzasowy::getSkczDataOd).setHeader("Od").setWidth("35px");
        gridComponentsInMonth.addColumn(SkladnikCzasowy::getSkczDataDo).setHeader("Do").setWidth("35px");

        add(new HorizontalLayout(periodText, labNameWorker, hAnchor//, butPDF
                        , hClose)
                , new HorizontalLayout(grid, new VerticalLayout(gridHoursInDay, gridHoursInMonth, gridComponentsInMonth))
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
        var componentsInMonth = componentsInMonthService.findAllForPeriodAndPrcId(worker.getPrcId()
                , periodText.getFirstDayOfPeriod()
                , periodText.getLastDayOfPeriod()
        );
        gridComponentsInMonth.setItems(componentsInMonth);
        generatePDF();
        generateExcel();
    }

    private void generatePDF() {
        Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
        Style groupStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM_BOLD).build();

        final String[] skList = {""};
        worker.getZatrudnienia().forEach( item -> {
            skList[0] += item.getSkKod() + " ";
        });

        PrintPreviewReport<HarmIndividual> report = new PrintPreviewReport<>();
        report.setItems(harm);
        report.getReportBuilder()
                .addAutoText("+", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, groupStyle)
                .setPrintBackgroundOnOddRows(true)
                .setReportLocale(new Locale("pl", "PL"))
                //.addAutoText("Karta żźćółśćą Pracy: " + labNameWorker.getText()+ " " + periodText.getPeriod() +  " ( MPK: " + skList[0] + ")", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
                .setTitle("Karta Pracy: " + translateToEn(labNameWorker.getText())+ " " + translateToEn(periodText.getPeriod()) +  " ( MPK: " + skList[0] + ")")
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").setStyle(headerStyle).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhFrom", String.class).setTitle("Od").setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhTo", String.class).setTitle("Do").setWidth(15).build())
        ;
        StreamResource pdf = report.getStreamResource("karta_" + translateToEn(labNameWorker.getText()) + "_"+translateToEn(periodText.getPeriod()) +".pdf"
                , harmIndividualService::getHarmForWorker, PrintPreviewReport.Format.PDF);
        Anchor anchor = new Anchor(pdf, "PDF");
        anchor.setTarget("_blank");
        hAnchor.add(anchor);
    }

    private void generateExcel() {
        Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
        Style groupStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM_BOLD).build();

        final String[] skList = {""};
        worker.getZatrudnienia().forEach( item -> {
            skList[0] += item.getSkKod() + " ";
        });

        PrintPreviewReport<HarmIndividual> reportCsv = new PrintPreviewReport<>();
        reportCsv.setItems(harm);
        reportCsv.getReportBuilder()
                .setPrintBackgroundOnOddRows(true)
                .setReportLocale(new Locale("pl", "PL"))
                //.addAutoText("Karta żźćółśćą Pracy: " + labNameWorker.getText()+ " " + periodText.getPeriod() +  " ( MPK: " + skList[0] + ")", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
                .setTitle("Karta Pracy: " + translateToEn(labNameWorker.getText())+ " " + translateToEn(periodText.getPeriod()) +  " ( MPK: " + skList[0] + ")")
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").setStyle(headerStyle).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhFrom", String.class).setTitle("Od").setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhTo", String.class).setTitle("Do").setWidth(15).build())
        ;
        StreamResource csv = reportCsv.getStreamResource("karta_" + translateToEn(labNameWorker.getText()) + "_"+translateToEn(periodText.getPeriod()) +".csv"
        , harmIndividualService::getHarmForWorker, PrintPreviewReport.Format.CSV);
        Anchor anchorCsv = new Anchor(csv, "CSV");
        hAnchor.add(anchorCsv);
    }

    private void getHoursForDay( Integer hiId) {
        var hours = hoursInDayService.findAllByHiId( hiId );
        gridHoursInDay.setItems(hours);
    }


    private String translateToEn(String text) {
        text = text.replace("ą", "a");
        text = text.replace("ć", "c");
        text = text.replace("ę", "e");
        text = text.replace("ł", "l");
        text = text.replace("ń", "n");
        text = text.replace("ó", "o");
        text = text.replace("ś", "s");
        text = text.replace("ź", "z");
        text = text.replace("ż", "z");
        text = text.replace("Ą", "A");
        text = text.replace("Ć", "C");
        text = text.replace("Ę", "E");
        text = text.replace("Ł", "L");
        text = text.replace("Ń", "N");
        text = text.replace("Ó", "O");
        text = text.replace("Ś", "S");
        text = text.replace("Ź", "Z");
        text = text.replace("Ż", "Z");
        return text;
    }

}
