package com.lajommariano.service.impl;

import org.apache.commons.lang.StringUtils;

import com.lajommariano.service.model.UserDTO;
import com.lajommariano.dao.UserDao;
import com.lajommariano.model.User;
import com.lajommariano.service.MailEngine;
import com.lajommariano.service.UserExistsException;
import com.lajommariano.service.UserManager;
import com.lajommariano.service.UserService;
import com.lajommariano.util.DozerHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("userManager")
@WebService(serviceName = "UserService", endpointInterface = "com.lajommariano.service.UserService")
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements UserManager, UserService {
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;

    private DozerHelper mapper;
	private MailEngine mailEngine;
    private SimpleMailMessage message;
    private PasswordTokenManager passwordTokenManager;

    private String passwordRecoveryTemplate = "passwordRecovery.vm";
    private String passwordUpdatedTemplate = "passwordUpdated.vm";

    @Autowired
    @Qualifier("passwordEncoder")
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Autowired
    public void setUserDao(final UserDao userDao) {
        this.dao = userDao;
        this.userDao = userDao;
    }

    @Autowired(required = false)
    public void setMailEngine(final MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    @Autowired(required = false)
    public void setMailMessage(final SimpleMailMessage message) {
        this.message = message;
    }

    @Autowired(required = false)
    public void setPasswordTokenManager(final PasswordTokenManager passwordTokenManager) {
        this.passwordTokenManager = passwordTokenManager;
    }
    
    public DozerHelper getMapper() {
		return mapper;
	}

    @Autowired
	public void setMapper(DozerHelper mapper) {
		this.mapper = mapper;
	}

    /**
     * Velocity template name to send users a password recovery mail (default
     * passwordRecovery.vm).
     *
     * @param passwordRecoveryTemplate the Velocity template to use (relative to classpath)
     * @see com.lajommariano.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String, java.util.Map)
     */
    public void setPasswordRecoveryTemplate(final String passwordRecoveryTemplate) {
        this.passwordRecoveryTemplate = passwordRecoveryTemplate;
    }

    /**
     * Velocity template name to inform users their password was updated
     * (default passwordUpdated.vm).
     *
     * @param passwordUpdatedTemplate the Velocity template to use (relative to classpath)
     * @see com.lajommariano.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String, java.util.Map)
     */
    public void setPasswordUpdatedTemplate(final String passwordUpdatedTemplate) {
        this.passwordUpdatedTemplate = passwordUpdatedTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUser(final String userId) {
        User user = userDao.get(new Long(userId));
        UserDTO mappedUser = mapper.map(user, UserDTO.class);
        user = null;
        return mappedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> getUsers() {
        return mapper.map(userDao.getAllDistinct(), UserDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO saveUser(final UserDTO user) throws UserExistsException {

    	if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }

        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                final String currentPassword = userDao.getUserPassword(user.getId());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }

        try {
        	System.out.println(user.getId());
        	System.out.println(mapper.map(user, User.class).getId());
            return mapper.map(userDao.saveUser(mapper.map(user, User.class)), UserDTO.class);
        } catch (final Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(final UserDTO user) {
        log.debug("removing user: " + user);
        userDao.remove(mapper.map(user, User.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(final String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(new Long(userId));
    }

    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException thrown when username not found
     */
    @Override
    public UserDTO getUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug(mapper.map(userDao.loadUserByUsername(username), UserDTO.class));
        return mapper.map(userDao.loadUserByUsername(username), UserDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> search(final String searchTerm) {
        return mapper.map(super.search(searchTerm, User.class), UserDTO.class);
    }

    @Override
    public String buildRecoveryPasswordUrl(final UserDTO user, final String urlTemplate) {
        final String token = generateRecoveryToken(user);
        final String username = user.getUsername();
        return StringUtils.replaceEach(urlTemplate,
                new String[]{"{username}", "{token}"},
                new String[]{username, token});
    }

    @Override
    public String generateRecoveryToken(final UserDTO user) {
        return passwordTokenManager.generateRecoveryToken(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRecoveryTokenValid(final String username, final String token) {
        return isRecoveryTokenValid(getUserByUsername(username), token);
    }

    @Override
    public boolean isRecoveryTokenValid(final UserDTO user, final String token) {
        return passwordTokenManager.isRecoveryTokenValid(user, token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPasswordRecoveryEmail(final String username, final String urlTemplate) {
        log.debug("Sending password recovery token to user: " + username);

        final UserDTO user = getUserByUsername(username);
        final String url = buildRecoveryPasswordUrl(user, urlTemplate);

        sendUserEmail(user, passwordRecoveryTemplate, url);
    }

    private void sendUserEmail(final UserDTO user, final String template, final String url) {
        message.setTo(user.getUsername() + "<" + user.getEmail() + ">");

        final Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("applicationURL", url);

        mailEngine.sendMessage(message, template, model);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO updatePassword(final String username, final String currentPassword, final String recoveryToken, final String newPassword, final String applicationUrl) throws UserExistsException {
        UserDTO user = getUserByUsername(username);
        if (isRecoveryTokenValid(user, recoveryToken)) {
            log.debug("Updating password from recovery token for user:" + username);
            User plainUser = userDao.loadUserByUsername(username);
            
            plainUser.setPassword(newPassword);
            plainUser = userDao.save(plainUser);
            passwordTokenManager.invalidateRecoveryToken(user, recoveryToken);

            sendUserEmail(user, passwordUpdatedTemplate, applicationUrl);

            return mapper.map(plainUser, UserDTO.class);
        } else if (StringUtils.isNotBlank(currentPassword)) {
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                log.debug("Updating password (providing current password) for user:" + username);
                User plainUser = userDao.loadUserByUsername(username);
                plainUser.setPassword(newPassword);
                plainUser = userDao.save(plainUser);
                return mapper.map(plainUser, UserDTO.class);
            }
        }
        // or throw exception
        return null;
    }


	public UserDetails loadUserByUsername(String arg0){
		return getUserByUsername(arg0);
	}

	
}
