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
    @ApiOperation(value = "마이페이지 (프론트 수정 필요)", notes = "\n\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- sort(path): 최신순(0)/인기순(1) 정렬" +
            "- count(Path): 더보기 클릭된 횟수\n" +
            "Response\n" +
            "- 모든 ootd글을 정렬된 상태로 리턴\n")
    public List<OotdMainDTO> ootdSortedList(@RequestHeader("X-AUTH-ACCESS") String jwtToken,  @PathVariable int sort, @PathVariable int count) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.SortByRecentOrPopularity(sort, count);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "상세페이지 (프론트 수정 필요)", notes = "\n\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- ootdIdx(path): 보고자하는 글의 idx" +
            "Response \n" +
            "- ootdIdx: ootd글번호\n" +
            "- content: 해당 ootd글 내용\n" +
            "- likeCount: ootd글 좋아요 개수\n" +
            "- isUpdated: ootd글의 수정 여부\n" +
            "- writeDate: 글 작성 날짜\n" +
            "- nickname\n" +
            "- hashtagList: 해당 글의 해시태그들\n" +
            "- ootdReplyList: 해당 글의 댓글 리스트\n" +
            "- likeChk: 상세페이지에 들어간 유저가 해당글을 좋아요 눌렀는지 여부")
    public OotdDetailDTO detailOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable("ootdIdx") Long ootdIdx) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        OotdDetailDTO ootdDetailDTO = ootdService.SpecificOotd(ootdIdx, nickname);
        return ootdDetailDTO;
    }


    @PutMapping("/ootd/modi")
    @Transactional
    @ApiOperation(value = "Ootd 글수정 (프론트 수정 필요)", notes = "\n\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- ootdIdx: 글 번호\n" +
            "- hashtag: 새로 수정할 해시태그\n" +
            "- content: 새로 수정할 글 내용\n" +
            "Response \n" +
            "- 수정 성공 혹은 실패 여부(success or fail)")
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
    @ApiOperation(value = "Ootd 글삭제(비활성화) (프론트 수정 필요: uri 수정 + delete->put)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- ootdIdx: 글 번호      --수정\n" +
            "Response\n" +
            "- 글 비활성화 성공여부(success or fail)")
    public String deleteOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long ootdIdx) {
        boolean flag = ootdService.deleteOotd(ootdIdx); //글 비활성화
        if (flag)
            return "success";
        else
            return "fail";
    }

    /**
     * 누가 좋아요 했는지 정보를 사용자마다 보이는 화면이 다르니깐 해당 nickname의 유저가 좋아요한 글의 리스트를 보내준다.
     */
    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요 (프론트 수정 필요)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- nickname: 닉네임\n" +
            "- ootdIdx: 글 번호\n" +
            "Response\n" +
            "- 좋아요 추가 - true  좋아요 해제 - false") // 성공
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
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 클릭) (프론트 수정 필요)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- hashtag(path): 검색할 해시태그\n" +
            "Response\n" +
            "- List<OotdMainDTO>(해시태그가 포함된 모든 ootd글)")
    public List<OotdMainDTO> hashtagSearchByClick(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagClick(hashtag);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/hashtag/search/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 입력) (프론트 수정 필요)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- hashtag(path): 검색할 해시태그 문자열\n" +
            "Response\n" +
            "- List<OotdMainDTO>(해시태그 문자열이 포함된 모든 ootd글)")
    public List<OotdMainDTO> hashtagSearchByInput(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagInput(hashtag);
        return ootdMainDTOList;
    }

    @Transactional(rollbackOn = RuntimeException.class)
    @PostMapping("/ootd") // 내용, 닉네임 같은것 입력하면 ootd에만 들어가고 해시태그 안들어가는 오류발생
    @ApiOperation(value = "Ootd글 작성 (프론트 수정 필요)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- nickName(닉네임)\n" +
            "- content(작성할 내용)\n" +
            "- hashtag(작성할 해시태그)\n" +
            "Response\n" +
            "- 작성한 ootd글 정보(ootdIdx,hastagList,writeDate,content,nickname,likeChk,likecount)")
    public OotdDetailDTO writeOotd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdWriteDTO ootdWriteDTO) {
        OotdDetailDTO ootdDetailDTO = ootdService.writeOotd(ootdWriteDTO);
        return ootdDetailDTO;
    }

    @PostMapping("/ootd/reply")
    @ApiOperation(value = "Ootd글 댓글 작성 (프론트 수정 필요)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- replyIdx: 작성될 댓글 번호 - null\n" +
            "- ootdIdx: 댓글을 작성할 ootd글 번호\n" +
            "- nickname: 댓글 작성자 닉네임\n" +
            "- content: 댓글 내용\n" +
            "- depth: 댓글혹은대댓글여부 - null\n" +
            "- write_date: 작성날짜 - null\n" +
            "- like: 현재 유저가 좋아요 눌렀지의 여부 - null\n" +
            "- groupNum: 어느 글에 종속된 댓글인지의 그룹 - null\n" +
            "- likeCount: 댓글 좋아요 개수 - null\n" +
            "- isDeleted: 댓글 삭제 여부 - null\n" +
            "Response\n" +
            "- 해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyWrite(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdReplyDTO ootdreplyDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = null;
        ootdReplyDTOList = ootdService.writeReply(ootdreplyDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply/modi")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 수정 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- replyIdx(댓글 번호)\n" +
            "- content(댓글 내용)\n" +
            "Response\n" +
            "- 해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyUpdate(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @Valid @RequestBody OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.updateReply(ootdReplyUpdateDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply/del")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 삭제 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- replyIdx: 댓글 번호      --수정(path -> requestbody)\n" +
            "Response\n" +
            "- 해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyDelete(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long replyIdx) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.deleteReply(replyIdx);
        return ootdReplyDTOList;
    }

    @Transactional
    @PutMapping("/ootd/reply/like/{replyIdx}")
    @ApiOperation(value = "Ootd 댓글에 대한 좋아요 설정 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- replyIdx: 댓글 번호      --수정(path -> requestbody)\n" +
            "Response\n" +
            "- 해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyLike(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam Long replyIdx) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.ootdReplyLike(replyIdx, nickname);
        return ootdReplyDTOList;
    }
}