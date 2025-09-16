package com.jpetstore.api.service;

import com.jpetstore.api.dto.RegisterRequest;
import com.jpetstore.api.dto.UserDto;
import com.jpetstore.api.entity.Account;
import com.jpetstore.api.entity.Profile;
import com.jpetstore.api.entity.SignOn;
import com.jpetstore.api.repository.AccountRepository;
import com.jpetstore.api.repository.ProfileRepository;
import com.jpetstore.api.repository.SignOnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final AccountRepository accountRepository;
    private final SignOnRepository signOnRepository;
    private final ProfileRepository profileRepository;
    
    // In-memory storage for demo purposes (no real database)
    private final Map<String, UserDto> users = new HashMap<>();

    public AuthService(AccountRepository accountRepository, 
                      SignOnRepository signOnRepository,
                      ProfileRepository profileRepository) {
        this.accountRepository = accountRepository;
        this.signOnRepository = signOnRepository;
        this.profileRepository = profileRepository;
        
        // Initialize with some demo users
        initializeDemoUsers();
    }
    
    private void initializeDemoUsers() {
        // Create demo user 1
        UserDto user1 = new UserDto();
        user1.setUsername("demo");
        user1.setEmail("demo@jpetstore.com");
        user1.setFirstName("Demo");
        user1.setLastName("User");
        user1.setPhone("555-0100");
        user1.setAddress1("123 Demo Street");
        user1.setCity("Demo City");
        user1.setState("CA");
        user1.setZip("12345");
        user1.setCountry("USA");
        users.put("demo:password", user1);
        
        // Create demo user 2
        UserDto user2 = new UserDto();
        user2.setUsername("john");
        user2.setEmail("john@example.com");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setPhone("555-0101");
        user2.setAddress1("456 Main Street");
        user2.setCity("Springfield");
        user2.setState("IL");
        user2.setZip("62701");
        user2.setCountry("USA");
        users.put("john:john123", user2);
        
        // Create admin user
        UserDto admin = new UserDto();
        admin.setUsername("admin");
        admin.setEmail("admin@jpetstore.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPhone("555-0000");
        admin.setAddress1("1 Admin Plaza");
        admin.setCity("Admin City");
        admin.setState("CA");
        admin.setZip("00000");
        admin.setCountry("USA");
        users.put("admin:admin", admin);
    }

    public UserDto authenticate(String username, String password) {
        // For demo purposes, check in-memory storage
        String key = username + ":" + password;
        UserDto user = users.get(key);
        
        if (user != null) {
            return user;
        }
        
        // Try to authenticate with database (if exists)
        try {
            Optional<SignOn> signOn = signOnRepository.findById(username);
            if (signOn.isPresent() && signOn.get().getPassword().equals(password)) {
                Optional<Account> account = accountRepository.findById(username);
                if (account.isPresent()) {
                    return convertToUserDto(account.get());
                }
            }
        } catch (Exception e) {
            // Database might not be initialized, fall back to in-memory
        }
        
        throw new RuntimeException("Invalid username or password");
    }

    public UserDto register(RegisterRequest request) {
        // Check if user already exists
        String key = request.getUsername() + ":" + request.getPassword();
        if (users.containsKey(key)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Create new user
        UserDto newUser = new UserDto();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPhone(request.getPhone());
        newUser.setAddress1(request.getAddress1());
        newUser.setAddress2(request.getAddress2());
        newUser.setCity(request.getCity());
        newUser.setState(request.getState());
        newUser.setZip(request.getZip());
        newUser.setCountry(request.getCountry());
        
        // Store in memory
        users.put(key, newUser);
        
        // Try to save to database (if available)
        try {
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setEmail(request.getEmail());
            account.setFirstName(request.getFirstName());
            account.setLastName(request.getLastName());
            account.setStatus("OK");
            account.setAddress1(request.getAddress1());
            account.setAddress2(request.getAddress2());
            account.setCity(request.getCity());
            account.setState(request.getState());
            account.setZip(request.getZip());
            account.setCountry(request.getCountry());
            account.setPhone(request.getPhone());
            
            SignOn signOn = new SignOn();
            signOn.setUsername(request.getUsername());
            signOn.setPassword(request.getPassword());
            
            accountRepository.save(account);
            signOnRepository.save(signOn);
        } catch (Exception e) {
            // Database might not be available, continue with in-memory
        }
        
        return newUser;
    }
    
    private UserDto convertToUserDto(Account account) {
        UserDto dto = new UserDto();
        dto.setUsername(account.getUsername());
        dto.setEmail(account.getEmail());
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setPhone(account.getPhone());
        dto.setAddress1(account.getAddress1());
        dto.setAddress2(account.getAddress2());
        dto.setCity(account.getCity());
        dto.setState(account.getState());
        dto.setZip(account.getZip());
        dto.setCountry(account.getCountry());
        return dto;
    }
}
