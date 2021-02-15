module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  plugins: [
    { src: '@/plugins/vue-infinite-loading.js', mode: 'client' }
  ],
  devServer: {
    port: process.env.VUE_APP_PORT || 3000
  }
}