package com.example.springsecurityexample.dao.entity;

import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@Data
@Enabled
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Builder
@Entity
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override //default olaraq customera "USER" rolunu veririk, basqa rollarda elave elemek olar
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    } //userin rolunu return eleyir

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override //tokenin(sessiyanın) vaxtının bitməsini yoxlayır
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //userin hesabının isActive nin true ya da false olmasını yoxlayır
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //userin kimlik datalarının vaxtının dolub dolmadığını yoxlayır
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //userin active olub olmadığını yoxlayır
    public boolean isEnabled() {
        return isActive;
    }
}
//UserDetails interfeysi bir userin kimlik datalarını təmsil etmək üçün istifadə olunur.
//Bu interfeys userin kimlik authentifikasiyası zamanı lazım olan dataları təmin eləyir
//və userin datalarını güvənli şəkildə idarə etmək üçün UserDetailsService ilə inteqrasiya olunur