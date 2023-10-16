package com.example.kdbe.web;


import com.example.kdbe.auth.KdbeUserDetail;
import com.example.kdbe.auth.KdbeUserDetailService;
import com.example.kdbe.model.dto.request.SignInRequestDto;
import com.example.kdbe.model.dto.request.SignUpRequestDto;
import com.example.kdbe.model.dto.request.UserRequestDto;
import com.example.kdbe.model.dto.response.SignInResponseDto;
import com.example.kdbe.model.entity.User;
import com.example.kdbe.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController implements BaseController<User,UserRequestDto> {

    @Getter
    private final UserService service;

    private final KdbeUserDetailService userDetailService;

    @PostMapping("/signIn")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto req)
    {
        KdbeUserDetail userDetail = userDetailService.loadUserByUsername(req.getUserId());

        return service.signIn(req, userDetail);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto req)
    {
        boolean res = service.signUp(req);

        if(!res)
        {
            return ResponseEntity.badRequest().body("SignUp Failed");
        }

        return ResponseEntity.ok(req);
    }

}