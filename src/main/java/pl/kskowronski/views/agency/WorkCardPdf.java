package pl.kskowronski.views.agency;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;

import java.util.List;

//@Route("report")
//@RolesAllowed({"admin","user"})
public class WorkCardPdf extends Dialog {

    VerticalLayout v01 = new VerticalLayout();

    public WorkCardPdf() {
        this.setWidth("1200px");
        this.setHeight("750px");
        add(v01);
    }

    public void openPopUp(List<HarmIndividual> harm ) {

        Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
        Style groupStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM_BOLD).build();

        PrintPreviewReport<HarmIndividual> report = new PrintPreviewReport<>();
        v01.add(new Label("Raport:"), report);
        report.setItems(harm);
        report.getReportBuilder()
                .setMargins(20, 20, 40, 40)
                .addAutoText("+", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, groupStyle)
                .setPrintBackgroundOnOddRows(true)
                .setTitle("Karta Pracy: ")
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiType", String.class).setTitle("Typ").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("day", String.class).setTitle("D").setStyle(headerStyle).setWidth(15).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiNameHarm", String.class).setTitle("Zmiana").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursPlan", Integer.class).setTitle("Plan").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("hiHoursOverworked", Integer.class).setTitle("Wykonanie").setStyle(headerStyle).setWidth(30).build())
                .addColumn(ColumnBuilder.getNew().setColumnProperty("absenceName", String.class).setTitle("").setStyle(headerStyle).build())

        ;

        v01.setPadding(false);



        this.open();
    }
}
