package com.filmdoms.community.account.service;

import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.DeleteAccountRequestDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdatePasswordRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdateProfileRequestDto;
import com.filmdoms.community.account.data.dto.response.AccountResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final FileRepository fileRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 유저 이메일과 비밀번호를 확인해 계정 정보를 찾는다.
     *
     * @param email 유저 이메일
     * @param password 비밀번호
     * @return 계정정보
     */
    public String login(String email, String password) {
        // 가입 여부 확인
        AccountDto accountDto = accountRepository.findByEmail(email)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호를 암호화 시켜 저장된 비밀번호와 대조
        if (!passwordEncoder.matches(password, accountDto.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 반환
        return jwtTokenProvider.createToken(String.valueOf(accountDto.getId()));
    }

//    public boolean isUsernameDuplicate(String username) {
//        return accountRepository.existsByUsername(username);
//    }

    public boolean isEmailDuplicate(String email) {
        return accountRepository.existsByEmail(email);
    }

    public void createAccount(JoinRequestDto requestDto) {

//        log.info("아이디 중복 확인");
//        if (isUsernameDuplicate(requestDto.getUsername())) {
//            throw new ApplicationException(ErrorCode.DUPLICATE_USERNAME);
//        }

        log.info("이메일 중복 확인");
        if (isEmailDuplicate(requestDto.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
        }

        log.info("Account 엔티티 생성");
        Account newAccount = Account.builder()
                //.username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .role(AccountRole.USER)
                .profileImage(getDefaultImage())
                .build();

        log.info("Account 엔티티 저장");
        accountRepository.save(newAccount);
    }

    // TODO: 프로필 기본 이미지 어떻게 처리할 지 상의 필요
    private File getDefaultImage() {
        return fileRepository.findById(1L).orElse(File.builder()
                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                .originalFileName("original_file_name")
                .build()
        );
    }

    public AccountResponseDto readAccount(AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmailWithImage(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        return AccountResponseDto.from(account);
    }

    @Transactional
    public AccountResponseDto updateAccountProfile(UpdateProfileRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmailWithImage(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        File profileImage = account.getProfileImage();
        if (!Objects.equals(requestDto.getFileId(), profileImage.getId())) {
            log.info("요청 File 엔티티 호출");
            profileImage = fileRepository.findById(requestDto.getFileId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        }

        log.info("Account 엔티티 수정");
        account.updateProfile(requestDto.getNickname(), profileImage);

        return AccountResponseDto.from(account);
    }

    @Transactional
    public void updateAccountPassword(UpdatePasswordRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("기존 비밀번호와 대조");
        if (!passwordEncoder.matches(requestDto.getOldPassword(), account.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("비밀번호 수정");
        account.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Transactional
    public void deleteAccount(DeleteAccountRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("기존 비밀번호와 대조");
        if (!passwordEncoder.matches(requestDto.getPassword(), account.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("Account 엔티티 삭제");
        accountRepository.delete(account);
    }
}
