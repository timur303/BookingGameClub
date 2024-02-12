package kg.kadyrbekov.service;

import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.OrderHistory;
import kg.kadyrbekov.repository.OrderHistoryRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final UserRepository userRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistory> getUserOrderHistory() {
        User user = getAuthentication();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setUser(user);
        return orderHistoryRepository.findByUserId(user.getId());
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }
}
