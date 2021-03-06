package com.lajommariano.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.DiscriminatorColumn;

/**
 * This class represents the basic "user" object in AppFuse that allows for authentication
 * and user management.  It implements Acegi Security's UserDetails interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *         Extended to implement Acegi UserDetails interface
 *         by David Carter david@carter.net
 */

@Entity
@Table(name = "user")
@Indexed
@DiscriminatorColumn(name="user_type")
public class User extends BaseModel implements Serializable {
    private static final long serialVersionUID = 3832626162173359411L;

    protected String username;                    // required
    protected String password;
    protected String salt;                    // required
    protected String confirmPassword;
    protected String passwordHint;
    protected String firstName;                   // required
    protected String lastName;                    // required
    protected String email;                       // required; unique
    protected String phoneNumber;
    protected String website;
    protected Address address = new Address();
    protected Integer version;
    protected Set<Role> roles = new HashSet<Role>();
    protected boolean enabled;
    protected boolean accountExpired;
    protected boolean accountLocked;
    protected boolean credentialsExpired;
    protected Date birthdate;
	protected Date premiumDate;
	protected Date registrationDate;

	protected List<User> following = new ArrayList<User>();

	protected List<User> followers = new ArrayList<User>();
	
    public User() {
    }

    public User(final String username) {
        this.username = username;
    }

    @Column(nullable = false, length = 50, unique = true)
    @Field
    public String getUsername() {
        return username;
    }

    @Column(nullable = false)
    @XmlTransient
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Column(name="salt", nullable=true, length=50)
    public String getSalt(){
        return salt;
    }

    @Column(name = "password_hint")
    @XmlTransient
    public String getPasswordHint() {
        return passwordHint;
    }

    @Column(name = "first_name", nullable = false, length = 50)
    @Field
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", nullable = false, length = 50)
    @Field
    public String getLastName() {
        return lastName;
    }

    @Column(nullable = false, unique = true)
    @Field
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number")
    @Field(analyze= Analyze.NO)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Field
    public String getWebsite() {
        return website;
    }

    @Embedded
    @IndexedEmbedded
    public Address getAddress() {
        return address;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)    
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )public Set<Role> getRoles() {
        return roles;
    }

    @Column(name="birthdate", nullable=false)
	public Date getBirthdate() {
		return birthdate;
	}

    @Column(name="premium_date", nullable=false)
	public Date getPremiumDate() {
		return premiumDate;
	}

    @Column(name="registration_date", nullable=false)
	public Date getRegistrationDate() {
		return registrationDate;
	}

    @ManyToMany(targetEntity=User.class,fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(
		name = "follow", 
		joinColumns = { @JoinColumn(name="follower_id", nullable=false, updatable=false) }, 
		inverseJoinColumns = { @JoinColumn(name = "following_id", nullable=false, updatable=false) }
	)
    public List<User> getFollowing() {
		return following;
	}

    @ManyToMany(fetch=FetchType.LAZY, mappedBy="following")
	public List<User> getFollowers() {
		return followers;
	}

	public void addRole(Role role) {
        getRoles().add(role);
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    @Column(name = "account_enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @Column(name = "account_expired", nullable = false)
    public boolean isAccountExpired() {
        return accountExpired;
    }

    @Column(name = "account_locked", nullable = false)
    public boolean isAccountLocked() {
        return accountLocked;
    }

    @Column(name = "credentials_expired", nullable = false)
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setPremiumDate(Date premiumDate) {
		this.premiumDate = premiumDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;

        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);

    }

  
    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

   
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("username", this.username)
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

	public String getConfirmPassword() {
		return confirmPassword;
	}
}
