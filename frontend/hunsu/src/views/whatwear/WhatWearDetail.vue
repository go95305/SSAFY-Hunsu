<template>
  <v-card height="100%">
    <div id="whatwear_profile">
      <v-list one-line>
        <v-list-item>
          <v-list-item-content class="pb-0">
            <v-list-item-title class="text-h6">
              {{ whatwearInfo.title }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-list one-line>
        <v-list-item>
          <!--작성자정보-->
          <v-list-item-avatar>
            <!--프로필이미지-->
            <v-img :src="getTargetProfileImage" alt="John" width="100"></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            <!--닉네임-->
            <v-list-item-title class="text-subtitle-1">
              {{ whatwearInfo.nickname }}
            </v-list-item-title>
          </v-list-item-content>

          <!--작성자nickname과 로그인nickname이 같을때만 삭제버튼 출력-->
          <v-menu bottom v-if="whatwearInfo.nickname === getNickname">
            <template v-slot:activator="{ on, attrs }">
              <v-avatar v-bind="attrs" v-on="on">
                <v-btn color="black" icon class="d-inline-block">
                  <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
              </v-avatar>
            </template>
            <v-list>
              <v-dialog v-model="deleteDialog" persistent>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="red accent-4"
                    dark
                    v-bind="attrs"
                    v-on="on"
                    text
                  >
                    삭제
                  </v-btn>
                </template>
                <v-card>
                  <v-card-title class="mb-3 font-weight-bold"
                    >해당 글을 삭제하시겠습니까?</v-card-title
                  >
                  <v-card-subtitle>사진과 글이 삭제됩니다.</v-card-subtitle>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                      class="font-weight-bold"
                      color="primary"
                      text
                      @click="deleteDialog = false"
                    >
                      취소
                    </v-btn>
                    <v-btn
                      class="font-weight-bold"
                      color="error"
                      text
                      @click="
                        [
                          (deleteDialog = false),
                          deleteWhatWear(whatwearInfo.wear_idx),
                        ]
                      "
                    >
                      삭제하기
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-list>
          </v-menu>
        </v-list-item>
      </v-list>
    </div>
    <!--투표x 사진-->
    <div v-if="!whatwearInfo.vote_activated">
      <v-carousel
        v-if="getWhatwearInfoImages"
        v-model="model"
        hide-delimiter-background
      >
        <v-carousel-item
          v-for="(imageUrl, i) in getWhatwearInfoImages"
          :key="i"
        >
          <v-sheet height="100%" tile>
            <v-row class="fill-height" align="center" justify="center">
              <!-- <div class="display-3">Slide {{ i + 1 }}</div> -->
              <v-img :src="imageUrl" />
            </v-row>
          </v-sheet>
        </v-carousel-item>
      </v-carousel>
    </div>
    <!--투표사진-->
    <div id="vote" v-if="whatwearInfo.vote_activated">
      <ImageView :images="whatwearInfo.imageUrls" />
    </div>

    <!--글 내용-->
    <v-list one-line>
      <v-list-item>
        <v-list-item-title class="text-body-2">
          {{ whatwearInfo.content }}
        </v-list-item-title>
      </v-list-item>
    </v-list>

    <!--투표창-->
    <div v-if="whatwearInfo.vote_activated">
      <!-- <div id="vote_input">
        <v-checkbox
          v-for="(n, index) in getWhatwearVoteInfo"
          :key="n.idx"
          :label="`${index + 1}번`"
          v-model="n.choice"
          @click="voteWhatwear(getWhatwearVoteInfo[index].idx, getNickname)"
        ></v-checkbox>
      </div> -->
      <v-row justify="center" class="mb-5">
        <v-dialog v-model="dialog" scrollable max-width="300px">
          <template v-slot:activator="{ on, attrs }">
            <!--마감이 true면 버튼을 안보여준다. false일때만 보여준다.-->
            <v-btn
              v-if="getVoteTime === false"
              color="black"
              dark
              v-bind="attrs"
              v-on="on"
              text
            >
              Choice✨
            </v-btn>
            <v-btn v-else text disabled>투표가 마감되었습니다</v-btn>
          </template>
          <v-card>
            <v-card-title>투표창</v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 300px">
              <v-card-subtitle class="pa-0 mt-4">중복투표가능</v-card-subtitle>
              <v-checkbox
                v-for="(n, index) in getWhatwearVoteInfo"
                :key="n.idx"
                :label="`${index + 1}번`"
                v-model="n.choice"
                @click="
                  voteWhatwear(getWhatwearVoteInfo[index].idx, getNickname)
                "
              ></v-checkbox>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn color="gray" text @click="dialog = false"> 취소 </v-btn>
              <v-btn color="blue darken-1" text @click="dialog = false">
                확인
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
      <!--투표결과그래프-->
      <div v-if="getVoteTotal">
        <div
          id="vote_linear"
          v-for="(value, idx) in getWhatwearVoteInfo"
          :key="idx"
        >
          <v-progress-linear
            color="light-blue"
            height="10"
            :value="(value.count / getVoteTotal) * 100"
            striped
          ></v-progress-linear>
          <br />
        </div>
      </div>

      <div v-if="getVoteTotal === 0">
        <div
          id="vote_linear"
          v-for="(value, idx) in getWhatwearVoteInfo"
          :key="idx"
        >
          <v-progress-linear
            color="light-blue"
            height="10"
            value="0"
            striped
          ></v-progress-linear>
          <br />
        </div>
      </div>
    </div>
    <WhatWearDetailComment />
  </v-card>
</template>


<script>
import WhatWearDetailComment from "@/components/whatwear/WhatWearDetailComment";
import { mapActions, mapGetters, mapMutations } from "vuex";
import ImageView from "@/components/module/ImageView";
import { rscApi } from "@/services/api";

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearDetailComment,
    ImageView,
  },
  computed: {
    ...mapGetters([
      "getWhatwearInfo",
      "getWhatwearVoteInfo",
      "getNickname",
      "getVoteTotal",
      "getTargetProfileImage",
      "getVoteTime",
      "getWhatwearInfoImages",
    ]),
  },
  data() {
    return {
      model: 0,
      cycle: false,
      colors: ["primary", "secondary", "yellow darken-2"],
      dialog: false,
      deleteDialog: false,
      vote_activated: false,
      voteImages: [],
      endTimeCheck: this.getVoteTime,
      images: [],
      whatwearInfo: {},
    };
  },
  mounted() {
    console.log(this.getWhatwearInfo);
    this.whatwearInfo = this.getWhatwearInfo;
  },
  methods: {
    ...mapMutations(["setWhatwearInfo"]),
    ...mapActions(["getWhatwearInfoApi", "voteWhatwearInfo", "getImageList"]),
    // 글 삭제 함수
    deleteWhatWear(wear_idx) {
      // const wearIdx = wear_idx;
      rscApi
        .put(`wear?wear_idx=${wear_idx}`)
        .then((res) => {
          console.log(res);
          this.$router.push({ name: "WhatWear" });
        })
        .catch((err) => {
          console.error(err);
        });
    },
    // 투표하는 함수
    voteWhatwear(voteIdx) {
      this.voteWhatwearInfo(voteIdx)
    },
  },
};
</script>

<style>
#vote_input {
  margin-left: 18px;
  text-align: center;
}
#vote_linear {
  margin-left: 40px;
  margin-right: 40px;
}
</style>