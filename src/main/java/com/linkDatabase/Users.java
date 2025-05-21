package com.linkDatabase;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "app_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String pass;
    private boolean isActivated;
    private boolean isDisabled;
    private Date createdAt;

    // Profile photo path (for backward compatibility)
    private String profilePhoto;

    // Add profile photo data as BLOB
    @Lob
    @Column(name = "profile_photo_data", columnDefinition = "LONGBLOB")
    private byte[] profilePhotoData;

    // Add profile photo content type
    @Column(name = "profile_photo_content_type")
    private String profilePhotoContentType;

    // Add cover photo fields after the profile photo fields
    @Lob
    @Column(name = "cover_photo_data", columnDefinition = "LONGBLOB")
    private byte[] coverPhotoData;

    @Column(name = "cover_photo_content_type")
    private String coverPhotoContentType;

    private String coverPhoto;

    // Add fields for additional profile information
    private String mobile;
    private String aboutMe;
    private String country;
    private String city;
    private String state;
    private String gender;
    private String birthdate;
    private String language;

    // Add resetToken field for password reset
    private String resetToken;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return pass; }
    public void setPassword(String pass) { this.pass = pass; }
    public boolean getIsActivated() { return isActivated; }
    public void setIsActivated(boolean isActivated) { this.isActivated = isActivated; }
    public boolean getIsDisabled() { return isDisabled; }
    public void setIsDisabled(boolean isDisabled) { this.isDisabled = isDisabled; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    // Add getters and setters for the new fields
    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public byte[] getProfilePhotoData() { return profilePhotoData; }
    public void setProfilePhotoData(byte[] profilePhotoData) { this.profilePhotoData = profilePhotoData; }

    public String getProfilePhotoContentType() { return profilePhotoContentType; }
    public void setProfilePhotoContentType(String profilePhotoContentType) { this.profilePhotoContentType = profilePhotoContentType; }

    public byte[] getCoverPhotoData() { return coverPhotoData; }
    public void setCoverPhotoData(byte[] coverPhotoData) { this.coverPhotoData = coverPhotoData; }

    public String getCoverPhotoContentType() { return coverPhotoContentType; }
    public void setCoverPhotoContentType(String coverPhotoContentType) { this.coverPhotoContentType = coverPhotoContentType; }

    public String getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(String coverPhoto) { this.coverPhoto = coverPhoto; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAboutMe() { return aboutMe; }
    public void setAboutMe(String aboutMe) { this.aboutMe = aboutMe; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    // Add getter and setter for resetToken
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
}
