package com.example.cleardayapplication.domain.repository;

import com.example.cleardayapplication.domain.model.User;
import com.example.cleardayapplication.domain.utils.RepositoryCallback;

/**
 * Repository interface that defines the data access operations for {@link User} entities.
 * This interface abstracts the underlying data source (e.g., Firebase, Room, or a Web API)
 * and provides asynchronous methods for user management.
 *
 * @see User
 * @see RepositoryCallback
 */
public interface UserRepository {
    void createUser(User user, RepositoryCallback<Void> callback);
    void getUserById(String userId, RepositoryCallback<User> callback);
    void loginUser(String email, String password, RepositoryCallback<User> callback);
}
// after end undrastand this code delete it
/*
* userRepository.createUser(user, new RepositoryCallback<Void>() {
*
*    @Override
*    public void onSuccess(Void result) {
        // ماذا يحدث عند النجاح؟
*        navigateToHome();
*    }
*
*   @Override
*   public void onError(Exception e) {
       // ماذا يحدث عند الفشل؟
*        showError(e.getMessage());
*   }
*});
 */