package com.project.hunsu.controller;

import com.project.hunsu.repository.UserRepository;
import com.project.hunsu.service.WearService;
import com.project.hunsu.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
public class WearController {
    private final WearService wearService;
    private final UserRepository userRepository;
    public WearController(WearService wearService, UserRepository userRepository) {
        this.wearService = wearService;
        this.userRepository = userRepository;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    //뭘 입을까 목록(최신순 정렬)
    @GetMapping("/wear/{page}")
    @ApiOperation(value = "뭘 입을까 메인", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- page(path): 페이지 넘버\n\n" +
            "Response\n" +
            "- count: wear글에 대한 좋아요 카운트\n" +
            "- wearMainDTOList(WearMainDTO 배열)\n" +
            "-- wear_idx: 뭘입을까 idx(글번호)\n" +
            "-- nickname: 뭘입을까 작성자 닉네임\n" +
            "-- title: 뭘입을까 제목\n" +
            "-- uid: 뭘입을까 작성자의 uid(이미지에 필요)\n" +
            "-- voteActivated: 뭘입을까 투표 활성화(true) 비활성화(false) 여부\n")
    public WearMainTotalDTO wearMain(@PathVariable Integer page ,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        WearMainTotalDTO wearMainTotalDTO = wearService.sortByRecent(page);
        return wearMainTotalDTO;
    }

    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- title: 뭘입을까 제목\n" +
            "-- content: 뭘입을까 내용\n" +
            "-- nickname: 뭘입을까 작성자의 닉네임\n" +
            "-- endtime: 뭘입을까 투표 종료 시간\n" +
            "-- num: 투표용 사진 갯수\n\n" +
            "Response\n" +
            "- idx: 작성된 뭘입을까 idx(글번호), 0은 작성 실패\n")
    public long insertWear(@RequestBody WearDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        long idx = 0;
        if(request.getNickname() != null){
            idx = wearService.insertWear(request);
        }
        return idx;
    }

    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/{wear_idx}")
    @ApiOperation(value = "뭘 입을까 디테일", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- wear_idx(path): 뭘입을까 idx(글번호)\n\n" +
            "Response\n" +
            "- wear_idx: 뭘입을까 idx(글번호)\n" +
            "- title: 뭘입을까 제목\n" +
            "- content: 뭘입을까 내용\n" +
            "- nickname: 뭘입을까 작성자 닉네임\n" +
            "- write_date: 뭘입을까 작성날짜\n" +
            "- end_time: 뭘입을까 투표 종료 시간\n" +
            "- voteActivated: 뭘입을까 투표 활성화(true) 비활성화(false) 여부\n" +
            "- replyList(WearReplyDTO 배열)\n" +
            "-- idx: 댓글의 idx\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- count: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- flag: 댓글의 삭제 여부\n" +
            "- voteList(VoteDTO 배열)\n" +
            "-- idx: 투표 항목 idx\n" +
            "-- count: 투표 항목의 투표 카운트\n" +
            "-- choice: 사용자(페이지 보는사람)의 해당 항목 선택 여부")
    public WearDetailDTO detailWear(@PathVariable Long wear_idx, @RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        WearDetailDTO wearDetailDTO = wearService.detailWear(wear_idx, nickname);
        return wearDetailDTO;
    }

    //wear_idx 필요
    @PutMapping(value = "/wear")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- wear_idx(param): 뭘입을까 idx(글번호)\n" +
            "Response(x)")
    public void deleteWear(@RequestParam Long wear_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        wearService.deleteWear(wear_idx);
    }

    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- content: 댓글 내용\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- nickname: 댓글 작성자\n" +
            "-- wear_idx: 댓글이 게시된 뭘입을까 idx(글번호)\n\n" +
            "Response\n" +
            "- replyDTOList(WearReplyDTO 배열)\n" +
            "-- idx: 댓글의 idx\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- count: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- flag: 댓글의 삭제 여부\n")
    public List<WearReplyDTO> insertReply(@RequestBody WearReplyDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.insertReply(request);

        return replyDTOList;
    }

    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply/modi")
    @Transactional
    @ApiOperation(value = "댓글 수정", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- idx: 댓글 idx(글번호)\n" +
            "-- content: 댓글 내용\n\n" +
            "Response\n" +
            "- replyDTOList(WearReplyDTO 배열)\n" +
            "-- idx: 댓글의 idx\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- count: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- flag: 댓글의 삭제 여부\n")
    public List<WearReplyDTO> updateReply(@RequestBody WearReplyDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.modifyReply(request);

        return replyDTOList;
    }

    //reply_idx 필요
    @PutMapping(value = "/wear/reply/del")
    @Transactional
    @ApiOperation(value = "댓글 삭제", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- idx(param): 댓글 idx(글번호)\n" +
            "Response\n" +
            "- replyDTOList(WearReplyDTO 배열)\n" +
            "-- idx: 댓글의 idx\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- count: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- flag: 댓글의 삭제 여부\n")
    public List<WearReplyDTO> deleteReply(@RequestParam Long idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.deleteReply(idx);

        return replyDTOList;
    }

    //reply_idx, nickname 필요
    @PutMapping("/wear/reply/like")
    @Transactional
    @ApiOperation(value = "댓글 좋아요/좋아요취소", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- reply_idx(param): 댓글 idx(글번호)\n" +
            "Response\n" +
            "- replyDTOList(WearReplyDTO 배열)\n" +
            "-- idx: 댓글의 idx\n" +
            "-- nickname: 댓글 작성자 닉네임\n" +
            "-- uid: 댓글 작성자 uid\n" +
            "-- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-- write_date: 댓글 작성 날짜\n" +
            "-- content: 댓글 내용\n" +
            "-- groupNum: 댓글의 그룹 넘버(어떤 글의 댓글, 대댓글인지 그룹 구분)\n" +
            "-- count: 댓글 좋아요 카운트\n" +
            "-- like: 사용자(페이지 보는사람)의 해당댓글에 대한 좋아요 활성화 여부\n" +
            "-- flag: 댓글의 삭제 여부\n")
    public List<WearReplyDTO> likeReply(@RequestParam Long reply_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        List<WearReplyDTO> replyDTOList = wearService.replyLike(reply_idx, nickname);

        return replyDTOList;
    }

    //vote_item_idx, nickname 필요
    @PutMapping(value = "/wear/reply/vote")
    @Transactional
    @ApiOperation(value = "투표/투표취소 (O)", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- vote_item_idx(param): 투표 항목의 idx\n" +
            "Response\n" +
            "- voteList(VoteDTO 배열)\n" +
            "-- idx: 투표 항목 idx\n" +
            "-- count: 투표 항목의 투표 카운트\n" +
            "-- choice: 사용자(페이지 보는사람)의 해당 항목 선택 여부")
    public List<VoteDTO> voteChoice(@RequestParam Long vote_item_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        List<VoteDTO> voteDTOList = wearService.voteChoice(vote_item_idx, nickname);

        return voteDTOList;
    }

}