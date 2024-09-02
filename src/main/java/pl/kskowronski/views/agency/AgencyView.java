package pl.kskowronski.views.agency;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.ek.Pracownik;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;
import pl.kskowronski.data.service.admin.AgencyForLoginService;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.ek.ComponentsInMonthService;
import pl.kskowronski.data.service.egeria.ek.graphics.HarmIndividualService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInDayService;
import pl.kskowronski.data.service.egeria.ek.graphics.HoursInMonthService;
import pl.kskowronski.data.service.egeria.global.NapUserService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.componets.PeriodLayout;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@PageTitle("Agency")
@Route(value = "agency", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"admin","user"})
public class AgencyView extends VerticalLayout {

    private ClientService clientService;
    private ZatrudnienieService zatrudnienieService;
    private HarmIndividualService harmIndividualService;
    private ComponentsInMonthService componentsInMonthService;

    private PeriodLayout periodText = new PeriodLayout(1);
    private Grid<Pracownik> grid = new Grid<>(Pracownik.class, false);
    private ComboBox<Client> listAgency;
    private List<Client> agencyList = new ArrayList<>();
    private List<Pracownik> workers = new ArrayList<>();

    private List<HarmIndividual> harms = new ArrayList<>();

    @Autowired
    private WorkCardView workCardView;

    private HorizontalLayout h1 = new HorizontalLayout();
    private Anchor anchorCsv = new Anchor();

    public AgencyView(ClientService clientService, ZatrudnienieService zatrudnienieService, NapUserService napUserService, HoursInMonthService hoursInMonthService
            , HarmIndividualService harmIndividualService, HoursInDayService hoursInDayService, AgencyForLoginService agencyForLoginService, ComponentsInMonthService componentsInMonthService) {
        this.clientService = clientService;
        this.zatrudnienieService = zatrudnienieService;
        this.harmIndividualService = harmIndividualService;
        this.componentsInMonthService = componentsInMonthService;
        this.workCardView = new WorkCardView(this.harmIndividualService, hoursInDayService, hoursInMonthService, componentsInMonthService);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user  = napUserService.findByUsername(userDetails.getUsername());
        var agencyForUser = agencyForLoginService.findAllForUser(user.get().getUzId());
        agencyForUser.stream().forEach( item -> {
            Client client = clientService.findByKlKod(item.getKlKod());
            agencyList.add(client);
        });

        listAgency = getSelectAgency();

        this.setHeight("100%");
        grid.setHeightFull();

        grid.addColumn(Pracownik::getPrcNumer).setHeader("Numer");
        grid.addColumn(Pracownik::getPrcNazwisko).setHeader("Nazwisko");
        grid.addColumn(Pracownik::getPrcImie).setHeader("Imie");
        grid.addColumn(Pracownik::getPrcPesel).setHeader("Pesel");
        grid.addComponentColumn( worker -> {
            Label l = new Label();
            final String[] skList = {""};
            worker.getZatrudnienia().forEach( item -> {
                skList[0] += item.getSkKod() + " ";
            });
            l.setText(skList[0]);
            return l;
        }).setHeader("MPK");
        grid.addComponentColumn(worker -> {
            Button viewWorkCard = new Button("Karta Pracy");
            viewWorkCard.addClickListener( e -> {
                workCardView.openView(worker);
            });
            return viewWorkCard;
        });


        periodText.addClickListener( e -> {
            generateListWorkersForAgency();
        });


        Button butPDF = new Button("PDF");
        butPDF.addClickListener( e -> {
            generatePDF();
        });

        Button butExcel = new Button("Excel");
        butExcel.addClickListener( e -> {
            h1.remove(anchorCsv);
            generateExcel();
        });


        h1.add(butExcel);

        add(new HorizontalLayout(periodText
                        , new Label("Agencja:"), listAgency, h1)
                        , grid);
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( agencyList );
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        selectAgency.addValueChangeListener( e -> {
            h1.remove(anchorCsv);
            generateListWorkersForAgency();
        });
        return selectAgency;
    }

    private void generateListWorkersForAgency() {
        workers = zatrudnienieService.getWorkersEmployedOnTheAgency( periodText.getPeriod(), Long.valueOf("0"), listAgency.getValue().getKlKod() );
        grid.setItems(workers);
    }

    private void generateExcel() {
        harmIndividualService.getHarmForWorkers(workers, periodText.getPeriod());

        PrintPreviewReport<HarmIndividual> reportCsv = new PrintPreviewReport<>();
        reportCsv.setItems(harms);
        reportCsv.getReportBuilder()
                .setPrintColumnNames(false)
                //.setReportLocale(new Locale("pl", "PL"))
                //.addAutoText("Karta żźćółśćą Pracy: " + labNameWorker.getText()+ " " + periodText.getPeriod() +  " ( MPK: " + skList[0] + ")", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
                .addColumn(ColumnBuilder.getNew().setColumnProperty("prcNumer", Integer.class).setTitle("prcNumer").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("prcNazwisko", String.class).setTitle("prcNazwisko").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("prcImie", String.class).setTitle("prcImie").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("skKod", String.class).setTitle("skKod").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiDateS", String.class).setTitle("Dzień").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhFrom", String.class).setTitle("Od").build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hhTo", String.class).setTitle("Do").build())
        ;
        StreamResource csv = reportCsv.getStreamResource("karta_" + "_"+periodText.getPeriod() +".csv"
                , harmIndividualService::getHarmForWorkers, PrintPreviewReport.Format.CSV);

        anchorCsv = new Anchor(csv, "Pobierz");
        h1.add(anchorCsv);
    }

    private void generatePDF() {

        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
        dialog.setHeight("150px");

        PrintPreviewReport<HarmIndividual> report = new PrintPreviewReport<>();
        workers.forEach( item -> {
            var harm = harmIndividualService.getHarmForWorker(item.getPrcId(), periodText.getPeriod());

            report.setItems(harm);
            report.getReportBuilder()
                    .setMargins(20, 20, 40, 40)
                    .addAutoText("+", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200)
                    .setPrintBackgroundOnOddRows(true)
                    .setTitle("Karta Pracy: ")
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").setWidth(15).build())
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").setWidth(15).build())
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setWidth(30).build())
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setWidth(30).build())
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setWidth(30).build())
                    .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").build())

            ;



        });

        StreamResource pdf = report.getStreamResource("karta.pdf", harmIndividualService::getHarmForWorker, PrintPreviewReport.Format.PDF);
        Anchor anchor = new Anchor(pdf, "PDF");
        anchor.setTarget("_blank");
        dialog.add(anchor);

        add(dialog);
        dialog.open();

    }

}
