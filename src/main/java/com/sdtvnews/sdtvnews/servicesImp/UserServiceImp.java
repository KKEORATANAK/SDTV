package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.EncryptionUtil;
import com.sdtvnews.sdtvnews.dto.ListUserDTO;
import com.sdtvnews.sdtvnews.dto.request.UserRequest;
import com.sdtvnews.sdtvnews.dto.response.UserResponse;
import com.sdtvnews.sdtvnews.entity.User;
import com.sdtvnews.sdtvnews.repository.UserRepository;
import com.sdtvnews.sdtvnews.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public UserRequest createUser(UserRequest request) {
        // Check if the user already exists
        List<User> existingUser = userRepository.findAll();
        for (User user : existingUser) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            String requestFullName = request.getFirstName() + " " + request.getLastName();

            if (fullName.toLowerCase().contains(requestFullName.toLowerCase())) {
                // Return a message if a similar employee already exists
                throw new CustomException("An user with a similar name '" + requestFullName + "' already exists.");
            }
        }

        // Create a new User if it doesn't exist
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setUserName(request.getUserName());
        // Hash the password before saving
        String encryptedPassword = encryptionUtil.encrypt(request.getPassWord());
        newUser.setPassWord(encryptedPassword);
        newUser.setRoleId(request.getRoleId());
        newUser.setStatus("1");//1=active;0=dis-active
        newUser.setCreateBy(request.getCreateBy());
        newUser.setCreateDate(LocalDateTime.now());
        // Save the new user
        userRepository.save(newUser);

        return request; // or convert and return the saved entity as needed
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<ListUserDTO> users = userRepository.lstAllUser(); // Retrieve all categories from the repository
        List<UserResponse> userResponses = new ArrayList<>();

        for (ListUserDTO user : users) {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setRoleName(user.getName());
            response.setStatus(user.getStatus());
            response.setCreateDate(user.getCreateDate());
            // Set other necessary fields
            userResponses.add(response);
        }

        return userResponses;
    }

    @Override
    public List<UserResponse> getActiveUser() {
        List<User> users = userRepository.lstActiveUser(); // Retrieve all categories from the repository
        List<UserResponse> userResponses = new ArrayList<>();

        for (User user : users) {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            // Set other necessary fields
            userResponses.add(response);
        }

        return userResponses;
    }


    @Override
    public Optional<UserResponse> getUserById(Long id){
        // Retrieve the user by its ID
        Optional<User> userOptional = userRepository.findById(id);

        // Check if the user exists
        if (userOptional.isPresent()) {
            // Convert User to UserResponse if it exists
            User user = userOptional.get();
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setUserName(user.getUserName());
            // Decrypt the password to show it to admin
            String decryptedPassword = encryptionUtil.decrypt(user.getPassWord());
            userResponse.setPassWord(decryptedPassword);
            userResponse.setRoleId(user.getRoleId());
            userResponse.setStatus(user.getStatus());
            // Set other necessary fields
            return Optional.of(userResponse);
        } else {
            // Return an empty Optional if the user doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateUser(Long id, UserRequest userRequest){
        // Find the user by its ID
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update the user details
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setUserName(userRequest.getUserName());
            String encryptedPassword = encryptionUtil.encrypt(userRequest.getPassWord());
            user.setPassWord(encryptedPassword);
            user.setRoleId(userRequest.getRoleId());
            user.setStatus(userRequest.getStatus());
            user.setUpdateBy(userRequest.getUpdateBy());
            user.setUpdateDate(LocalDateTime.now());
            // Save the updated user
            userRepository.save(user);
        } else {
            throw new CustomException("User not found with ID: " + id);
        }
    }


    @Override
    public void updateUserStatus(Long id, String status) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(status); // Update the status (e.g., "0" for inactive)
            userRepository.save(user); // Save the updated user
        } else {
            throw new CustomException("User not found with ID: " + id);
        }
    }

    public boolean isNameDuplicate(String name) {
        // Check if the title exists in the database
        return userRepository.existsByUserName(name);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
