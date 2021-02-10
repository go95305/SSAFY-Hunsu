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
    @ApiOperation(value = "뭘 입을까 메인 (O)", notes = "Parameter\n" +
            "- page(path): 페이지 넘버\n" +
            "- jwtToken(RequestHeader)\n" +
            "Response(DTO)\n" +
            "- wear_list(wear): 뭘 입을까 리스트\n" +
            "- title: 제목\n" +
            "- nickname: 작성자\n" +
            "- wear_idx: 뭘입을까 idx\n" +
            "- voteActivated: 투표 활성화 여부(true or false)\n")
    public WearMainTotalDTO wearMain(@PathVariable Integer page ,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        WearMainTotalDTO wearMainTotalDTO = wearService.sortByRecent(page);
        return wearMainTotalDTO;
    }

    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성 (O)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "[WearDTO]" +
            "- content: 내용\n" +
            "- nickname: 작성자\n" +
            "- num: 투표용 사진 개수\n" +
            "- title: 제목\n" +
            "- endtime: 투표 마감 시간(투표 비활성화(num == 0) 시 선택 필요 없음)\n" +
            "Response" +
            "")
    public long insertWear(@RequestBody WearDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        return wearService.insertWear(request);
    }

    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/{wear_idx}")
    @ApiOperation(value = "뭘 입을까 디테일 (O)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- wear_idx(path): 뭘입을까 idx\n" +
            "Response\n" +
            "-wear_idx: 뭘입을까 idx" +
            "-title: 제목\n" +
            "-content: 내용\n" +
            "-nickname: 작성자\n" +
            "-write_date: 작성날짜\n" +
            "-end_time: 투표종료시간\n" +
            "-vote_activated: 투표 활성화 여부(true or false)\n" +
            "-replyList: 댓글 리스트\n" +
            "-----idx: 댓글 idx\n" +
            "-----nickname: 댓글 작성자\n" +
            "-----depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-----write_date: 작성 날짜\n" +
            "-----content: 댓글 내용\n" +
            "-----groupNum: 댓글 그룹으로 같은 숫자를 가졌으면 해당 idx(=groupNum)에 대한 댓글, 대댓글\n" +
            "-----count: 해당 댓글에 대한 좋아요 수\n" +
            "-----like: 좋아요 활성화 여부(true or false)\n" +
            "-----flag: 댓글이 유효 여부(true or false(삭제))\n" +
            "-voteList: 투표 리스트\n" +
            "-----voteItem_idx: 투표 항목 idx\n" +
            "-----count: 투표 항목의 투표 수\n" +
            "-----choice: 해당 항목을 선택 했는지 여부(true or false)")
    public WearDetailDTO detailWear(@PathVariable Long wear_idx, @RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        WearDetailDTO wereDetail = wearService.detailWear(wear_idx, nickname);
        return wereDetail;
    }

    //wear_idx 필요
    @PutMapping(value = "/wear")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제 (O)", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- wear_idx(RequestBody) \n" +
            "Response(x)")
    public void deleteWear(@RequestBody Long wear_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        wearService.deleteWear(wear_idx);
    }

    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성 (O)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "[WearReplyDTO]\n" +
            "- content: 댓글 내용\n" +
            "- depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "- groupNum: 댓글 그룹으로 depth가 0이면 0, depth가 1이면 상위댓글의 groupNum\n" +
            "- nickname: 댓글 작성자\n" +
            "- wear_idx: 뭘입을까 idx\n" +
            "Response\n" +
            "-replyList: 댓글 리스트\n" +
            "-----idx: 댓글 idx\n" +
            "-----nickname: 댓글 작성자\n" +
            "-----depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-----write_date: 작성 날짜\n" +
            "-----content: 댓글 내용\n" +
            "-----groupNum: 댓글 그룹으로 같은 숫자를 가졌으면 해당 idx(=groupNum)에 대한 댓글, 대댓글\n" +
            "-----count: 해당 댓글에 대한 좋아요 수\n" +
            "-----like: 좋아요 활성화 여부(true or false)\n" +
            "-----flag: 댓글이 유효 여부(true or false(삭제))")
    public List<WearReplyDTO> insertReply(@RequestBody WearReplyDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.insertReply(request);

        return replyDTOList;
    }

    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply/modi")
    @Transactional
    @ApiOperation(value = "댓글 수정 (O)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "[WearReplyDTO]\n" +
            "-reply_idx: 댓글 idx\n" +
            "-content: 댓글 내용\n" +
            "Response\n" +
            "-replyList: 댓글 리스트\n" +
            "-----idx: 댓글 idx\n" +
            "-----nickname: 댓글 작성자\n" +
            "-----depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-----write_date: 작성 날짜\n" +
            "-----content: 댓글 내용\n" +
            "-----groupNum: 댓글 그룹으로 같은 숫자를 가졌으면 해당 idx(=groupNum)에 대한 댓글, 대댓글\n" +
            "-----count: 해당 댓글에 대한 좋아요 수\n" +
            "-----like: 좋아요 활성화 여부(true or false)\n" +
            "-----flag: 댓글이 유효 여부(true or false(삭제))")
    public List<WearReplyDTO> updateReply(@RequestBody WearReplyDTO request,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.modifyReply(request);

        return replyDTOList;
    }

    //reply_idx 필요
    @PutMapping(value = "/wear/reply/del")
    @Transactional
    @ApiOperation(value = "댓글 삭제 (O)", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- idx: 댓글 idx (RequestBody)\n" +
            "Response\n" +
            "-replyList: 댓글 리스트\n" +
            "-----idx: 댓글 idx\n" +
            "-----nickname: 댓글 작성자\n" +
            "-----depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-----write_date: 작성 날짜\n" +
            "-----content: 댓글 내용\n" +
            "-----groupNum: 댓글 그룹으로 같은 숫자를 가졌으면 해당 idx(=groupNum)에 대한 댓글, 대댓글\n" +
            "-----count: 해당 댓글에 대한 좋아요 수\n" +
            "-----like: 좋아요 활성화 여부(true or false)\n" +
            "-----flag: 댓글이 유효 여부(true or false(삭제))")
    public List<WearReplyDTO> deleteReply(@RequestBody Long idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        List<WearReplyDTO> replyDTOList = wearService.deleteReply(idx);

        return replyDTOList;
    }

    //reply_idx, nickname 필요
    @PutMapping("/wear/reply/like")
    @Transactional
    @ApiOperation(value = "댓글 좋아요/좋아요취소 (O)", notes = "PathVariable ->RequestBody\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- reply_idx: 댓글 idx(RequestBody)\n" +
            "Response\n" +
            "-replyList: 댓글 리스트\n" +
            "-----idx: 댓글 idx\n" +
            "-----nickname: 댓글 작성자\n" +
            "-----depth: 댓글 종류(댓글-0, 대댓글-1)\n" +
            "-----write_date: 작성 날짜\n" +
            "-----content: 댓글 내용\n" +
            "-----groupNum: 댓글 그룹으로 같은 숫자를 가졌으면 해당 idx(=groupNum)에 대한 댓글, 대댓글\n" +
            "-----count: 해당 댓글에 대한 좋아요 수\n" +
            "-----like: 좋아요 활성화 여부(true or false)\n" +
            "-----flag: 댓글이 유효 여부(true or false(삭제))")
    public List<WearReplyDTO> likeReply(@RequestBody Long reply_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
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
            "- vote_item_idx: 투표 항목의 idx(RequestBody) \n" +
            "Response\n" +
            "-voteList: 투표 리스트\n" +
            "-----idx: 투표 항목의 idx\n" +
            "-----count: 투표 항목의 투표 수\n" +
            "-----choice: 해당 항목을 선택 했는지 여부(true or false)")
    public List<VoteDTO> voteChoice(@RequestBody Long vote_item_idx,@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        List<VoteDTO> voteDTOList = wearService.voteChoice(vote_item_idx, nickname);

        return voteDTOList;
    }

}