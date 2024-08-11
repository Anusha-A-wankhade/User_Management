//package com.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.dto.ReqRes;
//import com.entity.OurUsers;
//import com.repository.UsersRepo;
//
//@Service
//public class UsersManagementService {
//
//    @Autowired
//    private UsersRepo usersRepo;
//    @Autowired
//    private JWTUtils jwtUtils;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    public ReqRes register(ReqRes registrationRequest){
//        ReqRes resp = new ReqRes();
//
//        try {
//            OurUsers ourUser = new OurUsers();
//            ourUser.setEmail(registrationRequest.getEmail());
//            ourUser.setCity(registrationRequest.getCity());
//            ourUser.setRole(registrationRequest.getRole());
//            ourUser.setName(registrationRequest.getName());
//            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//            OurUsers ourUsersResult = usersRepo.save(ourUser);
//            if (ourUsersResult.getId()>0) {
//                resp.setOurUserList((ourUsersResult));
//                resp.setMessege("User Saved Successfully");
//                resp.setStatusCode(200);
//            }
//
//        }catch (Exception e){
//            resp.setStatusCode(500);
//            resp.setError(e.getMessage());
//        }
//        return resp;
//    }
//
//
//    public ReqRes login(ReqRes loginRequest){
//        ReqRes response = new ReqRes();
//        try {
//            authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
//                            loginRequest.getPassword()));
//            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
//            var jwt = jwtUtils.generateToken(user);
//            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
//            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRole(user.getRole());
//            response.setRefreshToken(refreshToken);
//            response.setExpirationTime("24Hrs");
//            response.setMessege("Successfully Logged In");
//
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessege(e.getMessage());
//        }
//        return response;
//    }
//
//
//
//
//
//    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
//        ReqRes response = new ReqRes();
//        try{
//            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
//            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
//            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
//                var jwt = jwtUtils.generateToken(users);
//                response.setStatusCode(200);
//                response.setToken(jwt);
//                response.setRefreshToken(refreshTokenReqiest.getToken());
//                response.setExpirationTime("24Hr");
//                response.setMessege("Successfully Refreshed Token");
//            }
//            response.setStatusCode(200);
//            return response;
//
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessege(e.getMessage());
//            return response;
//        }
//    }
//
//
//    public ReqRes getAllUsers() {
//        ReqRes reqRes = new ReqRes();
//
//        try {
//            List<OurUsers> result = usersRepo.findAll();
//            if (!result.isEmpty()) {
//                reqRes.setOurUsersList(result);
//                reqRes.setStatusCode(200);
//                reqRes.setMessege("Successful");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessege("No users found");
//            }
//            return reqRes;
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessege("Error occurred: " + e.getMessage());
//            return reqRes;
//        }
//    }
//
//
//    public ReqRes getUsersById(Integer id) {
//        ReqRes reqRes = new ReqRes();
//        try {
//            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
//            reqRes.setOurUserList(usersById);
//            reqRes.setStatusCode(200);
//            reqRes.setMessege("Users with id '" + id + "' found successfully");
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessege("Error occurred: " + e.getMessage());
//        }
//        return reqRes;
//    }
//
//
//    public ReqRes deleteUser(Integer userId) {
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<OurUsers> userOptional = usersRepo.findById(userId);
//            if (userOptional.isPresent()) {
//                usersRepo.deleteById(userId);
//                reqRes.setStatusCode(200);
//                reqRes.setMessege("User deleted successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessege("User not found for deletion");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessege("Error occurred while deleting user: " + e.getMessage());
//        }
//        return reqRes;
//    }
//
//    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<OurUsers> userOptional = usersRepo.findById(userId);
//            if (userOptional.isPresent()) {
//                OurUsers existingUser = userOptional.get();
//                existingUser.setEmail(updatedUser.getEmail());
//                existingUser.setName(updatedUser.getName());
//                existingUser.setCity(updatedUser.getCity());
//                existingUser.setRole(updatedUser.getRole());
//
//                // Check if password is present in the request
//                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
//                    // Encode the password and update it
//                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//                }
//
//                OurUsers savedUser = usersRepo.save(existingUser);
//                reqRes.setOurUserList(savedUser);
//                reqRes.setStatusCode(200);
//                reqRes.setMessege("User updated successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessege("User not found for update");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessege("Error occurred while updating user: " + e.getMessage());
//        }
//        return reqRes;
//    }
//
//
//    public ReqRes getMyInfo(String email){
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
//            if (userOptional.isPresent()) {
//                reqRes.setOurUserList(userOptional.get());
//                reqRes.setStatusCode(200);
//                reqRes.setMessege("successful");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessege("User not found for update");
//            }
//
//        }catch (Exception e){
//            reqRes.setStatusCode(500);
//            reqRes.setMessege("Error occurred while getting user info: " + e.getMessage());
//        }
//        return reqRes;
//
//    }
//}

package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.ReqRes;
import com.entity.OurUsers;
import com.repository.UsersRepo;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId() > 0) {
                resp.setOurUserList(List.of(ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers user = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();
        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUserList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers userById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUserList(List.of(userById)); // Wrap in a list
            reqRes.setStatusCode(200);
            reqRes.setMessage("User with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUserList(List.of(savedUser)); // Wrap in a list
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUserList(List.of(userOptional.get())); // Wrap in a list
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }
}
