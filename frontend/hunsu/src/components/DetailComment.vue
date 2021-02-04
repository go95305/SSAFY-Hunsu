<template>
<!-- OOTD, WHATWEAR 공통 댓글창 -->
  <div>
    <v-card
      flat>
      <v-list three-line>
        <template v-for="(item, index) in items">
          <!--댓글-->
          <v-subheader
            v-if="item.header"
            :key="index"
            v-text="item.header"
          ></v-subheader>

          <!--댓글출력되는부분-->
          <v-list-item
            v-else
            :key="item.title"
          >
          <!--프로필이미지-->
            <v-list-item-avatar>
              <v-img :src="item.avatar"></v-img>
            </v-list-item-avatar>
            <!--title자리가 댓글, sub가 좋아요 수정 자리-->
            <v-list-item-content>
              <v-list-item-title v-html="item.title"></v-list-item-title>
              <v-list-item-subtitle v-html="item.subtitle"></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-card>
    <v-container fluid>
      <v-row>
        <v-col
          cols="12"
          sm="6"
        >
          <v-textarea
            label="댓글쓰기"
            auto-grow
            outlined
            rows="3"
            row-height="25"
          ></v-textarea>
        </v-col>

      </v-row>
    </v-container>
  </div>
</template>

<script>
  export default {
    name: "DetailComment",
    data: () => ({
      items: [
        { header: '댓글' },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          title: 'Brunch this weekend?',
          subtitle: `<span class="text--primary">Ali Connors</span> &mdash; I'll be in your neighborhood doing errands this weekend. Do you want to hang out?`,
        },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
          title: 'Summer BBQ <span class="grey--text text--lighten-1">4</span>',
          subtitle: `<span class="text--primary">to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend.`,
        },

        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
          title: 'Oui oui',
          subtitle: '<span class="text--primary">Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?',
        },

        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
          title: 'Birthday gift',
          subtitle: '<span class="text--primary">Trevor Hansen</span> &mdash; Have any ideas about what we should get Heidi for her birthday?',
        },

        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
          title: 'Recipe to try',
          subtitle: '<span class="text--primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash, Corn, and tomatillo Tacos.',
        },
      ],
    }),
  }
</script>

<style>

</style>