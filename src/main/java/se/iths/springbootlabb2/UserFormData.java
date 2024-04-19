package se.iths.springbootlabb2;

import lombok.Getter;
import lombok.Setter;
import se.iths.springbootlabb2.entities.UserEntity;

@Getter
@Setter
public class UserFormData {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;

    public UserFormData() {
    }

    public UserFormData(String userName, String firstName, String lastName, String email, String profilePicture) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public UserEntity toEntity() {
        var userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setProfilePicture(profilePicture);
        return userEntity;
    }
}