package pl.kskowronski.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kskowronski.data.Role;
import pl.kskowronski.data.entity.User;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.egeria.global.NapUserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NapUserRepo napUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NapUser user = napUserRepo.findByUsernamePG(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            //user.setPassword(getMd5(user.getPassword()));
            user.setRoles(Collections.singleton(Role.USER));
            var uS = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    getAuthorities(user));
            return uS;
        }
    }

    private static List<GrantedAuthority> getAuthorities(NapUser user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());

    }


    private static String getMd5(String input) {
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }
    }

}
