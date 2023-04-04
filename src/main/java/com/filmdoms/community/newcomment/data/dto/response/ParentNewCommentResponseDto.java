package com.filmdoms.community.newcomment.data.dto.response;

import com.filmdoms.community.board.comment.data.dto.CommentResponseDto;
import com.filmdoms.community.board.comment.data.dto.ParentCommentResponseDto;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * 게시글 상세 페이지에서 댓글 정보를 주기 위한 응답 DTO, 부모 댓글 용도
 * NewComment 엔티티 리스트를 받아 DTO 계층 구조로 변환하는 convert 정적 메서드를 가짐
 */
@Getter
public class ParentNewCommentResponseDto extends NewCommentResponseDto {

    private List<NewCommentResponseDto> childComments;

    private ParentNewCommentResponseDto(NewComment comment, List<NewCommentResponseDto> childComments) {
        super(comment);
        this.childComments = childComments;
    }

    public static ParentNewCommentResponseDto from(NewComment comment, List<NewCommentResponseDto> childComments) {
        return new ParentNewCommentResponseDto(comment, childComments);
    }

    public static List<ParentNewCommentResponseDto> convert(List<NewComment> comments) { //댓글을 생성순으로 정렬한 후 부모 댓글에 자식 댓글을 연결해서 반환하는 메서드
        Map<Boolean, List<NewComment>> isParentComment = comments.stream()
                .sorted(Comparator.comparing(NewComment::getDateCreated)) //생성순 정렬
                .collect(partitioningBy(comment -> comment.getParentComment() == null, toList())); //부모 댓글 여부로 분류
        //isParentComment={true=부모댓글 리스트(생성순 정렬), false=자식댓글 리스트(생성순 정렬)}

        Map<NewComment, List<NewCommentResponseDto>> parentToChildren = isParentComment.get(false).stream()
                .collect(groupingBy(NewComment::getParentComment, mapping(NewCommentResponseDto::from, toList()))); //자식 댓글을 부모 댓글을 키로 그룹화하고, DTO로 변환
        //parentToChildren={ 부모댓글1=부모댓글1의 자식댓글 DTO 리스트, 부모댓글2=부모댓글2의 자식댓글 DTO 리스트, ...} (자식댓글이 없는 부모댓글은 값으로 null을 가짐)

        return isParentComment.get(true).stream()
                .map(comment -> ParentNewCommentResponseDto.from(comment, parentToChildren.get(comment))) //부모댓글, 자식댓글 DTO리스트를 이용하여 부모댓글 DTO로 변환
                .collect(toList()); //최종적으로 부모댓글 리스트가 완성되고, 각각의 부모댓글은 자식댓글 DTO 리스트를 내부에 가지고 있음 (없다면 childComments 필드가 null로 설정됨)
    }
}