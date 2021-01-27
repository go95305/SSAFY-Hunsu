<template>
<div>
  <h2>회원가입 추가정보 컴포넌트</h2>
  <v-avatar>
    <img
      src="https://cdn.vuetifyjs.com/images/john.jpg"
      alt="John"
    >
  </v-avatar>
    <v-row
    align="center"
    justify="space-around"
  >
    <v-btn
      tile
      color="success"
    >
      Edit
    </v-btn>
  </v-row>
  <v-text-field
    label="닉네임을 입력해주세요(필수)"
    :rules="rules"
    hide-details="auto"
    style="width: 250px"
  ></v-text-field>

  <h3>추가정보(선택)</h3>
  추천 서비스제공에 사용되는 정보입니다.
    <v-text-field
    label="키(cm)"
    hide-details="auto"
    style="width: 250px"
  ></v-text-field>
  <v-container fluid>
    <v-row align="center">
      <v-col
        class="d-flex"
        cols="12"
        sm="6"
      >
        <v-select
          :items="items"
          label="사이즈"
        ></v-select>
      </v-col>
    </v-row>
  </v-container>
</div>
</template>

<script>
export default {
  name: "SignupInfo",
  data: () => ({
    rules: [
      value => !!value || 'Required.',
      value => (value || '').length <= 20 || 'Max 20 characters',
      value => {
        const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        return pattern.test(value) || 'Invalid e-mail.'
      },
    ],
    items: ['XXS', 'XS', 'S', 'M', 'L', 'XL', 'XXL'],
  }),
}
</script>

<style>

</style>