package com.project.hunsu.controller;

import com.project.hunsu.model.entity.Ootd;
import com.project.hunsu.repository.UserRepository;
import com.project.hunsu.service.OotdService;
import com.project.hunsu.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin(origins = {"*"})
public class OotdController {
    private final OotdService ootdService;
    private final UserRepository userRepository;

    public OotdController(OotdService ootdService, UserRepository userRepository) {
        this.ootdService = ootdService;
        this.userRepository = userRepository;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    @GetMapping("/ootd/{sort}/{count}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    @ApiOperation(value = "Ootd 리스트 출력", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- sort(path): 최신순(0)/인기순(1) 정렬\n" +
            "- count(Path): 더보기 클릭된 횟수\n\n" +
            "Response\n" +
            "- OotdMainDTO\n" +
            "-- ootdIdx: ootd의 idx(글번호)\n" +
            "-- nickname: ootd글 작성자 닉네임\n" +
            "-- ootdContent: ootd글 내용\n" +
            "-- uid: ootd글 작성자의 uid(이미지에 필요)\n" +
            "-- ootdLike: ootd글 좋아요 카운트\n" +
            "-- hashtagList(String 배열): ootd글 해시태그 리스트\n")
    public List<OotdMainDTO> ootdSortedList(@RequestHeader("X-AUTH-ACCESS") String jwtToken,  @PathVariable int sort, @PathVariable int count) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.SortByRecentOrPopularity(sort, count);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- ootdIdx(path): 선택된 ootd의 idx(글번호)\n\n" +
            "Response\n" +
            "- ootdIdx: ootd의 idx(글번호)\n" +
            "- content: ootd글 내용\n" +
            "- likeCount: ootd글 좋아요 카운트\n" +
            "- uid: ootd글 작성자의 uid(이미지에 필요)" +
            "- isUpdated: ootd글 수정 여부\n" +
            "- writeDate: ootd글 작성 날짜\n" +
            "- nickname: ootd글 작성자 닉네임\n" +
            "- hashtagList(String 배열): ootd글 해시태그 리스트\n" +
            "- likeChk: 사용자(페이지 보는사람)의 해당글에 대한 좋아요 활성화 여부\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public OotdDetailDTO detailOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable("ootdIdx") Long ootdIdx) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        OotdDetailDTO ootdDetailDTO = ootdService.SpecificOotd(ootdIdx, nickname);
        return ootdDetailDTO;
    }


    @PutMapping("/ootd/modi")
    @Transactional
    @ApiOperation(value = "Ootd 글수정", notes = "\n\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- ootdIdx: ootd의 idx(글번호)\n" +
            "-- hashtagList(String 배열): 수정할 ootd글 해시태그 리스트\n" +
            "-- content: 수정할 ootd글 내용\n\n" +
            "Response \n" +
            "- state: 수정 성공(success) 혹은 실패(fail) 여부\n")
    public String updateOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdUpdateDTO ootdUpdateDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdUpdateDTO.getOotdIdx());
        String state = "";
        if (ootd != null) {
            ootdService.updateHashtag(ootdUpdateDTO);
            state = "success";
        } else
            state = "fail";
        return state;
    }

    @PutMapping("/ootd/del")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- ootdIdx(param): ootd의 idx(글번호)\n\n" +
            "Response\n" +
            "- state: 수정 성공(success) 혹은 실패(fail) 여부")
    public String deleteOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long ootdIdx) {
        boolean flag = ootdService.deleteOotd(ootdIdx); //글 비활성화
        String state = "";
        if (flag)
            state = "success";
        else
            state = "fail";
        return state;
    }

    /**
     * 누가 좋아요 했는지 정보를 사용자마다 보이는 화면이 다르니깐 해당 nickname의 유저가 좋아요한 글의 리스트를 보내준다.
     */
    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- nickname: 사용자(좋아요 누르는 사람)의 닉네임\n" +
            "-- ootdIdx: ootd의 idx(글번호)\n\n" +
            "Response\n" +
            "- chk: 좋아요 활성화(true) 비활성화(false)") // 성공
    public Boolean ootdLike(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdLikeDTO ootdLikeDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeDTO.getOotdIdx());
        List<OotdLikeDTO> ootdLikeDTOList = null;
        boolean chk = false;
        if (ootd != null) {
            chk = ootdService.ootdLike(ootdLikeDTO);
        }
        return chk;
    }


    @GetMapping("/ootd/hashtag/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 클릭)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- hashtag(path): 검색할 해시태그\n\n" +
            "Response\n" +
            "- ootdMainDTOList(OotdMainDTO 배열)\n" +
            "-- ootdIdx: ootd의 idx(글번호)\n" +
            "-- nickname: ootd글 작성자 닉네임\n" +
            "-- ootdContent: ootd글 내용\n" +
            "-- uid: ootd글 작성자의 uid(이미지에 필요)\n" +
            "-- ootdLike: ootd글 좋아요 카운트\n" +
            "-- hashtagList(String 배열): ootd글 해시태그 리스트\n")
    public List<OotdMainDTO> hashtagSearchByClick(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagClick(hashtag);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/hashtag/search/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 입력)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- hashtag(path): 검색할 해시태그 문자열\n\n" +
            "Response\n" +
            "- ootdMainDTOList(OotdMainDTO 배열)\n" +
            "-- ootdIdx: ootd의 idx(글번호)\n" +
            "-- nickname: ootd글 작성자 닉네임\n" +
            "-- ootdContent: ootd글 내용\n" +
            "-- uid: ootd글 작성자의 uid(이미지에 필요)\n" +
            "-- ootdLike: ootd글 좋아요 카운트\n" +
            "-- hashtagList(String 배열): ootd글 해시태그 리스트\n")
    public List<OotdMainDTO> hashtagSearchByInput(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagInput(hashtag);
        return ootdMainDTOList;
    }

    @Transactional(rollbackOn = RuntimeException.class)
    @PostMapping("/ootd") // 내용, 닉네임 같은것 입력하면 ootd에만 들어가고 해시태그 안들어가는 오류발생
    @ApiOperation(value = "Ootd글 작성", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- nickName: ootd글 작성자 닉네임\n" +
            "-- content: ootd글 내용\n" +
            "-- hashtagList(String 배열): ootd글 해시태그 리스트\n\n" +
            "Response\n" +
            "- ootdIdx: ootd의 idx(글번호)\n" +
            "- content: ootd글 내용\n" +
            "- likeCount: ootd글 좋아요 카운트\n" +
            "- uid: ootd글 작성자의 uid(이미지에 필요)" +
            "- isUpdated: ootd글 수정 여부\n" +
            "- writeDate: ootd글 작성 날짜\n" +
            "- nickname: ootd글 작성자 닉네임\n" +
            "- hashtagList(String 배열): ootd글 해시태그 리스트\n" +
            "- likeChk: 사용자(페이지 보는사람)의 해당글에 대한 좋아요 활성화 여부\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx(글번호)\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public OotdDetailDTO writeOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdWriteDTO ootdWriteDTO) {
        OotdDetailDTO ootdDetailDTO = ootdService.writeOotd(ootdWriteDTO);
        return ootdDetailDTO;
    }

    @PostMapping("/ootd/reply")
    @ApiOperation(value = "Ootd글 댓글 작성", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- ootdIdx: 댓글을 작성할 ootd글 번호\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- content: 댓글 내용\n\n" +
            "Response\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx(글번호)\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public List<OotdReplyDTO> ootdReplyWrite(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdReplyDTO ootdreplyDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = null;
        ootdReplyDTOList = ootdService.writeReply(ootdreplyDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply/modi")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 수정", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- replyIdx(댓글 번호)\n" +
            "-- content(댓글 내용)\n\n" +
            "Response\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx(글번호)\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public List<OotdReplyDTO> ootdReplyUpdate(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.updateReply(ootdReplyUpdateDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply/del")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 삭제", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- replyIdx(param): 댓글의 idx(글번호)\n\n" +
            "Response\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx(글번호)\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public List<OotdReplyDTO> ootdReplyDelete(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long replyIdx) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.deleteReply(replyIdx);
        return ootdReplyDTOList;
    }

    @Transactional
    @PutMapping("/ootd/reply/like")
    @ApiOperation(value = "Ootd 댓글에 대한 좋아요", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- replyIdx(param): 댓글의 idx(글번호)\n\n" +
            "Response\n" +
            "- ootdReplyDTOList(OotdReplyDTO 배열): 해당 글의 댓글 리스트\n" +
            "-- replyIdx: 댓글의 idx(글번호)\n" +
            "-- ootdIdx: 댓글이 게시된 ootd글 idx(글번호)\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- likeCount: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- isDeleted: 댓글의 삭제 여부\n")
    public List<OotdReplyDTO> ootdReplyLike(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long replyIdx) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.ootdReplyLike(replyIdx, nickname);
        return ootdReplyDTOList;
    }
}