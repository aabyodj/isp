package by.aab.isp.service.validator;

import org.springframework.stereotype.Service;

import by.aab.isp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailAvailabilityValidator {

    private final UserRepository userRepository;

    public boolean isEmailAvailable(String email, Long userId) {
        return userRepository.findIdByEmail(email)
                .filter(id -> !id.equals(userId))
                .isEmpty();
    }

}
