package no.ntnu.apotychia.service.security;

import no.ntnu.apotychia.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class ApotychiaUserDetails implements UserDetails {

    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String ROLE_USER = "ROLE_USER";

    private Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
    private User user;

    public ApotychiaUserDetails(User user) {
        Assert.notNull(user, "The provided user reference can't be null");
        this.user = user;
        for (String ga : Arrays.asList(ROLE_USER, SCOPE_READ, SCOPE_WRITE)) {
            this.grantedAuthorities.add(new SimpleGrantedAuthority(ga));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return this.user;
    }
}
