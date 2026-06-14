package com.uniovi.sdi.sdi2526extr203spring.dtos;

public class ChangePasswordDto {
    private String actualPassword;
    private String password;
    private String passwordConfirm;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String currentPassword, String newPassword, String confirmPassword) {
        this.actualPassword = currentPassword;
        this.password = newPassword;
        this.passwordConfirm = confirmPassword;

    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String actualPassword) {
        this.actualPassword = actualPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
