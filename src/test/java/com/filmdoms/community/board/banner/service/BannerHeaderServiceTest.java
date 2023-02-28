package com.filmdoms.community.board.banner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.banner.data.dto.BannerDto;
import com.filmdoms.community.board.banner.data.dto.request.BannerInfoRequestDto;
import com.filmdoms.community.board.banner.data.entity.BannerHeader;
import com.filmdoms.community.board.banner.repository.BannerHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest(controllers = BannerService.class, excludeAutoConfiguration = SecurityAutoConfiguration.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("비즈니스 로직 - 배너 서비스")
public class BannerHeaderServiceTest {

    @Autowired
    BannerService bannerService;

    @MockBean
    AccountRepository accountRepository;
    @MockBean
    BannerHeaderRepository bannerHeaderRepository;
    @MockBean
    ImageFileRepository imageFileRepository;
    @MockBean
    ImageFileService imageFileService;


    @Nested
    @DisplayName("배너 생성 요청 테스트")
    class aboutBannerCreate {
        @Test
        @DisplayName("배너 생성 요청시, 정상적인 요청이라면, 배너 정보를 반환한다.")
        void givenValidInfo_whenCreatingBanner_thenCreatesBanner() {
            // Given
            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            BannerInfoRequestDto mockRequestDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, mockRequestDto);
            given(accountRepository.getReferenceById(any())).willReturn(mockAdminAccount);
            given(imageFileRepository.getReferenceById(any())).willReturn(mock(ImageFile.class));
            given(bannerHeaderRepository.save(any())).willReturn(mockBanner);

            // When
            BannerDto bannerDto = bannerService.create(mockAccountDto, mockRequestDto);

            // Then
            assertThat(bannerDto).hasFieldOrProperty("id").hasFieldOrProperty("imageUrl")
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle());
        }

        @Test
        @DisplayName("배너 생성 요청시, 이미 매핑된 이미지ID를 받으면, 예외를 발생시킨다.")
        void givenAlreadyMappedImageId_whenCreatingBanner_thenThrowsException() {
            // Given
            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            BannerInfoRequestDto mockRequestDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, mockRequestDto);
            given(accountRepository.getReferenceById(any())).willReturn(mockAdminAccount);
            given(imageFileRepository.getReferenceById(any())).willReturn(mock(ImageFile.class));
            given(bannerHeaderRepository.save(any())).willReturn(mockBanner);
            doThrow(new ApplicationException(ErrorCode.IMAGE_BELONG_TO_OTHER_POST)).when(imageFileService)
                    .setImageContent((Long) any(), any());
            
            // WHEN
            Throwable throwable = catchThrowable(
                    () -> bannerService.create(mockAccountDto, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.IMAGE_BELONG_TO_OTHER_POST.getMessage());
        }
    }

    @Nested
    @DisplayName("배너 수정 요청 테스트")
    class aboutBannerUpdate {

        @Test
        @DisplayName("배너 수정 요청시, 정상적인 요청이라면, 배너 정보를 반환한다.")
        void givenValidInfo_whenUpdatingBanner_thenUpdatesBanner() {
            // Given
            Long requestHeaderId = 1L;
            String titleToUpdate = "changed title";
            Long mainImageToUpdate = 2L;
            ImageFile mockImageFile = mock(ImageFile.class);

            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            // 원본 내용
            BannerInfoRequestDto createDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            // 수정 내용
            BannerInfoRequestDto mockRequestDto = BannerInfoRequestDto.builder().title(titleToUpdate)
                    .mainImageId(mainImageToUpdate).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerHeaderRepository.findById(requestHeaderId)).willReturn(Optional.of(mockBanner));
            given(imageFileRepository.findById(mockRequestDto.getMainImageId())).willReturn(Optional.of(mockImageFile));
            given(bannerHeaderRepository.save(any())).willReturn(mockBanner);
            given(mockImageFile.getBoardContent()).willReturn(mockBanner.getBoardContent());

            // When
            BannerDto bannerDto = bannerService.update(mockAccountDto, requestHeaderId, mockRequestDto);

            // Then
            assertThat(bannerDto).hasFieldOrPropertyWithValue("id", requestHeaderId).hasFieldOrProperty("imageUrl")
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle());
        }

        @Test
        @DisplayName("배너 수정 요청시, 요청 이미지가 없다면, 예외를 발생시킨다.")
        void givenInvalidImageId_whenUpdatingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;
            String titleToUpdate = "changed title";
            Long mainImageToUpdate = 2L;

            Account mockAdminAccount = getMockAdminAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAdminAccount);
            // 원본 내용
            BannerInfoRequestDto createDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            // 수정 내용
            BannerInfoRequestDto mockRequestDto = BannerInfoRequestDto.builder().title(titleToUpdate)
                    .mainImageId(mainImageToUpdate).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerHeaderRepository.findById(requestHeaderId)).willReturn(Optional.of(mockBanner));
            given(imageFileRepository.findById(mockRequestDto.getMainImageId())).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(
                    () -> bannerService.update(mockAccountDto, requestHeaderId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.NO_IMAGE_ERROR.getMessage());
            then(bannerHeaderRepository).should().findById(requestHeaderId);
            then(imageFileRepository).should().findById(mockRequestDto.getMainImageId());
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
            BannerInfoRequestDto createDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerHeaderRepository.findById(requestHeaderId)).willReturn(Optional.of(mockBanner));

            // WHEN
            bannerService.delete(requestHeaderId);

            // THEN
            then(bannerHeaderRepository)
                    .should()
                    .delete(mockBanner);
        }

        @Test
        @DisplayName("배너 삭제 요청시, 요청 배너가 없다면, 예외를 발생시킨다.")
        void givenInvalidBannerId_whenDeletingBanner_thenThrowsException() {
            // GIVEN
            Long requestHeaderId = 1L;
            Account mockAdminAccount = getMockAdminAccount();
            BannerInfoRequestDto createDto = BannerInfoRequestDto.builder().title("title").mainImageId(1L).build();
            BannerHeader mockBanner = getMockBanner(mockAdminAccount, createDto);

            given(bannerHeaderRepository.findById(requestHeaderId)).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(() -> bannerService.delete(requestHeaderId));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.URI_NOT_FOUND.getMessage());
            then(bannerHeaderRepository).should().findById(requestHeaderId);
        }
    }

    private Account getMockAdminAccount() {
        Account mockAccount = Account.builder().username("testAdmin").password("testpw").email("tester@email.com")
                .role(AccountRole.ADMIN).build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 1L, Long.class);
        return mockAccount;
    }

    private BannerHeader getMockBanner(Account author, BannerInfoRequestDto requestDto) {
        BoardContent mockContent = BoardContent.builder().build();
        ImageFile mockImageFile = ImageFile.builder().boardContent(mockContent).build();
        ReflectionTestUtils.setField(mockImageFile, ImageFile.class, "id", requestDto.getMainImageId(), Long.class);
        BannerHeader mockHeader = BannerHeader.builder().author(author).title(requestDto.getTitle())
                .content(mockContent).mainImage(mockImageFile).build();
        ReflectionTestUtils.setField(mockHeader, PostHeader.class, "id", 1L, Long.class);
        ReflectionTestUtils.setField(mockContent, BoardContent.class, "imageFiles",
                new HashSet<>(Set.of(mockImageFile)), Set.class);
        return mockHeader;
    }
}
