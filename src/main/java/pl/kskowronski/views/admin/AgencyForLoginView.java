package pl.kskowronski.views.admin;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.data.entity.admin.AgencyForLogin;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.global.NapUserService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("AgencyForLoginView")
@Route(value = "agencyforlogin", layout = MainLayout.class)
@RolesAllowed("user")
public class AgencyForLoginView extends Div {

    private NapUserService napUserService;
    private ClientService clientService;

    private Crud<AgencyForLogin> crud;

    private String USER_NAME = "userName";
    private String AGENCY_NAME = "agencyName";
    private String EDIT_COLUMN = "vaadin-crud-edit-column";

    public AgencyForLoginView(NapUserService napUserService, ClientService clientService) {
        this.napUserService = napUserService;
        this.clientService = clientService;

        setHeight("100%");
        crud = new Crud<>(
                AgencyForLogin.class,
                createEditor()
        );
        crud.setHeightFull();

        add( new HorizontalLayout(crud));
    }

    private CrudEditor<AgencyForLogin> createEditor() {

        ComboBox<NapUser> selectUser = getSelectUser();
        ComboBox<Client> selectSkKod = getSelectAgency();

        FormLayout form = new FormLayout(selectUser, selectSkKod);

        Binder<AgencyForLogin> binder = new Binder<>(AgencyForLogin.class);
        binder.forField(selectUser).asRequired().bind(AgencyForLogin::getNapUser, AgencyForLogin::setNapUser);
        binder.forField(selectSkKod).asRequired().bind(AgencyForLogin::getClient, AgencyForLogin::setClient);


        return new BinderCrudEditor<>(binder, form);
    }

    private ComboBox<NapUser> getSelectUser() {
        ComboBox<NapUser> selectUser = new ComboBox<>();
        selectUser.setItems( query -> napUserService.findAll("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()) );
        selectUser.setItemLabelGenerator(NapUser::getUsername);
        selectUser.setLabel("Login");
        return selectUser;
    }

    private ComboBox<Client> getSelectAgency() {
        ComboBox<Client> selectAgency = new ComboBox<>();
        selectAgency.setItems( query -> clientService.findAllAgency("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()));
        selectAgency.setItemLabelGenerator(Client::getKldNazwa);
        return selectAgency;
    }


}
