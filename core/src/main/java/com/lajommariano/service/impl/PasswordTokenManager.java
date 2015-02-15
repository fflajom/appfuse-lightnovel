package com.lajommariano.service.impl;

import com.lajommariano.service.model.UserDTO;
import com.lajommariano.model.User;


public interface PasswordTokenManager {

    /**
     * {@inheritDoc}
     */
    String generateRecoveryToken(UserDTO user);

    /**
     * {@inheritDoc}
     */
    boolean isRecoveryTokenValid(UserDTO user, String token);

    void invalidateRecoveryToken(UserDTO user, String token);
}