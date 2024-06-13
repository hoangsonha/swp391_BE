package com.group6.swp391.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegister {

//    @NotNull(message = "Họ không hợp lệ")
//    @NotBlank(message = "Họ không hợp lệ")
//    @Size(max = 20, min = 1, message = "Họ phải từ 1 tới 20 kí tự")
    private String firstName;

//    @NotNull(message = "Tên không hợp lệ")
//    @NotBlank(message = "Tên không hợp lệ")
//    @Size(max = 20, min = 1, message = "Tên phải từ 1 tới 20 kí tự")
    private String lastName;

    @NotNull(message = "Email không hợp lệ")
    @NotBlank(message = "Email không hợp lệ")
    @Size(max = 100, min = 10, message = "Email phải từ 10 tới 100 kí tự bao gồm cả @gmail.com")
    private String email;

    @NotNull(message = "Mật khẩu không hợp lệ")
    @NotBlank(message = "Mật khẩu không hợp lệ")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

//    @NotNull(message = "Số điện thoại không hợp lệ")
//    @NotBlank(message = "Số điện thoại không hợp lệ")
//    @Size(max = 15, min = 10, message = "Số điện thoại phải từ 10 tới 25 kí tự")
    private String phone;

//    @NotNull(message = "Địa chỉ không hợp lệ")
//    @NotBlank(message = "Địa chỉ không hợp lệ")
//    @Size(max = 300, min = 2, message = "Địa chỉ phải từ 2 tới 300 kí tự")
    private String address;
    private String avata;

    @NotNull(message = "Quyền không hợp lệ")
    @NotBlank(message = "Quyền không hợp lệ")
    private String role;
}
