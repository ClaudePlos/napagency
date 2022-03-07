package pl.kskowronski.data.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.AgencyForLogin;

@Service
public class AgencyForLoginService extends CrudService<AgencyForLogin, Integer> {

    private AgencyForLoginRepo repo;

    public AgencyForLoginService(@Autowired AgencyForLoginRepo repo) {
        this.repo = repo;
    }

    @Override
    protected AgencyForLoginRepo getRepository() {
        return repo;
    }

}
