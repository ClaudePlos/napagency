package pl.kskowronski.views.agency;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.egeria.ek.graphics.HarmIndividual;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route("report")
@RolesAllowed({"admin","user"})
public class WorkCardPdf extends VerticalLayout {

    public WorkCardPdf(List<HarmIndividual> harm ) {

        PrintPreviewReport<HarmIndividual> report = new PrintPreviewReport<>(HarmIndividual.class);
        report.setItems(harm);
        report.getReportBuilder().setTitle("Karta Pracy");

        add(report);

    }
}
