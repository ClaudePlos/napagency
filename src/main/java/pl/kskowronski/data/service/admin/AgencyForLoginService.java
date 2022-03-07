package pl.kskowronski.data.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.AgencyForLogin;

import java.util.List;

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

    public List<AgencyForLogin> findAll() {
        List<AgencyForLogin> list = repo.findAll();
        return list;
    }

    public List<AgencyForLogin> findAllForUser( Integer userId) {
        List<AgencyForLogin> list = repo.findAllForUser( userId );
        return list;
    }

    public void save(AgencyForLogin agencyForLogin) {
        repo.save(agencyForLogin);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

}
