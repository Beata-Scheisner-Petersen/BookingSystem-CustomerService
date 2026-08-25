package org.example.customerservice.security.password;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    public String hash(String rawPassword) {
        return BCrypt.hashpw(
                rawPassword,
                BCrypt.gensalt()
        );
    }

    public boolean matches(
            String rawPassword,
            String hashedPassword
    ) {
        return BCrypt.checkpw(
                rawPassword,
                hashedPassword
        );
    }
}
