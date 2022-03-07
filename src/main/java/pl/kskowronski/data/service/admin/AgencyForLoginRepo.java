package pl.kskowronski.data.service.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.admin.AgencyForLogin;

public interface AgencyForLoginRepo extends JpaRepository<AgencyForLogin, Integer> {

}
