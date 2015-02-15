package com.lajommariano.service.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lajommariano.model.Address;
import com.lajommariano.model.LabelValue;
import com.lajommariano.model.Role;

public class UserDTO implements UserDetails {

	private Long id;
	private String username;                   
	private String password;
	private String salt;                    
	private String confirmPassword;
	private String passwordHint;
	private String firstName;                   
	private String lastName;                    
	private String email;                       
	private String phoneNumber;
	private String website;
	private Integer version;
	private boolean enabled;
	private boolean accountExpired;
	private boolean accountLocked;
	private boolean credentialsExpired;
	private Date birthdate;
	private Date premiumDate;
	private Date registrationDate;

	private Set<Role> roles = new HashSet<Role>();
	private Address address = new Address();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getPasswordHint() {
		return passwordHint;
	}
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isAccountExpired() {
		return accountExpired;
	}
	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}
	public boolean isAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}
	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public Date getPremiumDate() {
		return premiumDate;
	}
	public void setPremiumDate(Date premiumDate) {
		this.premiumDate = premiumDate;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
		authorities.addAll(roles);
		return authorities;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	public boolean isCredentialsNonExpired() {
		return !isCredentialsExpired();
	}
	public void addRole(Role role) {
		getRoles().add(role);	
	}

	public String toString(){
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("username", this.username)
		.append("password", this.password)
		.append("enabled", this.enabled)
		.append("accountExpired", this.accountExpired)
		.append("credentialsExpired", this.credentialsExpired)
		.append("accountLocked", this.accountLocked);

		if (roles != null) {
			sb.append("Granted Authorities: ");

			int i = 0;
			for (Role role : roles) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(role.toString());
				i++;
			}
		} else {
			sb.append("No Granted Authorities");
		}
		return sb.toString();
	}
	
    public String getFullName() {
        return firstName + ' ' + lastName;
    }
    
    public List<LabelValue> getRoleList() {
        List<LabelValue> userRoles = new ArrayList<LabelValue>();

        if (this.roles != null) {
            for (Role role : roles) {
                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getName(), role.getName()));
            }
        }

        return userRoles;
    }

}
