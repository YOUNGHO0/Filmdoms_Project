package com.filmdoms.community.banner.service;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.banner.data.dto.request.BannerRequestDto;
import com.filmdoms.community.banner.data.dto.response.BannerResponseDto;
import com.filmdoms.community.banner.data.entity.Banner;
import com.filmdoms.community.banner.repository.BannerRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {BannerService.class})
@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 배너 서비스")
public class BannerServiceTest {

    @Autowired
    BannerService bannerService;

    @MockBean
    AccountRepository accountRepository;
    @MockBean
    BannerRepository bannerRepository;
    @MockBean
    FileRepository fileRepository;


    @Nested
    @DisplayName("배너 생성 요청 테스트")
    class aboutBannerCreate {
        @Test
        @DisplayName("배너 생성 요청시, 정상적인 요청이라면, 배너 정보를 반환한다.")
        void givenValidInfo_whenCreatingBanner_thenCreatesBanner() {
            // Given
            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            BannerRequestDto mockRequestDto = BannerRequestDto.builder().title("title").fileId(1L).build();
            Banner mockBanner = getMockBanner(mockAdminAccount, mockRequestDto);

            given(accountRepository.getReferenceById(any())).willReturn(mockAdminAccount);
            given(fileRepository.findById(any())).willReturn(Optional.of(mock(File.class)));
            given(bannerRepository.save(any())).willReturn(mockBanner);

            // When
            BannerResponseDto responseDto = bannerService.create(mockAccountDto, mockRequestDto);

            // Then
            assertThat(responseDto)
                    .hasFieldOrProperty("id")
                    .hasFieldOrProperty("file")
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle());
        }

        @Test
        @DisplayName("배너 생성 요청시, 요청 이미지가 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidImageId_whenCreatingBanner_thenThrowsException() {
            // Given
            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            BannerRequestDto mockRequestDto = BannerRequestDto.builder().title("title").fileId(1L).build();
            Banner mockBanner = getMockBanner(mockAdminAccount, mockRequestDto);
            given(accountRepository.getReferenceById(any())).willReturn(mockAdminAccount);
            given(fileRepository.findById(any())).willReturn(Optional.empty());
            given(bannerRepository.save(any())).willReturn(mockBanner);

            // WHEN
            Throwable throwable = catchThrowable(
                    () -> bannerService.create(mockAccountDto, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_IMAGE_ID.getMessage());
        }
    }

    @Nested
    @DisplayName("배너 수정 요청 테스트")
    class aboutBannerUpdate {

        @Test
        @DisplayName("배너 수정 요청시, 정상적인 요청이라면, 배너 정보를 반환한다.")
        void givenValidInfo_whenUpdatingBanner_thenUpdatesBanner() {
            // Given
            Long requestBannerId = 1L;
            String changedTitle = "changed title";
            Long changedFileId = 2L;
            File mockFile = mock(File.class);
            Account mockAdminAccount = getMockAdminAccount();
            // 원본 내용
            BannerRequestDto createDto = BannerRequestDto.builder().title("title").fileId(1L).build();
            // 수정 내용
            BannerRequestDto mockRequestDto = BannerRequestDto.builder().title(changedTitle)
                    .fileId(changedFileId).build();
            Banner mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerRepository.findByIdWithFile(requestBannerId)).willReturn(Optional.of(mockBanner));
            given(fileRepository.findById(any())).willReturn(Optional.of(mockFile));
            given(mockFile.getId()).willReturn(1L);

            // When
            BannerResponseDto responseDto = bannerService.update(requestBannerId, mockRequestDto);

            // Then
            assertThat(responseDto).hasFieldOrPropertyWithValue("id", requestBannerId)
                    .hasFieldOrProperty("file")
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle());
        }

        //
        @Test
        @DisplayName("배너 수정 요청시, 요청 배너가 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidBannerId_whenUpdatingBanner_thenThrowsException() {
            // Given
            Long requestBannerId = 1L;
            String changedTitle = "changed title";
            Long changedFileId = 2L;
            File mockFile = mock(File.class);
            BannerRequestDto mockRequestDto = BannerRequestDto.builder().title(changedTitle)
                    .fileId(changedFileId).build();

            given(bannerRepository.findByIdWithFile(requestBannerId)).willReturn(Optional.empty());
            given(fileRepository.findById(any())).willReturn(Optional.of(mockFile));

            // WHEN
            Throwable throwable = catchThrowable(
                    () -> bannerService.update(requestBannerId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_POST_ID.getMessage());
            then(bannerRepository).should().findByIdWithFile(requestBannerId);
            then(fileRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("배너 수정 요청시, 요청 이미지가 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidImageId_whenUpdatingBanner_thenThrowsException() {
            // Given
            Long requestBannerId = 1L;
            String changedTitle = "changed title";
            Long changedFileId = 2L;
            Account mockAdminAccount = getMockAdminAccount();
            // 원본 내용
            BannerRequestDto createDto = BannerRequestDto.builder().title("title").fileId(1L).build();
            // 수정 내용
            BannerRequestDto mockRequestDto = BannerRequestDto.builder().title(changedTitle)
                    .fileId(changedFileId).build();
            Banner mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerRepository.findByIdWithFile(requestBannerId)).willReturn(Optional.of(mockBanner));
            given(fileRepository.findById(any())).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(
                    () -> bannerService.update(requestBannerId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_IMAGE_ID.getMessage());
            then(bannerRepository).should().findByIdWithFile(requestBannerId);
            then(fileRepository).should().findById(mockRequestDto.getFileId());
        }
    }

    @Nested
    @DisplayName("배너 삭제 요청 테스트")
    class aboutBannerDelete {

        @Test
        @DisplayName("배너 삭제 요청시, 정상적인 요청이라면, 배너를 삭제한다.")
        void givenValidInfo_whenDeletingBanner_thenDeletesBanner() {
            // GIVEN
            Long requestHeaderId = 1L;
            Account mockAdminAccount = getMockAdminAccount();
            BannerRequestDto createDto = BannerRequestDto.builder().title("title").fileId(1L).build();
            Banner mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerRepository.findById(requestHeaderId)).willReturn(Optional.of(mockBanner));

            // WHEN
            bannerService.delete(requestHeaderId);

            // THEN
            then(bannerRepository)
                    .should()
                    .delete(mockBanner);
        }

        @Test
        @DisplayName("배너 삭제 요청시, 요청 배너가 없다면, 예외를 발생시킨다.")
        void givenInvalidBannerId_whenDeletingBanner_thenThrowsException() {
            // GIVEN
            Long requestHeaderId = 1L;
            given(bannerRepository.findById(requestHeaderId)).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(() -> bannerService.delete(requestHeaderId));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.URI_NOT_FOUND.getMessage());
            then(bannerRepository).should().findById(requestHeaderId);
        }
    }

    private Account getMockAdminAccount() {
        Account mockAccount = Account.builder().nickname("testAdminNickname").password("testpw").email("tester@email.com")
                .role(AccountRole.ADMIN).build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 1L, Long.class);
        return mockAccount;
    }

    private Banner getMockBanner(Account author, BannerRequestDto requestDto) {
        File mockFile = File.builder().originalFileName("filename").uuidFileName("uuidname").build();
        ReflectionTestUtils.setField(mockFile, File.class, "id", requestDto.getFileId(), Long.class);

        Banner mockBanner = Banner.builder().author(author).title(requestDto.getTitle()).file(mockFile).build();
        ReflectionTestUtils.setField(mockBanner, Banner.class, "id", 1L, Long.class);
        return mockBanner;
    }
}
